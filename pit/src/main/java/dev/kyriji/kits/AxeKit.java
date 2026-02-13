package dev.kyriji.kits;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import dev.kyriji.objects.Kit;

import java.util.List;

public class AxeKit extends Kit {
	public AxeKit() {
		super("Axe");
	}

	@Override
	public KitArmor getArmor() {
		ItemStack helmet = new ItemStack("Armor_Iron_Head");
		ItemStack chestplate = new ItemStack("Armor_Iron_Chest");
		ItemStack gloves = new ItemStack("Armor_Iron_Hands");
		ItemStack leggings = new ItemStack("Armor_Iron_Legs");

		return new KitArmor(helmet, chestplate, gloves, leggings);
	}

	@Override
	public KitUtility getUtility() {
		return new KitUtility(new ItemStack("Weapon_Shield_Iron"));
	}

	@Override
	public List<ItemStack> getHotbar() {
		return List.of(
				new ItemStack("Weapon_Axe_Iron"),
				new ItemStack("C_Sprint_Drain")
		);
	}

	@Override
	public List<ItemStack> getStorage() {
		return List.of();
	}
}
