package dev.kyriji.common.config.documents;

import dev.kyriji.common.config.models.ConfigDocument;
import dev.kyriji.common.config.models.MongoConnection;
import dev.kyriji.common.config.models.RedisConnection;

public class CoreConfig extends ConfigDocument {

	MongoConnection playerDataConnection = new MongoConnection();
	RedisConnection redisConnection = new RedisConnection();

	public MongoConnection getPlayerDataConnection() {
		return playerDataConnection;
	}

	public void setPlayerDataConnection(MongoConnection playerDataConnection) {
		this.playerDataConnection = playerDataConnection;
	}

	public RedisConnection getRedisConnection() {
		return redisConnection;
	}

	public void setRedisConnection(RedisConnection redisConnection) {
		this.redisConnection = redisConnection;
	}
}
