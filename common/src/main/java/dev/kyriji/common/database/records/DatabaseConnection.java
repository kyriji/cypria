package dev.kyriji.common.database.records;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public record DatabaseConnection(MongoClient mongoClient, MongoDatabase database) { }
