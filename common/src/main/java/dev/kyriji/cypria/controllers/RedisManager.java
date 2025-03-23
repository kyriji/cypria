package dev.kyriji.cypria.controllers;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.time.Duration;
import java.util.function.Consumer;

public class RedisManager {

	public static final String CHANNEL_NAME = "cypria";
	private static final int MAX_CONNECTIONS = 25;
	private static final String REDIS_HOST = "redis-service";

	private JedisPool pool;

	public RedisManager() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(MAX_CONNECTIONS);
		poolConfig.setMaxIdle(MAX_CONNECTIONS / 4);
		poolConfig.setMinIdle(1);

		this.pool = new JedisPool(poolConfig);
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
