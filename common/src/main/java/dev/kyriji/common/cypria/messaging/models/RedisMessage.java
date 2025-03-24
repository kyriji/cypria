package dev.kyriji.common.cypria.messaging.models;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messaging.enums.MessageIdentifier;
import dev.kyriji.common.cypria.messaging.enums.MessageDirection;
import dev.kyriji.common.cypria.messaging.enums.MessageType;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;
import java.util.function.Consumer;

import static dev.kyriji.common.cypria.CypriaCommon.gson;
import static dev.kyriji.common.cypria.controllers.RedisManager.CHANNEL_NAME;

public abstract class RedisMessage<T extends RedisMessageResponse> {

	private final MessageIdentifier messageIdentifier;
	private final MessageDirection messageDirection;
	private final String replyIdentifier;

	private transient Consumer<T> response;

	public RedisMessage(MessageIdentifier messageIdentifier, MessageDirection messageDirection) {
		this.messageIdentifier = messageIdentifier;
		this.messageDirection = messageDirection;

		this.replyIdentifier = UUID.randomUUID().toString();
	}

	public void send() {
		send(null);
	}

	public RedisMessage<T> send(Consumer<T> response) {
		this.response = response;

		StringBuilder message = new StringBuilder(MessageType.REQUEST.name())
				.append("|")
				.append(messageIdentifier.name())
				.append("|")
				.append(replyIdentifier)
				.append("|")
				.append(messageDirection.name())
				.append("|");

		String objectString = gson.toJson(this);
		message.append(objectString);

		CypriaCommon.getRedisManager().getConnection().publish(CHANNEL_NAME, message.toString());
		CypriaCommon.getMessageManager().addMessage(this);
		return this;
	}

	public String getReplyIdentifier() {
		return replyIdentifier;
	}

	public MessageIdentifier getMessageIdentifier() {
		return messageIdentifier;
	}

	public void handleResponse(String content) {
		T response = gson.fromJson(content, ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		if(this.response != null) {
			this.response.accept(response);
		}
	}

	public void respond(T response) {
		String objectString = gson.toJson(response);
		String messageIdentifier = this.messageIdentifier.name();
		String messageType = MessageType.RESPONSE.name();

		CypriaCommon.getRedisManager().getConnection().publish(CHANNEL_NAME, messageType + "|" + messageIdentifier + "|" +
				replyIdentifier + "|" + getResponseDirection().name() + "|" + objectString);
	}

	public MessageDirection getResponseDirection() {
		return messageDirection == MessageDirection.INSTANCE_BOUND ? MessageDirection.MANAGER_BOUND : MessageDirection.INSTANCE_BOUND;
	}
}
