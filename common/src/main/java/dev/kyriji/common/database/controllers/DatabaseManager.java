package dev.kyriji.common.database.controllers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.kyriji.common.database.enums.DatabaseType;
import dev.kyriji.common.database.records.DatabaseConnection;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseManager {
	private static final Map<DatabaseType, DatabaseConnection> databaseConnections = new HashMap<>();

	public static void addDatabase(DatabaseType type, String connectionString, String defaultDatabase) {
		if(connectionString == null || defaultDatabase == null) throw new NullPointerException("Connection string or default database cannot be null");

		System.out.println("Adding database: " + type + " | " + connectionString + " | " + defaultDatabase);

		CodecRegistry pojoCodecRegistry = fromRegistries(
				getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder()
						.automatic(true)
						.build()));

		MongoClientSettings settings = MongoClientSettings.builder()
				.codecRegistry(pojoCodecRegistry)
				.applyConnectionString(new ConnectionString(connectionString))
				.uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
				.build();

		MongoClient client = MongoClients.create(settings);
		MongoDatabase database = client.getDatabase(defaultDatabase).withCodecRegistry(pojoCodecRegistry);

		databaseConnections.put(type, new DatabaseConnection(client, database));
	}

	public static DatabaseConnection getDatabase(DatabaseType type) {
		return databaseConnections.get(type);
	}
}
