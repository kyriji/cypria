package dev.kyriji.common.cypria.messages;

import dev.kyriji.bigminecraftapi.controllers.NetworkManager;
import dev.kyriji.common.cypria.enums.MessageIdentifier;
import dev.kyriji.common.cypria.enums.MessageType;
import dev.kyriji.common.cypria.models.MessageResponse;
import dev.kyriji.common.cypria.models.RedisMessage;

public class MessageInstanceReady extends RedisMessage<MessageInstanceReady.Response> {

	public final String address;

	public MessageInstanceReady() {
		super(MessageIdentifier.INSTANCE_READY, MessageType.MANAGER_BOUND);

		this.address = NetworkManager.getIPAddress();
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
