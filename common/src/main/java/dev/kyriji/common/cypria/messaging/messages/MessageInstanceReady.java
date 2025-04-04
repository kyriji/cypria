package dev.kyriji.common.cypria.messaging.messages;

import dev.kyriji.bigminecraftapi.controllers.NetworkManager;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messaging.enums.MessageIdentifier;
import dev.kyriji.common.cypria.messaging.enums.MessageDirection;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.common.cypria.messaging.models.RedisMessageResponse;
import dev.kyriji.common.cypria.messaging.models.RedisMessage;

public class MessageInstanceReady extends RedisMessage<MessageInstanceReady.Response> {

	public final String address;
	public final Deployment deployment;

	public MessageInstanceReady() {
		super(MessageIdentifier.INSTANCE_READY, MessageDirection.MANAGER_BOUND);

		this.address = NetworkManager.getIPAddress();
		this.deployment = CypriaCommon.getDeployment();
	}

	public static class Response extends RedisMessageResponse {

		public Response(boolean success) {
			super(success);
		}

	}
}
