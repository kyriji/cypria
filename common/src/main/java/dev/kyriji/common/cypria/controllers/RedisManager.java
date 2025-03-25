package dev.kyriji.common.cypria.controllers;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.config.documents.CoreConfig;
import dev.kyriji.common.cypria.config.enums.ConfigType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Consumer;

public class RedisManager {

	public static final String CHANNEL_NAME = "cypria";
	private static final int MAX_CONNECTIONS = 25;

	private final JedisPool pool;

	public RedisManager() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(MAX_CONNECTIONS);
		poolConfig.setMaxIdle(MAX_CONNECTIONS / 4);
		poolConfig.setMinIdle(1);

		CoreConfig coreConfig = CypriaCommon.getConfigManager().getConfig(ConfigType.CORE);

		this.pool = new JedisPool(poolConfig,
				coreConfig.getRedisConnection().getHost(),
				coreConfig.getRedisConnection().getPort()
		);
	}

	public Jedis getConnection() {
		return pool.getResource();
	}

	public void addListener(Consumer<String> listener) {
		JedisPubSub pubSub = new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				listener.accept(message);
			}
		};

		new Thread(() -> {
			try (Jedis jedisSubscriber = pool.getResource()) {
				jedisSubscriber.subscribe(pubSub, CHANNEL_NAME);
			}
		}).start();
	}

}
