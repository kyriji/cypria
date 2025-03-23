package dev.kyriji.common.cypria.models;

import com.google.gson.Gson;
import dev.kyriji.common.cypria.enums.MessageIdentifier;

import java.util.function.Consumer;

public class MessageListener<T extends RedisMessage<?>> {
	private final Consumer<T> onMessageReceived;
	private final Gson gson = new Gson();

	public MessageListener(Consumer<T> onMessageReceived) {
		this.onMessageReceived = onMessageReceived;
	}

	public void constructAndAccept(String content) {
		T object = gson.fromJson(content, this.getClass().getGenericSuperclass());
		onMessageReceived.accept(object);
	}

	public MessageIdentifier getMessageIdentifier() {
		return MessageIdentifier.fromClass((Class<? extends RedisMessage<?>>) this.getClass().getGenericSuperclass());
	}
}