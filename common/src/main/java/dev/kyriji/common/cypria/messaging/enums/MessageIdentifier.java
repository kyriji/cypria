package dev.kyriji.common.cypria.messaging.enums;

import dev.kyriji.common.cypria.messaging.messages.MessageInstanceReady;
import dev.kyriji.common.cypria.messaging.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.messaging.messages.MessageQueueRequest;
import dev.kyriji.common.cypria.messaging.models.RedisMessage;

public enum MessageIdentifier {
	//Manager Bound
	INSTANCE_READY(MessageInstanceReady.class),
	QUEUE_REQUEST(MessageQueueRequest.class),

	//Instance Bound
	LOAD_PLAYER_DATA(MessageLoadPlayerData.class),
	;

	public final Class<? extends RedisMessage<?>> clazz;

	MessageIdentifier(Class<? extends RedisMessage<?>> clazz) {
		this.clazz = clazz;
	}

	public static MessageIdentifier fromClass(Class<? extends RedisMessage<?>> clazz) {
		for(MessageIdentifier identifier : values()) {
			if(identifier.clazz.equals(clazz)) return identifier;
		}

		throw new IllegalArgumentException("No identifier found for class " + clazz.getName());
	}
}
