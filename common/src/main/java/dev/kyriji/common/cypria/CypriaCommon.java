package dev.kyriji.common.cypria;

import dev.kyriji.common.cypria.controllers.MessageManager;
import dev.kyriji.common.cypria.controllers.RedisManager;

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
