package dev.kyriji.common.cypria.messaging.messages;

import dev.kyriji.common.cypria.messaging.enums.MessageDirection;
import dev.kyriji.common.cypria.messaging.enums.MessageIdentifier;
import dev.kyriji.common.cypria.messaging.models.RedisMessage;
import dev.kyriji.common.cypria.messaging.models.RedisMessageResponse;

import java.util.UUID;

public class MessageLoadPlayerData extends RedisMessage<MessageLoadPlayerData.Response> {

	private final String targetInstance;
	private final UUID playerUUID;

	public MessageLoadPlayerData(String targetInstance, UUID playerUUID) {
		super(MessageIdentifier.LOAD_PLAYER_DATA, MessageDirection.INSTANCE_BOUND);

		this.targetInstance = targetInstance;
		this.playerUUID = playerUUID;
	}

	public String getTargetInstance() {
		return targetInstance;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public static class Response extends RedisMessageResponse {

		public Response(boolean success) {
			super(success);
		}
	}
}
