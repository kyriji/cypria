package dev.kyriji.common.cypria.controllers;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.MessageDirection;
import dev.kyriji.common.cypria.enums.MessageIdentifier;
import dev.kyriji.common.cypria.enums.MessageType;
import dev.kyriji.common.cypria.models.MessageListener;
import dev.kyriji.common.cypria.models.RedisMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
	private final List<RedisMessage<?>> awaitingMessages;
	private final List<MessageListener<?>> listeners;

	public MessageManager() {
		this.awaitingMessages = new ArrayList<>();
		this.listeners = new ArrayList<>();

		CypriaCommon.getRedisManager().addListener(message -> {
			String[] messageParts = message.split("\\|");

			if(messageParts.length < 5) return;

			MessageType messageType = MessageType.valueOf(messageParts[0]);
			MessageIdentifier messageIdentifier = MessageIdentifier.valueOf(messageParts[1]);
			String replyIdentifier = messageParts[2];
			MessageDirection messageDirection = MessageDirection.valueOf(messageParts[3]);
			String content = messageParts[4];

			if(messageDirection != CypriaCommon.getRunContext().getAcceptedDirection()) return;

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
