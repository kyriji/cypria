package dev.kyriji.cypria.messages;

import dev.kyriji.cypria.enums.MessageIdentifier;
import dev.kyriji.cypria.enums.MessageType;
import dev.kyriji.cypria.models.MessageResponse;
import dev.kyriji.cypria.models.RedisMessage;

public class MessageInstanceReady extends RedisMessage<MessageInstanceReady.InstanceReadyResponse> {

	public final String instanceId;

	public MessageInstanceReady(String instanceId) {
		super(MessageIdentifier.INSTANCE_READY, MessageType.INSTANCE_BOUND);

		this.instanceId = instanceId;
	}

	public static class InstanceReadyResponse extends MessageResponse {

		public InstanceReadyResponse(boolean success) {
			super(success);
		}

		@Override
		public void loadFromString(String[] values) {

		}
	}
}
