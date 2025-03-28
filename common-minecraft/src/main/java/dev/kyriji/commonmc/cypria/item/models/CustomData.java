package dev.kyriji.commonmc.cypria.item.models;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.Map;

public class CustomData {
	public static final NamespacedKey CONTAINER_KEY = new NamespacedKey(CypriaMinecraft.plugin(), "custom_data");

	public Map<CustomProperty<?>, Object> customDataMap = new HashMap<>();

	public CustomData() {}

	public CustomData(PersistentDataContainer container) {
		read(container);
	}

	public <T> T get(CustomProperty<T> entry) {
		return (T) customDataMap.get(entry);
	}

	public <T> CustomData set(CustomProperty<T> entry, T value) {
		customDataMap.put(entry, value);
		return this;
	}

	public void set(CustomData data) {
		customDataMap.putAll(data.customDataMap);
	}

	public void read(PersistentDataContainer container) {
		if (container == null) return;
		for (CustomProperty entry : CustomProperty.values()) {
			if (container.has(entry.getNamespacedKey(), entry.getDataType()))
				customDataMap.put(entry, container.get(entry.getNamespacedKey(), entry.getDataType()));
		}
	}

	public void write(PersistentDataContainer container) {
		if (container == null) return;
		for (Map.Entry<CustomProperty<?>, Object> entry : customDataMap.entrySet()) {
			CustomProperty dataEntry = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				container.remove(dataEntry.getNamespacedKey());
			} else {
				container.set(dataEntry.getNamespacedKey(), dataEntry.getDataType(), entry.getValue());
			}
		}
	}
}
