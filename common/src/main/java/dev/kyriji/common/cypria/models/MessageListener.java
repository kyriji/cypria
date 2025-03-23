package dev.kyriji.common.cypria.models;

import com.google.gson.Gson;
import dev.kyriji.common.cypria.enums.MessageIdentifier;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import static dev.kyriji.common.cypria.CypriaCommon.gson;

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