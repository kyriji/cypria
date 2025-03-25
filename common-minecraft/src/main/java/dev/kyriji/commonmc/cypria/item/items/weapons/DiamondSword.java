package dev.kyriji.commonmc.cypria.item.items.weapons;

import dev.kyriji.commonmc.cypria.item.enums.ItemID;
import dev.kyriji.commonmc.cypria.item.enums.ItemNKey;
import dev.kyriji.commonmc.cypria.item.models.CypriaItem;
import dev.kyriji.commonmc.cypria.item.models.ItemProperties;
import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataType;

public class DiamondSword extends CypriaItem {
	public DiamondSword() {
		super(ItemID.DIAMOND_SWORD);
	}

	@Override
	public ItemProperties getStaticProperties() {
		return new ItemProperties.Builder()
				.material(Material.DIAMOND_SWORD)
				.displayName("test")
				.enchantGlint(true)
				.customData(container -> {
					container.set(ItemNKey.DEMO, PersistentDataType.BOOLEAN, true);
				})
				.build();
	}
}
