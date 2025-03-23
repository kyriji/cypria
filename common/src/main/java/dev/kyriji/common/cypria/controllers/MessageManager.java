package dev.kyriji.common.cypria.controllers;

import com.google.gson.Gson;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.MessageType;
import dev.kyriji.common.cypria.models.MessageListener;
import dev.kyriji.common.cypria.models.RedisMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MessageManager {
	private final List<RedisMessage<?>> awaitingMessages;
	private final List<Consumer<RedisMessage<?>>> listeners;

	private final Gson gson = new Gson();

	public MessageManager() {
		this.awaitingMessages = new ArrayList<>();
		this.listeners = new ArrayList<>();

		CypriaCommon.getRedisManager().addListener(message -> {
			String[] messageParts = message.split("\\|");

			if(messageParts.length < 4) return;

			String messageIdentifier = messageParts[0];
			String replyIdentifier = messageParts[1];
			MessageType messageType = MessageType.valueOf(messageParts[2]);

			String content = messageParts[3];

			if(messageType == MessageType.INSTANCE_BOUND) {
				for(RedisMessage<?> awaitingMessage : awaitingMessages) {
					if(awaitingMessage.getReplyIdentifier().equals(replyIdentifier)) {
						awaitingMessage.handleResponse(content);
						awaitingMessages.remove(awaitingMessage);
						break;
					}
				}
			} else if(messageType == MessageType.MANAGER_BOUND) {
				listeners.forEach(listener -> {
					gson.fromJson(content, listener.getClass());
				});
			}
		});
	}

	public void addMessage(RedisMessage<?> message) {
		awaitingMessages.add(message);
	}

	public void addListener(Consumer<RedisMessage<?>> consumer) {
		listeners.add(consumer);
	}


}
