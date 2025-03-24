package dev.kyriji.common.cypria.messaging;

import com.google.gson.Gson;
import dev.kyriji.common.cypria.controllers.MessageManager;
import dev.kyriji.common.cypria.controllers.RedisManager;
import dev.kyriji.common.cypria.enums.RunContext;

public class CypriaCommon {
	private static CypriaCommon INSTANCE;
	public static Gson gson = new Gson();

	private final RedisManager redisManager;
	private final MessageManager messageManager;
	private final RunContext runContext;

	public CypriaCommon(RunContext runContext) {
		INSTANCE = this;

		this.redisManager = new RedisManager();
		this.messageManager = new MessageManager();
		this.runContext = runContext;
	}

	public static RedisManager getRedisManager() {
		return INSTANCE.redisManager;
	}

	public static MessageManager getMessageManager() {
		return INSTANCE.messageManager;
	}

	public static RunContext getRunContext() {
		return INSTANCE.runContext;
	}
}
