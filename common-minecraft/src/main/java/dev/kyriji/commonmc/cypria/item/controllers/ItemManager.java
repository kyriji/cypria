package dev.kyriji.commonmc.cypria.item.controllers;

import dev.kyriji.commonmc.cypria.item.enums.ItemID;
import dev.kyriji.commonmc.cypria.item.models.CypriaItem;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import dev.kyriji.commonmc.cypria.misc.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
	public static List<CypriaItem> itemList = new ArrayList<>();

	public static void init() {
		ReflectionUtils.initPackage("dev.kyriji.commonmc.cypria.item.items", CypriaItem.class)
				.forEach(ItemManager::registerItem);
	}

	public static void registerItem(CypriaItem item) {
		itemList.add(item);
	}

	public static <T extends CypriaItem> T getItem(Class<T> itemClass) {
		for (CypriaItem item : itemList) if (itemClass.isInstance(item)) return itemClass.cast(item);
		throw new RuntimeException();
	}

	public static CypriaItem getItem(ItemStack itemStack) {
		if (AUtil.isNullOrAir(itemStack)) return null;
		return null;
	}

	public static CypriaItem getItem(ItemID itemID) {
		for (CypriaItem item : itemList) if (item.itemID == itemID) return item;
		return null;
	}
}
