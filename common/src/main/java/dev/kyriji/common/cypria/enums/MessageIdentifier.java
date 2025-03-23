package dev.kyriji.common.cypria.enums;

import dev.kyriji.common.cypria.messages.MessageInstanceReady;
import dev.kyriji.common.cypria.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.models.RedisMessage;

public enum MessageIdentifier {
	INSTANCE_READY(MessageInstanceReady.class),
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
