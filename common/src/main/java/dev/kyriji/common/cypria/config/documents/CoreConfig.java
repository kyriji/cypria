package dev.kyriji.common.cypria.config.documents;

import dev.kyriji.common.cypria.config.models.ConfigDocument;
import dev.kyriji.common.cypria.config.models.MongoConnection;
import dev.kyriji.common.cypria.config.models.RedisConnection;
import dev.kyriji.common.cypria.config.models.SqlConnection;

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
