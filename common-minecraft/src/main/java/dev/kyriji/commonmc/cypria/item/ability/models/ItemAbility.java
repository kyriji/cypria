package dev.kyriji.commonmc.cypria.item.ability.models;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.item.ability.controllers.ItemAbilityManager;
import dev.kyriji.commonmc.cypria.item.ability.enums.ItemAbilityType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

public abstract class ItemAbility implements Listener {
	private final ItemAbilityType abilityType;

	public ItemAbility(ItemAbilityType abilityType) {
		this.abilityType = abilityType;

		Bukkit.getPluginManager().registerEvents(this, CypriaMinecraft.plugin());
	}

	public abstract EquipmentSlotGroup getEquipmentSlotGroup();

	public boolean hasThisAbility(Player player) {
		return ItemAbilityManager.getAbilities(player).contains(this);
	}

	public boolean hasThisAbility(ItemStack itemStack) {
		return ItemAbilityManager.getAbilities(itemStack).contains(this);
	}

	public ItemAbilityType getAbilityType() {
		return abilityType;
	}
}
