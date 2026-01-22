package dev.kyriji.common.messaging.controllers;

import dev.kyriji.common.HytaleCommon;
import dev.kyriji.common.messaging.enums.MessageDirection;
import dev.kyriji.common.messaging.enums.MessageIdentifier;
import dev.kyriji.common.messaging.enums.MessageType;
import dev.kyriji.common.messaging.models.MessageListener;
import dev.kyriji.common.messaging.models.RedisMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
	private final List<RedisMessage<?>> awaitingMessages;
	private final List<MessageListener<?>> listeners;

	public MessageManager() {
		this.awaitingMessages = new ArrayList<>();
		this.listeners = new ArrayList<>();

		HytaleCommon.getRedisManager().addListener(message -> {
			String[] messageParts = message.split("\\|");

			if(messageParts.length < 5) return;

			MessageType messageType = MessageType.valueOf(messageParts[0]);
			MessageIdentifier messageIdentifier = MessageIdentifier.valueOf(messageParts[1]);
			String replyIdentifier = messageParts[2];
			MessageDirection messageDirection = MessageDirection.valueOf(messageParts[3]);
			String content = messageParts[4];

			if(messageDirection != HytaleCommon.getDeployment().getAcceptedDirection()) return;

			if(messageType == MessageType.RESPONSE) {
				for(RedisMessage<?> awaitingMessage : awaitingMessages) {
					if(awaitingMessage.getReplyIdentifier().equals(replyIdentifier)) {
						awaitingMessage.handleResponse(content);
						awaitingMessages.remove(awaitingMessage);
						break;
					}
				}
			} else if(messageType == MessageType.REQUEST) {
				listeners.forEach(listener -> {
					if(listener.getMessageIdentifier().equals(messageIdentifier)) listener.constructAndAccept(content);
				});
			}
		});
	}

	public void addMessage(RedisMessage<?> message) {
		awaitingMessages.add(message);
	}

	public void addListener(MessageListener<?> consumer) {
		listeners.add(consumer);
	}
}
