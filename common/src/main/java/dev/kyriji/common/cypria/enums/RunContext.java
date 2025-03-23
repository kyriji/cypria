package dev.kyriji.common.cypria.enums;

public enum RunContext {
	PLUGIN(MessageDirection.INSTANCE_BOUND),
	MANAGER(MessageDirection.MANAGER_BOUND),
	;

	private final MessageDirection acceptedDirection;

	RunContext(MessageDirection acceptedDirection) {
		this.acceptedDirection = acceptedDirection;
	}

	public MessageDirection getAcceptedDirection() {
		return acceptedDirection;
	}
}
