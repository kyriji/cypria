package dev.kyriji.common.config.models;

public class MongoConnection {
	private String uri = "EXAMPLE_URI";
	private String database = "EXAMPLE_DATABASE";

	public MongoConnection() {
	}

	public MongoConnection(String uri, String database) {
		this.uri = uri;
		this.database = database;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}
