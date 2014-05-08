package org.apache.hadoop.yarn.server.nodemanager;

import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.event.AbstractEvent;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.container.Container;

public class ContextUpdateEvent extends AbstractEvent<ContextUpdateEventType> {

	private final ContainerId containerId;
	private final Container container;
	
	public ContextUpdateEvent(ContainerId containerId, Container c, 
			ContextUpdateEventType type) {
		super(type);
		this.containerId = containerId;
		this.container = c;
	}

	public ContainerId getContainerId() {
		return containerId;
	}

	public Container getContainer() {
		return container;
	}
}
