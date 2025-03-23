package dev.kyriji.cypria.models;

public abstract class MessageResponse {
	public boolean success;

	public MessageResponse(boolean success) {
		this.success = success;
	}

	public abstract void loadFromString(String[] values);
}
