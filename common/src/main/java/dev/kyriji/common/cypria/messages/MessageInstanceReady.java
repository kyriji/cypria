package dev.kyriji.common.cypria.messages;

import dev.kyriji.bigminecraftapi.controllers.NetworkManager;
import dev.kyriji.common.cypria.enums.MessageIdentifier;
import dev.kyriji.common.cypria.enums.MessageDirection;
import dev.kyriji.common.cypria.models.RedisMessageResponse;
import dev.kyriji.common.cypria.models.RedisMessage;

public class MessageInstanceReady extends RedisMessage<MessageInstanceReady.Response> {

	public final String address;

	public MessageInstanceReady() {
		super(MessageIdentifier.INSTANCE_READY, MessageDirection.MANAGER_BOUND);

		this.address = NetworkManager.getIPAddress();
	}

	public static class Response extends RedisMessageResponse {

		public Response(boolean success) {
			super(success);
		}

		@Override
		public void loadFromString(String[] values) {

		}
	}
}
