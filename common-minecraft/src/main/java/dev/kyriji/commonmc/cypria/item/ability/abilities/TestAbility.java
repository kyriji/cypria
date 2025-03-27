package dev.kyriji.commonmc.cypria.item.ability.abilities;

import dev.kyriji.commonmc.cypria.item.ability.controllers.ItemAbilityManager;
import dev.kyriji.commonmc.cypria.item.ability.enums.ItemAbilityType;
import dev.kyriji.commonmc.cypria.item.ability.models.ItemAbility;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlotGroup;

public class TestAbility extends ItemAbility {
	public TestAbility() {
		super(ItemAbilityType.TEST_ABILITY);
	}

	@Override
	public EquipmentSlotGroup getEquipmentSlotGroup() {
		return EquipmentSlotGroup.HAND;
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		if (!event.isSneaking()) return;
		Player player = event.getPlayer();
		if (!hasThisAbility(player)) return;
		AUtil.debug(player, "test ability");
	}
}
