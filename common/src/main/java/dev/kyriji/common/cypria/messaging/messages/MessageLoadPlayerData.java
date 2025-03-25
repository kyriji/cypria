package dev.kyriji.common.cypria.messaging.messages;

import dev.kyriji.common.cypria.messaging.enums.MessageDirection;
import dev.kyriji.common.cypria.messaging.enums.MessageIdentifier;
import dev.kyriji.common.cypria.messaging.models.RedisMessageResponse;
import dev.kyriji.common.cypria.messaging.models.RedisMessage;

import java.util.UUID;

public class MessageLoadPlayerData extends RedisMessage<MessageLoadPlayerData.Response> {

	private final UUID playerUUID;
	private final String instanceAddress;

	public MessageLoadPlayerData(UUID playerUUID, String instanceAddress) {
		super(MessageIdentifier.LOAD_PLAYER_DATA, MessageDirection.INSTANCE_BOUND);

		this.playerUUID = playerUUID;
		this.instanceAddress = instanceAddress;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public String getInstanceAddress() {
		return instanceAddress;
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
