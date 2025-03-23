package dev.kyriji.common.cypria.messages;

import dev.kyriji.common.cypria.enums.MessageIdentifier;
import dev.kyriji.common.cypria.enums.MessageType;
import dev.kyriji.common.cypria.models.MessageResponse;
import dev.kyriji.common.cypria.models.RedisMessage;

public class MessageInstanceReady extends RedisMessage<MessageInstanceReady.Response> {

	public final String instanceId;

	public MessageInstanceReady(String instanceId) {
		super(MessageIdentifier.INSTANCE_READY, MessageType.MANAGER_BOUND);

		this.instanceId = instanceId;
	}

	public static class Response extends MessageResponse {

		public Response(boolean success) {
			super(success);
		}

		@Override
		public void loadFromString(String[] values) {

		}
	}
}
