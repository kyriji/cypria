package dev.kyriji.common.messaging.models;

public abstract class RedisMessageResponse {
	public boolean success;

	public RedisMessageResponse(boolean success) {
		this.success = success;
	}
}
