/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.server.nodemanager.containermanager.monitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.AbstractService;
import org.apache.hadoop.util.StringUtils.TraditionalBinaryPrefix;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.server.nodemanager.ContainerExecutor;
import org.apache.hadoop.yarn.server.nodemanager.Context;
import org.apache.hadoop.yarn.server.nodemanager.ContextUpdateEvent;
import org.apache.hadoop.yarn.server.nodemanager.ContextUpdateEventType;
import org.apache.hadoop.yarn.server.nodemanager.NodeManager.NMContext;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.container.Container;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.container.ContainerImpl;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.container.ContainerKillEvent;
import org.apache.hadoop.yarn.util.ResourceCalculatorPlugin;
import org.apache.hadoop.yarn.util.ResourceCalculatorProcessTree;

import com.google.common.base.Preconditions;

public class ContainersMonitorImpl extends AbstractService implements
    ContainersMonitor {

  final static Log LOG = LogFactory
      .getLog(ContainersMonitorImpl.class);

  private long monitoringInterval;
  private MonitoringThread monitoringThread;

  final List<ContainerId> containersToBeRemoved;
  final Map<ContainerId, ProcessTreeInfo> containersToBeAdded;
  Map<ContainerId, ProcessTreeInfo> trackingContainers =
      new HashMap<ContainerId, ProcessTreeInfo>();

  final ContainerExecutor containerExecutor;
  private final Dispatcher eventDispatcher;
  private final Context context;
  private ResourceCalculatorPlugin resourceCalculatorPlugin;
  private Configuration conf;
  private Class<? extends ResourceCalculatorProcessTree> processTreeClass;

  private long maxVmemAllottedForContainers = UNKNOWN_MEMORY_LIMIT;
  private long maxPmemAllottedForContainers = UNKNOWN_MEMORY_LIMIT;

  private boolean pmemCheckEnabled;
  private boolean vmemCheckEnabled;
  private boolean containerElasticEnabled;
  private double containerExpandRatio;
  private double containerDecreaseRatio;

  private static final long UNKNOWN_MEMORY_LIMIT = -1L;

  public ContainersMonitorImpl(ContainerExecutor exec,
      AsyncDispatcher dispatcher, Context context) {
    super("containers-monitor");

    this.containerExecutor = exec;
    this.eventDispatcher = dispatcher;
    this.context = context;

    this.containersToBeAdded = new HashMap<ContainerId, ProcessTreeInfo>();
    this.containersToBeRemoved = new ArrayList<ContainerId>();
    this.monitoringThread = new MonitoringThread();
  }

  @Override
  protected void serviceInit(Configuration conf) throws Exception {
    this.monitoringInterval =
        conf.getLong(YarnConfiguration.NM_CONTAINER_MON_INTERVAL_MS,
            YarnConfiguration.DEFAULT_NM_CONTAINER_MON_INTERVAL_MS);

    Class<? extends ResourceCalculatorPlugin> clazz =
        conf.getClass(YarnConfiguration.NM_CONTAINER_MON_RESOURCE_CALCULATOR, null,
            ResourceCalculatorPlugin.class);
    this.resourceCalculatorPlugin =
        ResourceCalculatorPlugin.getResourceCalculatorPlugin(clazz, conf);
    LOG.info(" Using ResourceCalculatorPlugin : "
        + this.resourceCalculatorPlugin);
    processTreeClass = conf.getClass(YarnConfiguration.NM_CONTAINER_MON_PROCESS_TREE, null,
            ResourceCalculatorProcessTree.class);
    this.conf = conf;
    LOG.info(" Using ResourceCalculatorProcessTree : "
        + this.processTreeClass);

    long configuredPMemForContainers = conf.getLong(
        YarnConfiguration.NM_PMEM_MB,
        YarnConfiguration.DEFAULT_NM_PMEM_MB) * 1024 * 1024l;

    // Setting these irrespective of whether checks are enabled. Required in
    // the UI.
    // ///////// Physical memory configuration //////
    this.maxPmemAllottedForContainers = configuredPMemForContainers;
    
    // ///////// Container Elastic configuration //////
    this.containerElasticEnabled = conf.getBoolean(YarnConfiguration.YARN_CONTAINER_ELASTIC,
    						YarnConfiguration.DEFAULT_YARN_CONTAINER_ELASTIC);
    this.containerExpandRatio = conf.getDouble(YarnConfiguration.YARN_CONTAINER_ELASTIC_EXPAND_THRESHOLD, 
    						YarnConfiguration.DEFAULT_YARN_CONTAINER_ELASTIC_EXPAND_THRESHOLD);
    this.containerDecreaseRatio = conf.getDouble(YarnConfiguration.YARN_CONTAINER_ELASTIC_DECREASE_THRESHOLD, 
    						YarnConfiguration.DEFAULT_YARN_CONTAINER_ELASTIC_DECREASE_THRESHOLD);
    
    // ///////// Virtual memory configuration //////
    float vmemRatio = conf.getFloat(YarnConfiguration.NM_VMEM_PMEM_RATIO,
        YarnConfiguration.DEFAULT_NM_VMEM_PMEM_RATIO);
    Preconditions.checkArgument(vmemRatio > 0.99f,
        YarnConfiguration.NM_VMEM_PMEM_RATIO + " should be at least 1.0");
    this.maxVmemAllottedForContainers =
        (long) (vmemRatio * configuredPMemForContainers);

    pmemCheckEnabled = conf.getBoolean(YarnConfiguration.NM_PMEM_CHECK_ENABLED,
        YarnConfiguration.DEFAULT_NM_PMEM_CHECK_ENABLED);
    vmemCheckEnabled = conf.getBoolean(YarnConfiguration.NM_VMEM_CHECK_ENABLED,
        YarnConfiguration.DEFAULT_NM_VMEM_CHECK_ENABLED);
    LOG.info("Physical memory check enabled: " + pmemCheckEnabled);
    LOG.info("Virtual memory check enabled: " + vmemCheckEnabled);

    if (pmemCheckEnabled) {
      // Logging if actual pmem cannot be determined.
      long totalPhysicalMemoryOnNM = UNKNOWN_MEMORY_LIMIT;
      if (this.resourceCalculatorPlugin != null) {
        totalPhysicalMemoryOnNM = this.resourceCalculatorPlugin
            .getPhysicalMemorySize();
        if (totalPhysicalMemoryOnNM <= 0) {
          LOG.warn("NodeManager's totalPmem could not be calculated. "
              + "Setting it to " + UNKNOWN_MEMORY_LIMIT);
          totalPhysicalMemoryOnNM = UNKNOWN_MEMORY_LIMIT;
        }
      }

      if (totalPhysicalMemoryOnNM != UNKNOWN_MEMORY_LIMIT &&
          this.maxPmemAllottedForContainers > totalPhysicalMemoryOnNM * 0.80f) {
        LOG.warn("NodeManager configured with "
            + TraditionalBinaryPrefix.long2String(maxPmemAllottedForContainers,
                "", 1)
            + " physical memory allocated to containers, which is more than "
            + "80% of the total physical memory available ("
            + TraditionalBinaryPrefix.long2String(totalPhysicalMemoryOnNM, "",
                1) + "). Thrashing might happen.");
      }
    }
    super.serviceInit(conf);
  }

  private boolean isEnabled() {
    if (resourceCalculatorPlugin == null) {
            LOG.info("ResourceCalculatorPlugin is unavailable on this system. "
                + this.getClass().getName() + " is disabled.");
            return false;
    }
    if (ResourceCalculatorProcessTree.getResourceCalculatorProcessTree("0", processTreeClass, conf) == null) {
        LOG.info("ResourceCalculatorProcessTree is unavailable on this system. "
                + this.getClass().getName() + " is disabled.");
            return false;
    }
    if (!(isPmemCheckEnabled() || isVmemCheckEnabled())) {
      LOG.info("Neither virutal-memory nor physical-memory monitoring is " +
          "needed. Not running the monitor-thread");
      return false;
    }

    return true;
  }

  @Override
  protected void serviceStart() throws Exception {
    if (this.isEnabled()) {
      this.monitoringThread.start();
    }
    super.serviceStart();
  }

  @Override
  protected void serviceStop() throws Exception {
    if (this.isEnabled()) {
      this.monitoringThread.interrupt();
      try {
        this.monitoringThread.join();
      } catch (InterruptedException e) {
        ;
      }
    }
    super.serviceStop();
  }

  private static class ProcessTreeInfo {
    private ContainerId containerId;
    private String pid;
    private ResourceCalculatorProcessTree pTree;
    private long vmemLimit;
    private long pmemLimit;
    /*
     * pmemUpperLimit records the original memory limit value.
     * As pmemLimit will change during time if elastic container enabled. 
     */
    private long pmemUpperLimit;
    private boolean longRunMark;

    public ProcessTreeInfo(ContainerId containerId, String pid,
        ResourceCalculatorProcessTree pTree, long vmemLimit, long pmemLimit,
        boolean longRunMark) {
      this.containerId = containerId;
      this.pid = pid;
      this.pTree = pTree;
      this.vmemLimit = vmemLimit;
      this.pmemLimit = pmemLimit;
      this.pmemUpperLimit = pmemLimit;
      this.longRunMark = longRunMark;
    }

    public ContainerId getContainerId() {
      return this.containerId;
    }

    public String getPID() {
      return this.pid;
    }

    public void setPid(String pid) {
      this.pid = pid;
    }

    public ResourceCalculatorProcessTree getProcessTree() {
      return this.pTree;
    }

    public void setProcessTree(ResourceCalculatorProcessTree pTree) {
      this.pTree = pTree;
    }

    public long getVmemLimit() {
      return this.vmemLimit;
    }

    /**
     * @return Physical memory limit for the process tree in bytes
     */
    public long getPmemLimit() {
      return this.pmemLimit;
    }
    
    public void setPmemLimit(long memLimit) {
    	this.pmemLimit = memLimit;
    }
    
    public boolean getLongRunMark() {
    	return this.longRunMark;
    }

	public long getPmemUpperLimit() {
		return pmemUpperLimit;
	}

	public void setPmemUpperLimit(long pmemUpperLimit) {
		this.pmemUpperLimit = pmemUpperLimit;
	}
  }

  /**
   * Get the total memory allocated for containers on this node.
   * 
   * Get container list from context and add memory allocated for them
   * all together.
   * 
   * @return a long int represents total memory allocated in Byte.
   */
  long getTotalMemoryInUse() {
	  //ConcurrentMap<ContainerId, Container>  containers = this.context.getContainers();
	  synchronized (this.trackingContainers) {
		  long totalMemoryAllocated = 0;
		  for(Iterator<Map.Entry<ContainerId, ProcessTreeInfo>> it =
				  this.trackingContainers.entrySet().iterator(); it.hasNext();) {
			  Map.Entry<ContainerId, ProcessTreeInfo> entry = it.next();
			  ProcessTreeInfo ptInfo = entry.getValue();
			  totalMemoryAllocated += ptInfo.getPmemLimit();
		  }
		  return totalMemoryAllocated;
	  }
	  
  }
  

  /**
   * Check whether a container's process tree's current memory usage is over
   * limit.
   *
   * When a java process exec's a program, it could momentarily account for
   * double the size of it's memory, because the JVM does a fork()+exec()
   * which at fork time creates a copy of the parent's memory. If the
   * monitoring thread detects the memory used by the container tree at the
   * same instance, it could assume it is over limit and kill the tree, for no
   * fault of the process itself.
   *
   * We counter this problem by employing a heuristic check: - if a process
   * tree exceeds the memory limit by more than twice, it is killed
   * immediately - if a process tree has processes older than the monitoring
   * interval exceeding the memory limit by even 1 time, it is killed. Else it
   * is given the benefit of doubt to lie around for one more iteration.
   *
   * @param containerId
   *          Container Id for the container tree
   * @param currentMemUsage
   *          Memory usage of a container tree
   * @param curMemUsageOfAgedProcesses
   *          Memory usage of processes older than an iteration in a container
   *          tree
   * @param vmemLimit
   *          The limit specified for the container
   * @return true if the memory usage is more than twice the specified limit,
   *         or if processes in the tree, older than this thread's monitoring
   *         interval, exceed the memory limit. False, otherwise.
   */
  boolean isProcessTreeOverLimit(String containerId,
                                  long currentMemUsage,
                                  long curMemUsageOfAgedProcesses,
                                  long vmemLimit) {
    boolean isOverLimit = false;

    if (currentMemUsage > (2 * vmemLimit)) {
      LOG.warn("Process tree for container: " + containerId
          + " running over twice " + "the configured limit. Limit=" + vmemLimit
          + ", current usage = " + currentMemUsage);
      isOverLimit = true;
    } else if (curMemUsageOfAgedProcesses > vmemLimit) {
      LOG.warn("Process tree for container: " + containerId
          + " has processes older than 1 "
          + "iteration running over the configured limit. Limit=" + vmemLimit
          + ", current usage = " + curMemUsageOfAgedProcesses);
      isOverLimit = true;
    }

    return isOverLimit;
  }

  // method provided just for easy testing purposes
  boolean isProcessTreeOverLimit(ResourceCalculatorProcessTree pTree,
      String containerId, long limit) {
    long currentMemUsage = pTree.getCumulativeVmem();
    // as processes begin with an age 1, we want to see if there are processes
    // more than 1 iteration old.
    long curMemUsageOfAgedProcesses = pTree.getCumulativeVmem(1);
    return isProcessTreeOverLimit(containerId, currentMemUsage,
                                  curMemUsageOfAgedProcesses, limit);
  }

  private class MonitoringThread extends Thread {
    public MonitoringThread() {
      super("Container Monitor");
    }

    @Override
    public void run() {

      while (true) {

        // Print the processTrees for debugging.
        if (LOG.isDebugEnabled()) {
          StringBuilder tmp = new StringBuilder("[ ");
          for (ProcessTreeInfo p : trackingContainers.values()) {
            tmp.append(p.getPID());
            tmp.append(" ");
          }
          LOG.debug("Current ProcessTree list : "
              + tmp.substring(0, tmp.length()) + "]");
        }

        // Add new containers
        synchronized (containersToBeAdded) {
          for (Entry<ContainerId, ProcessTreeInfo> entry : containersToBeAdded
              .entrySet()) {
            ContainerId containerId = entry.getKey();
            ProcessTreeInfo processTreeInfo = entry.getValue();
            LOG.info("Starting resource-monitoring for " + containerId);
            trackingContainers.put(containerId, processTreeInfo);
          }
          containersToBeAdded.clear();
        }

        // Remove finished containers
        synchronized (containersToBeRemoved) {
          for (ContainerId containerId : containersToBeRemoved) {
            trackingContainers.remove(containerId);
            LOG.info("Stopping resource-monitoring for " + containerId);
          }
          containersToBeRemoved.clear();
        }

        // Now do the monitoring for the trackingContainers
        // Check memory usage and kill any overflowing containers
        long vmemStillInUsage = 0;
        long pmemStillInUsage = 0;
        for (Iterator<Map.Entry<ContainerId, ProcessTreeInfo>> it =
            trackingContainers.entrySet().iterator(); it.hasNext();) {

          Map.Entry<ContainerId, ProcessTreeInfo> entry = it.next();
          ContainerId containerId = entry.getKey();
          ProcessTreeInfo ptInfo = entry.getValue();
          try {
            String pId = ptInfo.getPID();

            // Initialize any uninitialized processTrees
            if (pId == null) {
              // get pid from ContainerId
              pId = containerExecutor.getProcessId(ptInfo.getContainerId());
              if (pId != null) {
                // pId will be null, either if the container is not spawned yet
                // or if the container's pid is removed from ContainerExecutor
                LOG.debug("Tracking ProcessTree " + pId
                    + " for the first time");

                ResourceCalculatorProcessTree pt =
                    ResourceCalculatorProcessTree.getResourceCalculatorProcessTree(pId, processTreeClass, conf);
                ptInfo.setPid(pId);
                ptInfo.setProcessTree(pt);
              }
            }
            // End of initializing any uninitialized processTrees

            if (pId == null) {
              continue; // processTree cannot be tracked
            }
            
            
            ResourceCalculatorProcessTree pTree = ptInfo.getProcessTree();
            pTree.updateProcessTree();    // update process-tree
            long currentPmemUsage = pTree.getCumulativeRssmem();
            long pmemLimit = ptInfo.getPmemLimit();
            long pmemUpperLimit = ptInfo.getPmemUpperLimit();
            
            //adjust long-run container's memory limit.
            if(isContainerElasticEnabled() && ptInfo.getLongRunMark()) {
            	updateLongRunContainerResource(containerId, currentPmemUsage,
            			pmemLimit, pmemUpperLimit);
            	
            }

            LOG.debug("Constructing ProcessTree for : PID = " + pId
                + " ContainerId = " + containerId);
//            ResourceCalculatorProcessTree pTree = ptInfo.getProcessTree();
            pTree.updateProcessTree();    // update process-tree
            long currentVmemUsage = pTree.getCumulativeVmem();
            currentPmemUsage = pTree.getCumulativeRssmem();
            // as processes begin with an age 1, we want to see if there
            // are processes more than 1 iteration old.
            long curMemUsageOfAgedProcesses = pTree.getCumulativeVmem(1);
            long curRssMemUsageOfAgedProcesses = pTree.getCumulativeRssmem(1);
            long vmemLimit = ptInfo.getVmemLimit();
            pmemLimit = ptInfo.getPmemLimit();
            pmemUpperLimit = ptInfo.getPmemUpperLimit();
            LOG.info(String.format(
                "Memory usage of ProcessTree %s for container-id %s: ",
                     pId, containerId.toString()) +
                formatUsageString(currentVmemUsage, vmemLimit, currentPmemUsage, pmemLimit));

            
            
            boolean isMemoryOverLimit = false;
            String msg = "";
            if (isVmemCheckEnabled()
                && isProcessTreeOverLimit(containerId.toString(),
                    currentVmemUsage, curMemUsageOfAgedProcesses, vmemLimit)) {
              // Container (the root process) is still alive and overflowing
              // memory.
              // Dump the process-tree and then clean it up.
              msg = formatErrorMessage("virtual",
                  currentVmemUsage, vmemLimit,
                  currentPmemUsage, pmemLimit,
                  pId, containerId, pTree);
              isMemoryOverLimit = true;
            } else if (isPmemCheckEnabled()
                && isProcessTreeOverLimit(containerId.toString(),
                    currentPmemUsage, curRssMemUsageOfAgedProcesses,
                    pmemLimit)) {
              // Container (the root process) is still alive and overflowing
              // memory.
              // Dump the process-tree and then clean it up.
              msg = formatErrorMessage("physical",
                  currentVmemUsage, vmemLimit,
                  currentPmemUsage, pmemLimit,
                  pId, containerId, pTree);
              isMemoryOverLimit = true;
            }

            if (isMemoryOverLimit) {
              // Virtual or physical memory over limit. Fail the container and
              // remove
              // the corresponding process tree
              LOG.warn(msg);
              // warn if not a leader
              if (!pTree.checkPidPgrpidForMatch()) {
                LOG.error("Killed container process with PID " + pId
                    + " but it is not a process group leader.");
              }
              // kill the container
              eventDispatcher.getEventHandler().handle(
                  new ContainerKillEvent(containerId, msg));
              it.remove();
              LOG.info("Removed ProcessTree with root " + pId);
            } else {
              // Accounting the total memory in usage for all containers that
              // are still
              // alive and within limits.
              vmemStillInUsage += currentVmemUsage;
              pmemStillInUsage += currentPmemUsage;
            }
            
            
          } catch (Exception e) {
            // Log the exception and proceed to the next container.
            LOG.warn("Uncaught exception in ContainerMemoryManager "
                + "while managing memory of " + containerId, e);
          }
        }

        try {
          Thread.sleep(monitoringInterval);
        } catch (InterruptedException e) {
          LOG.warn(ContainersMonitorImpl.class.getName()
              + " is interrupted. Exiting.");
          break;
        }
      }
    }

    /**
     * check if current container memory usage exceed the threshold.
     * If larger than the expand threshold, then first check if the node have extra 
     * memory for use, if yes, expand container's memory to 1.2 times of original
     * allocation. 
     * If smaller than the decrease threshold, then decrease container's memory 
     * to half of original allocation.
     * 
     * @param containerId, id of container to be checked.
     * @param curMemUsageOfAgedProcesses, current memory usage.
     * @param pmemLimt, physical memory allocated to the container.
     */
    @SuppressWarnings("unchecked")
	private void updateLongRunContainerResource(ContainerId containerId,
			long curMemUsageOfAgedProcesses, long pmemLimit, long pmemUpperLimit) {
		BigDecimal b1 = new BigDecimal(Double.toString(curMemUsageOfAgedProcesses));
		BigDecimal b2 = new BigDecimal(Double.toString(pmemLimit));
		double ratio = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		long newPmemLimit = pmemLimit;
		boolean memChanged = false;
		if(ratio >= getContainerExpandRatio()) {
			// expand container's memory limit
			// memory already allocated to containers.
			long memoryTotalInUse = getTotalMemoryInUse();
			// total memory configured for containers
			long memoryTotalAvailable = getPmemAllocatedForContainers();
			long delta = memoryTotalAvailable - memoryTotalInUse;
			if(delta >= (long) pmemLimit * 0.5) {
				//node have enough memory to expand
				newPmemLimit = (long) (pmemLimit * 1.5);
			}
			else {
				newPmemLimit = pmemLimit + delta;
				LOG.info("expand memory from " + pmemLimit/1024/1024 + 
						", but node memory not enough with "+ delta/1024/1024+"M left");
			}
			/*
			if(newPmemLimit > pmemUpperLimit * 2) {
				// check if memory exceed the upper limit
				// TODO: here the num values need to be parameterized. 
				newPmemLimit = pmemUpperLimit * 2;
			}*/
			memChanged = true;
			LOG.info("expand memory of container from "+ pmemLimit/1024/1024 + " to "+ containerId.toString() + 
					" to " + newPmemLimit/1024/1024 + "MB");
		}
		else if(ratio <= getContainerDecreaseRatio()) {
			//decrease container's memory limit
			newPmemLimit = (long) (pmemLimit * 0.7);
			memChanged = true;
			LOG.info("decrease memory of container from"+ pmemLimit/1024/1024 + " to "+ containerId.toString() + 
					" to " + newPmemLimit/1024/1024 + "MB");
		}
		// update trackingContainers
		if(memChanged) {
			ProcessTreeInfo ptInfo = trackingContainers.get(containerId);
			ptInfo.setPmemLimit(newPmemLimit);
			trackingContainers.put(containerId, ptInfo);
		
			//update context and trigger ContextUpdateEvent
			ConcurrentMap<ContainerId, Container> containers = context.getContainers();
			Container c = containers.get(containerId);
			if(c == null) {
				LOG.info("Container " + containerId.toString() + " not exists in context!");
				return;
			}
			Resource r = c.getResource();
			Long newPmem = newPmemLimit/1024/1024;
			r.setMemory(newPmem.intValue());
			((ContainerImpl)c).setResource(r);
			containers.put(containerId, c);
			((NMContext)context).setContainers(containers);
			LOG.info("Container " + containerId.toString() + "resource changed to" + 
						c.getResource().toString());
			eventDispatcher.getEventHandler().handle(
	                new ContextUpdateEvent(containerId, c, ContextUpdateEventType.CONTAINER_PMEM_UPDATE));
	
		}
    }

	private String formatErrorMessage(String memTypeExceeded,
        long currentVmemUsage, long vmemLimit,
        long currentPmemUsage, long pmemLimit,
        String pId, ContainerId containerId, ResourceCalculatorProcessTree pTree) {
      return
        String.format("Container [pid=%s,containerID=%s] is running beyond %s memory limits. ",
            pId, containerId, memTypeExceeded) +
        "Current usage: " +
        formatUsageString(currentVmemUsage, vmemLimit,
                          currentPmemUsage, pmemLimit) +
        ". Killing container.\n" +
        "Dump of the process-tree for " + containerId + " :\n" +
        pTree.getProcessTreeDump();
    }

    private String formatUsageString(long currentVmemUsage, long vmemLimit,
        long currentPmemUsage, long pmemLimit) {
      return String.format("%sB of %sB physical memory used; " +
          "%sB of %sB virtual memory used",
          TraditionalBinaryPrefix.long2String(currentPmemUsage, "", 1),
          TraditionalBinaryPrefix.long2String(pmemLimit, "", 1),
          TraditionalBinaryPrefix.long2String(currentVmemUsage, "", 1),
          TraditionalBinaryPrefix.long2String(vmemLimit, "", 1));
    }
  }

  @Override
  public long getVmemAllocatedForContainers() {
    return this.maxVmemAllottedForContainers;
  }

  /**
   * Is the total physical memory check enabled?
   *
   * @return true if total physical memory check is enabled.
   */
  @Override
  public boolean isPmemCheckEnabled() {
    return this.pmemCheckEnabled;
  }

  @Override
  public long getPmemAllocatedForContainers() {
    return this.maxPmemAllottedForContainers;
  }

  /**
   * Is the total virtual memory check enabled?
   *
   * @return true if total virtual memory check is enabled.
   */
  @Override
  public boolean isVmemCheckEnabled() {
    return this.vmemCheckEnabled;
  }
  
  @Override
  public boolean isContainerElasticEnabled() {
	return this.containerElasticEnabled;
  }

	@Override
	public double getContainerExpandRatio() {
		return this.containerExpandRatio;
	}

	@Override
	public double getContainerDecreaseRatio() {
		return this.containerDecreaseRatio;
	}

  @Override
  public void handle(ContainersMonitorEvent monitoringEvent) {

    if (!isEnabled()) {
      return;
    }

    ContainerId containerId = monitoringEvent.getContainerId();
    switch (monitoringEvent.getType()) {
    case START_MONITORING_CONTAINER:
      ContainerStartMonitoringEvent startEvent =
          (ContainerStartMonitoringEvent) monitoringEvent;
      synchronized (this.containersToBeAdded) {
        ProcessTreeInfo processTreeInfo =
            new ProcessTreeInfo(containerId, null, null,
                startEvent.getVmemLimit(), startEvent.getPmemLimit(),
                startEvent.getLongRunMark());
        this.containersToBeAdded.put(containerId, processTreeInfo);
      }
      break;
    case STOP_MONITORING_CONTAINER:
      synchronized (this.containersToBeRemoved) {
        this.containersToBeRemoved.add(containerId);
      }
      break;
    default:
      // TODO: Wrong event.
    }
  }

	
}
