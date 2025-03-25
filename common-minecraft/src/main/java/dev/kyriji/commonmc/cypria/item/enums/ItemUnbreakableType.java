package dev.kyriji.commonmc.cypria.item.enums;

import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ItemUnbreakableType {
	UNBREAKABLE_VISIBLE,
	UNBREAKABLE_HIDDEN,
	;

	public static ItemUnbreakableType fromItemStack(ItemStack itemStack) {
		if (AUtil.isNullOrAir(itemStack) || !itemStack.hasItemMeta()) return null;
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (!itemMeta.isUnbreakable()) return null;
		return itemMeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE) ? UNBREAKABLE_HIDDEN : UNBREAKABLE_VISIBLE;
	}
}
