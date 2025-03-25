package dev.kyriji.common.cypria.database.records;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public record DatabaseConnection(MongoClient mongoClient, MongoDatabase database) { }

