package dev.kyriji.commonmc.cypria.item.ability.controllers;

import dev.kyriji.commonmc.cypria.item.ability.enums.ItemAbilityType;
import dev.kyriji.commonmc.cypria.item.ability.models.ItemAbility;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.item.models.CypriaItem;
import dev.kyriji.commonmc.cypria.misc.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemAbilityManager implements Listener {
	public static List<ItemAbility> itemAbilities = new ArrayList<>();

	public ItemAbilityManager() {
		ReflectionUtils.initPackage("dev.kyriji.commonmc.cypria.item.ability.abilities", ItemAbility.class)
				.forEach(ItemAbilityManager::registerItem);
	}

	public static void registerItem(ItemAbility itemAbility) {
		itemAbilities.add(itemAbility);
	}

	public static <T extends ItemAbility> T getAbility(Class<T> clazz) {
		for (ItemAbility itemAbility : itemAbilities) if (clazz.isInstance(itemAbility)) return clazz.cast(itemAbility);
		throw new RuntimeException();
	}

	public static List<ItemAbility> getAbilities(Player player) {
		List<ItemAbility> abilities = new ArrayList<>();
		for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
			if (equipmentSlot == EquipmentSlot.BODY) continue;
			ItemStack itemStack = player.getInventory().getItem(equipmentSlot);
			abilities.addAll(getAbilities(itemStack).stream().filter(
					ability -> ability.getEquipmentSlotGroup().test(equipmentSlot)).toList());
		}
		return abilities;
	}

	public static List<ItemAbility> getAbilities(ItemStack itemStack) {
		CypriaItem cypriaItem = ItemManager.getItem(itemStack);
		if (cypriaItem == null) return new ArrayList<>();
		return cypriaItem.getItemAbilities();
	}

	public static ItemAbility getAbility(ItemAbilityType abilityID) {
		for (ItemAbility ability : itemAbilities) if (ability.getAbilityType() == abilityID) return ability;
		return null;
	}
}
