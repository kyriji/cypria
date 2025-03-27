package dev.kyriji.common.cypria.playerdata.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.config.documents.CoreConfig;
import dev.kyriji.common.cypria.config.enums.ConfigType;
import dev.kyriji.common.cypria.config.models.MongoConnection;
import dev.kyriji.common.cypria.database.controllers.DatabaseManager;
import dev.kyriji.common.cypria.database.enums.DatabaseType;
import dev.kyriji.common.cypria.database.records.DatabaseConnection;
import dev.kyriji.common.cypria.playerdata.enums.PlayerDataType;
import dev.kyriji.common.cypria.playerdata.models.PlayerDataDocument;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.eq;

public class PlayerDataManager {

	public PlayerDataManager() {
		CoreConfig config = CypriaCommon.getConfigManager().getConfig(ConfigType.CORE);
		if(config == null) throw new NullPointerException("Core config not found");

		MongoConnection playerDataConnection = config.getPlayerDataConnection();
		DatabaseManager.addDatabase(DatabaseType.PLAYER_DATA, playerDataConnection.getUri(), playerDataConnection.getDatabase());
	}

	private final Map<UUID, List<PlayerDataDocument>> loadedPlayerData = new HashMap<>();
	private final List<UUID> frozenPlayers = new ArrayList<>();

	public CompletableFuture<Void> loadPlayerData(UUID uuid, PlayerDataType type) {
		return CompletableFuture.runAsync(() -> getPlayerData(uuid, type));
	}

	public <T> T getTemporaryPlayerData(UUID uuid, PlayerDataType type) {
		DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.PLAYER_DATA);

		if (loadedPlayerData.containsKey(uuid)) {
			for (PlayerDataDocument document : loadedPlayerData.get(uuid)) {
				if (document.getClass().equals(type.getDocumentClass())) {
					return (T) document;
				}
			}
		}

		@SuppressWarnings("unchecked")
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		MongoCollection<T> collection = connection.database().getCollection(type.getCollectionName(), documentClass);

		T document = collection.find(eq("uuid", uuid.toString())).first();

		if (document == null) {
			try {
				T newInstance = documentClass.getDeclaredConstructor().newInstance();
				if (newInstance instanceof PlayerDataDocument) {
					((PlayerDataDocument) newInstance).setUuid(uuid.toString());
					collection.insertOne(newInstance);
					return newInstance;
				}
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		return document;
	}


	public <T extends PlayerDataDocument> T getPlayerData(UUID uuid, PlayerDataType type) {
		T document = getTemporaryPlayerData(uuid, type);
		if(document != null) {
			loadedPlayerData.putIfAbsent(uuid, new ArrayList<>());
			loadedPlayerData.get(uuid).add(document);
			return document;
		}

		@SuppressWarnings("unchecked")
		Class<T> documentClass = (Class<T>) type.getDocumentClass();

		try {
			PlayerDataDocument newInstance = documentClass.newInstance();
			newInstance.setUuid(uuid.toString());
			loadedPlayerData.putIfAbsent(uuid, new ArrayList<>());
			loadedPlayerData.get(uuid).add(newInstance);
			newInstance.save();
			return (T) newInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void unloadPlayerData(UUID uuid) {
		loadedPlayerData.remove(uuid);
		frozenPlayers.remove(uuid);
	}

	public CompletableFuture<Void> savePlayerData(UUID uuid, PlayerDataType type) {
		return CompletableFuture.runAsync(() -> {
			PlayerDataDocument data = getPlayerData(uuid, type);
			savePlayerData(data, type);
		});
	}

	public <T extends PlayerDataDocument> CompletableFuture<Void> savePlayerData(T document, PlayerDataType type) {
		return CompletableFuture.runAsync(() -> {
			DatabaseConnection connection = DatabaseManager.getDatabase(DatabaseType.PLAYER_DATA);

			MongoCollection<T> collection = connection.database().getCollection(type.getCollectionName(), (Class<T>) type.getDocumentClass());
			collection.replaceOne(eq("uuid", document.getUuid()), document, new ReplaceOptions().upsert(true));
		}).exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});
	}

	public CompletableFuture<Void> freezePlayerData(UUID playerUUID) {
		if(!loadedPlayerData.containsKey(playerUUID)) {
			throw new IllegalStateException("Player data is not loaded for UUID: " + playerUUID);
		}

		List<PlayerDataDocument> currentDataState = new ArrayList<>(loadedPlayerData.get(playerUUID));
		frozenPlayers.add(playerUUID);

		CompletableFuture<Void> unfreezeFuture = new CompletableFuture<>();

		unfreezeFuture.thenRun(() -> {
			loadedPlayerData.put(playerUUID, currentDataState);
			frozenPlayers.remove(playerUUID);
			System.out.println("Player data for UUID " + playerUUID + " has been reverted to its frozen state.");
		});

		return unfreezeFuture;
	}

	public boolean isFrozen(UUID playerUUID) {
		return frozenPlayers.contains(playerUUID);
	}
}