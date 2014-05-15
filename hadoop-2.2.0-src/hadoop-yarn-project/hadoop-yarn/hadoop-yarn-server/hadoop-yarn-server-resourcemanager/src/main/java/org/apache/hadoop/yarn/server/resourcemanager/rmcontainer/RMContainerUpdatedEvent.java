package org.apache.hadoop.yarn.server.resourcemanager.rmcontainer;

import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.Resource;

public class RMContainerUpdatedEvent extends RMContainerEvent {
	
	private Resource resource;

	public RMContainerUpdatedEvent(ContainerId containerId, Resource resource,
			RMContainerEventType type) {
		super(containerId, type);
		this.resource = resource;
		
	}

	public Resource getResource() {
		return resource;
	}


}
