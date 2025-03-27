package dev.kyriji.commonmc.cypria.item.items.weapons;

import dev.kyriji.commonmc.cypria.item.ability.abilities.TestAbility;
import dev.kyriji.commonmc.cypria.item.ability.controllers.ItemAbilityManager;
import dev.kyriji.commonmc.cypria.item.ability.models.ItemAbility;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.item.enums.ItemType;
import dev.kyriji.commonmc.cypria.item.models.CustomData;
import dev.kyriji.commonmc.cypria.item.models.CustomProperty;
import dev.kyriji.commonmc.cypria.item.models.CypriaWeapon;
import dev.kyriji.commonmc.cypria.item.models.ItemProperties;
import org.bukkit.Material;

import java.util.List;

public class DiamondSword extends CypriaWeapon {
	public DiamondSword() {
		super(ItemType.DIAMOND_SWORD);
	}

	@Override
	public ItemProperties getStaticProperties() {
		return new ItemProperties.Builder()
				.material(Material.DIAMOND_SWORD)
				.displayName("test")
				.enchantGlint(true)
				.addData(new CustomData()
						.set(CustomProperty.TEST, true)
				)
				.build();
	}

	@Override
	public List<String> getLore(ItemProperties properties) {
		return List.of(
				"&7This is test &alore",
				properties.data().get(CustomProperty.TEST) + ""
		);
	}

	@Override
	public ItemAbility getLeftAbility() {
		return ItemAbilityManager.getAbility(TestAbility.class);
	}

	@Override
	public ItemAbility getRightAbility() {
		return null;
	}
}
