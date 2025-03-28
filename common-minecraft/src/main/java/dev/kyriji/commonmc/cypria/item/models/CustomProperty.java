package dev.kyriji.commonmc.cypria.item.models;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomProperty<T> {
	private static final List<CustomProperty<?>> VALUES = new ArrayList<>();

	public static final CustomProperty<String> ID = prop("id", PersistentDataType.STRING);

	// abilities
	public static final CustomProperty<String> ABILITY_LEFT = prop("ability_left", PersistentDataType.STRING);
	public static final CustomProperty<String> ABILITY_RIGHT = prop("ability_right", PersistentDataType.STRING);
	public static final CustomProperty<String> ABILITY_SINGLE = prop("ability", PersistentDataType.STRING);

	public static final CustomProperty<Boolean> TEST = prop("test", PersistentDataType.BOOLEAN);

	private final NamespacedKey key;
	private final PersistentDataType<?, T> dataType;

	public CustomProperty(NamespacedKey key, PersistentDataType<?, T> dataType) {
		this.key = key;
		this.dataType = dataType;
		VALUES.add(this);
	}

	public NamespacedKey getNamespacedKey() {
		return key;
	}

	public PersistentDataType<?, T> getDataType() {
		return dataType;
	}

	public static <T> CustomProperty<T> prop(String key, PersistentDataType<?, T> dataType) {
		return new CustomProperty<>(new NamespacedKey(CypriaMinecraft.plugin(), key), dataType);
	}

	public static List<CustomProperty<?>> values() {
		return VALUES;
	}
}