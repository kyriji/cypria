package dev.kyriji.common.playerdata.enums;

import dev.kyriji.common.playerdata.models.PlayerDataDocument;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataType {
	private static final Map<Class<? extends PlayerDataDocument>, PlayerDataType> registry = new HashMap<>();

	private final String collectionName;
	private final Class<? extends PlayerDataDocument> documentClass;

	public PlayerDataType(String collectionName, Class<? extends PlayerDataDocument> documentClass) {
		this.collectionName = collectionName;
		this.documentClass = documentClass;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public Class<? extends PlayerDataDocument> getDocumentClass() {
		return documentClass;
	}

	public static void register(PlayerDataType type) {
		registry.put(type.getDocumentClass(), type);
	}

	public static PlayerDataType fromClass(Class<? extends PlayerDataDocument> clazz) {
		return registry.get(clazz);
	}

	public static PlayerDataType get(Class<? extends PlayerDataDocument> clazz) {
		PlayerDataType type = registry.get(clazz);
		if (type == null) {
			throw new IllegalArgumentException("No PlayerDataType registered for class: " + clazz.getName());
		}
		return type;
	}
}
