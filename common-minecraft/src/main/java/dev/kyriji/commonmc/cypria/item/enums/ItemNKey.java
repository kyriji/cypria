package dev.kyriji.commonmc.cypria.item.enums;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import org.bukkit.NamespacedKey;

public class ItemNKey {
	public static final NamespacedKey CUSTOM_DATA = key("custom_data");

	public static final NamespacedKey ID = key("id");

	public static final NamespacedKey DEMO = key("demo");

	private static NamespacedKey key(String key) {
		return new NamespacedKey(CypriaMinecraft.plugin, key);
	}
}
