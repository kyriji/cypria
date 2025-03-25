package dev.kyriji.common.cypria.controllers;

import com.google.gson.reflect.TypeToken;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.models.CypriaInstance;
import dev.kyriji.common.cypria.config.documents.CoreConfig;
import dev.kyriji.common.cypria.config.enums.ConfigType;
import dev.kyriji.common.cypria.enums.Deployment;
import redis.clients.jedis.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static dev.kyriji.common.cypria.CypriaCommon.gson;

public class RedisManager {

	public static final String CHANNEL_NAME = "cypria";
	private static final int MAX_CONNECTIONS = 25;

	private final JedisPool pool;
	private final List<JedisPubSub> activeListeners = new CopyOnWriteArrayList<>();


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

		activeListeners.add(pubSub);

		new Thread(() -> {
			try (Jedis jedisSubscriber = pool.getResource()) {
				jedisSubscriber.subscribe(pubSub, CHANNEL_NAME);
			} finally {
				activeListeners.remove(pubSub);
			}
		}).start();
	}

	public void unregisterAllListeners() {
		for(JedisPubSub pubSub : activeListeners) {
			pubSub.unsubscribe();
		}
		activeListeners.clear();
	}

	public void registerInstance(CypriaInstance instance) {
		String address = instance.getAddress();
		Deployment deployment = instance.getDeployment();

		String key = "cypria:instance:" + deployment.name() + ":" + address;

		try (Jedis jedis = pool.getResource()) {
			jedis.hset(key, "address", address);
			jedis.hset(key, "deployment", deployment.name());
			jedis.hset(key, "players", gson.toJson(instance.getPlayers()));
		}
	}

	public void removeInstance(CypriaInstance instance) {
		String address = instance.getAddress();
		Deployment deployment = instance.getDeployment();

		String key = "cypria:instance:" + deployment.name() + ":" + address;

		try (Jedis jedis = pool.getResource()) {
			jedis.del(key);
		}
	}

	public void updateInstance(CypriaInstance instance) {
		removeInstance(instance);
		registerInstance(instance);
	}

	public List<CypriaInstance> getInstances() {
		RedisManager redisManager = CypriaCommon.getRedisManager();
		String pattern = "cypria:instances:*:*";

		List<CypriaInstance> resultList = new ArrayList<>();
		Type playerMapType = new TypeToken<Map<UUID, String>>(){}.getType();

		try(Jedis jedis = redisManager.getConnection()) {
			String cursor = "0";
			do {
				ScanResult<String> scanResult = jedis.scan(cursor, new ScanParams().match(pattern));
				cursor = scanResult.getCursor();

				for(String key : scanResult.getResult()) {
					Map<String, String> hashData = jedis.hgetAll(key);

					String address = hashData.get("address");
					Deployment deployment = Deployment.valueOf(hashData.get("deployment"));

					CypriaInstance instance = new CypriaInstance(address, deployment);

					String playersStr = hashData.get("players");
					Map<UUID, String> players = playersStr != null ?
							gson.fromJson(playersStr, playerMapType) : new HashMap<>();

					instance.setPlayers(players);
					resultList.add(instance);
				}
			} while (!cursor.equals("0"));
		}

		return resultList;
	}

}
