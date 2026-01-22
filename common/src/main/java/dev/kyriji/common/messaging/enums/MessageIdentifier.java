package dev.kyriji.common.messaging.enums;

import dev.kyriji.common.messaging.models.RedisMessage;

public enum MessageIdentifier {
	// Add your message identifiers here
	// Example:
	// INSTANCE_READY(MessageInstanceReady.class),
	// QUEUE_REQUEST(MessageQueueRequest.class),
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
