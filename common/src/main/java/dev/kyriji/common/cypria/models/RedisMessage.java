package dev.kyriji.common.cypria.models;

import com.google.gson.Gson;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.MessageIdentifier;
import dev.kyriji.common.cypria.enums.MessageType;

import java.util.UUID;
import java.util.function.Consumer;

import static dev.kyriji.common.cypria.controllers.RedisManager.CHANNEL_NAME;

public abstract class RedisMessage<T extends MessageResponse> {

	private transient final Gson gson = new Gson();
	private final MessageIdentifier messageIdentifier;
	private final MessageType messageType;
	private final String replyIdentifier;

	private Consumer<T> response;

	public RedisMessage(MessageIdentifier messageIdentifier, MessageType messageType) {
		this.messageIdentifier = messageIdentifier;
		this.messageType = messageType;

		this.replyIdentifier = UUID.randomUUID().toString();
	}

	public void send() {
		send(null);
	}

	public RedisMessage<T> send(Consumer<T> response) {
		this.response = response;

		StringBuilder message = new StringBuilder(messageIdentifier.name())
				.append("\\|")
				.append(replyIdentifier)
				.append("\\|")
				.append(messageType.name())
				.append("\\|");

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
		T response = gson.fromJson(content, this.getClass().getGenericSuperclass());
		if(this.response != null) {
			this.response.accept(response);
		}
	}

	public void respond(T response) {
		String objectString = gson.toJson(response);
		String messageIdentifier = this.messageIdentifier.name();

		CypriaCommon.getRedisManager().getConnection().publish(CHANNEL_NAME, messageIdentifier + "\\|" +
				replyIdentifier + "\\|" + MessageType.INSTANCE_BOUND.name() + "\\|" + objectString);
	}
}
