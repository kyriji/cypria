package dev.kyriji.common.cypria.messaging.models;

public abstract class RedisMessageResponse {
	public boolean success;

	public RedisMessageResponse(boolean success) {
		this.success = success;
	}

	public abstract void loadFromString(String[] values);
}
