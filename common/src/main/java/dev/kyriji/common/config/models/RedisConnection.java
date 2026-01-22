package dev.kyriji.common.config.models;

public class RedisConnection {

	private String host = "redis-service";
	private int port = 6379;

	public RedisConnection() {

	}

	public RedisConnection(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
