package dev.kyriji.common.messaging.models;

import dev.kyriji.common.messaging.enums.MessageIdentifier;

import java.util.function.Consumer;

import static dev.kyriji.common.HytaleCommon.gson;

public class MessageListener<T extends RedisMessage<?>> {
	private final Consumer<T> onMessageReceived;
	private final Class<T> messageClass;

	public MessageListener(Class<T> messageClass, Consumer<T> onMessageReceived) {
		this.onMessageReceived = onMessageReceived;
		this.messageClass = messageClass;
	}

	public void constructAndAccept(String content) {
		T object = gson.fromJson(content, messageClass);
		onMessageReceived.accept(object);
	}

	public MessageIdentifier getMessageIdentifier() {
		return MessageIdentifier.fromClass(messageClass);
	}
}
