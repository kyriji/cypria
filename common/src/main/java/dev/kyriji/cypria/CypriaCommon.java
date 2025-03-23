package dev.kyriji.cypria;

import dev.kyriji.cypria.controllers.MessageManager;
import dev.kyriji.cypria.controllers.RedisManager;

public class CypriaCommon {
	private static CypriaCommon INSTANCE;

	private final RedisManager redisManager;
	private final MessageManager messageManager;

	public CypriaCommon() {
		INSTANCE = this;

		this.redisManager = new RedisManager();
		this.messageManager = new MessageManager();
	}

	public static RedisManager getRedisManager() {
		return INSTANCE.redisManager;
	}

	public static MessageManager getMessageManager() {
		return INSTANCE.messageManager;
	}
}
