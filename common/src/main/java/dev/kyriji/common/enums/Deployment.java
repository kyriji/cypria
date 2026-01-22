package dev.kyriji.common.enums;

import dev.kyriji.common.messaging.enums.MessageDirection;

public enum Deployment {
	PIT("pit", MessageDirection.INSTANCE_BOUND),
	;

	private final String deploymentName;
	private final MessageDirection acceptedDirection;

	Deployment(String deploymentName, MessageDirection acceptedDirection) {
		this.deploymentName = deploymentName;
		this.acceptedDirection = acceptedDirection;
	}

	public String getDeploymentName() {
		return deploymentName;
	}

	public MessageDirection getAcceptedDirection() {
		return acceptedDirection;
	}
}
