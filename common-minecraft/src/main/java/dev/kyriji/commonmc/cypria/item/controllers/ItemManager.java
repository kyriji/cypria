package dev.kyriji.commonmc.cypria.item.controllers;

import dev.kyriji.commonmc.cypria.item.models.CypriaItem;
import dev.kyriji.commonmc.cypria.misc.ReflectionUtils;

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
}
