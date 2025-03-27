package dev.kyriji.commonmc.cypria.item.controllers;

import dev.kyriji.commonmc.cypria.item.enums.ItemType;
import dev.kyriji.commonmc.cypria.item.models.CustomData;
import dev.kyriji.commonmc.cypria.item.models.CustomProperty;
import dev.kyriji.commonmc.cypria.item.models.CypriaItem;
import dev.kyriji.commonmc.cypria.item.models.ItemProperties;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import dev.kyriji.commonmc.cypria.misc.ReflectionUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
	public static List<CypriaItem> itemList = new ArrayList<>();
	public static ItemProperties defaultProperties;

	public static void init() {
		defaultProperties = new ItemProperties.Builder()
				.unbreakable(true)
				.build();

		ReflectionUtils.initPackage("dev.kyriji.commonmc.cypria.item.items", CypriaItem.class)
				.forEach(ItemManager::registerItem);
	}

	public static void registerItem(CypriaItem item) {
		itemList.add(item);
	}

	public static <T extends CypriaItem> T getItem(Class<T> clazz) {
		for (CypriaItem item : itemList) if (clazz.isInstance(item)) return clazz.cast(item);
		throw new RuntimeException();
	}

	// Normally you would want to do this by using ItemProperties.fromItemStack, but this should be faster
	public static CypriaItem getItem(ItemStack itemStack) {
		if (AUtil.isNullOrAir(itemStack)) return null;
		PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
		PersistentDataContainer customData = container.get(CustomData.CONTAINER_KEY, PersistentDataType.TAG_CONTAINER);
		if (customData == null) return null;
		ItemType itemType = ItemType.fromString(customData.get(CustomProperty.ID.getNamespacedKey(), PersistentDataType.STRING));
		return getItem(itemType);
	}

	public static CypriaItem getItem(ItemType itemType) {
		for (CypriaItem item : itemList) if (item.getItemID() == itemType) return item;
		return null;
	}
}
