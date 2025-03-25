package dev.kyriji.common.cypria.messaging.messages;

import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.common.cypria.messaging.enums.MessageDirection;
import dev.kyriji.common.cypria.messaging.enums.MessageIdentifier;
import dev.kyriji.common.cypria.messaging.models.RedisMessageResponse;
import dev.kyriji.common.cypria.messaging.models.RedisMessage;

import java.util.UUID;

public class MessageQueueRequest extends RedisMessage<MessageQueueRequest.Response> {

	private final UUID playerUUID;
	private final Deployment deployment;

	public MessageQueueRequest(UUID playerUUID, Deployment deployment) {
		super(MessageIdentifier.QUEUE_REQUEST, MessageDirection.MANAGER_BOUND);

		this.playerUUID = playerUUID;
		this.deployment = deployment;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public Deployment getDeployment() {
		return deployment;
	}

	public static class Response extends RedisMessageResponse {

		public String message;

		public Response(boolean success, String message) {
			super(success);

			this.message = message;
		}

	}
}
