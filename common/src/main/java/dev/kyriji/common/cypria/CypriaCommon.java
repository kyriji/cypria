package dev.kyriji.common.cypria;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.kyriji.common.cypria.config.controllers.ConfigManager;
import dev.kyriji.common.cypria.controllers.RedisManager;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.common.cypria.messaging.controllers.MessageManager;
import dev.kyriji.common.cypria.playerdata.controllers.PlayerDataManager;

public class CypriaCommon {
	private static CypriaCommon INSTANCE;
	private final Deployment deployment;

	public static Gson gson = new Gson();

	private final ConfigManager configManager;
	private final PlayerDataManager playerDataManager;
	private final RedisManager redisManager;
	private final MessageManager messageManager;

	public CypriaCommon(JsonObject localConfig, Deployment deployment) {
		INSTANCE = this;
		this.deployment = deployment;

		this.configManager = new ConfigManager(localConfig);
		this.playerDataManager = new PlayerDataManager();
		this.redisManager = new RedisManager();
		this.messageManager = new MessageManager();
	}

	public void shutdown() {
		this.redisManager.unregisterAllListeners();
	}

	public static ConfigManager getConfigManager() {
		return INSTANCE.configManager;
	}

	public static PlayerDataManager getPlayerDataManager() {
		return INSTANCE.playerDataManager;
	}

	public static RedisManager getRedisManager() {
		return INSTANCE.redisManager;
	}

	public static MessageManager getMessageManager() {
		return INSTANCE.messageManager;
	}

	public static Deployment getDeployment() {
		return INSTANCE.deployment;
	}
}
