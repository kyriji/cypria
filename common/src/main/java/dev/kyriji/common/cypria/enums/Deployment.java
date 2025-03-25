package dev.kyriji.common.cypria.enums;

import dev.kyriji.common.cypria.messaging.enums.MessageDirection;

public enum Deployment {
	HUB("cypria-hub", MessageDirection.INSTANCE_BOUND),
	ISLANDS("cypria-islands", MessageDirection.INSTANCE_BOUND),
	MANAGER("cypria-manager-dev", MessageDirection.MANAGER_BOUND),
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
