package dev.kyriji.commonmc.cypria.item.models;

import com.google.common.collect.Multimap;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.item.enums.ItemID;
import dev.kyriji.commonmc.cypria.item.enums.ItemNKey;
import dev.kyriji.commonmc.cypria.item.enums.ItemPropertyType;
import dev.kyriji.commonmc.cypria.item.enums.ItemUnbreakableType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

public abstract class CypriaItem implements Listener {
	private final ItemID itemID;

	public CypriaItem(ItemID itemID) {
		this.itemID = itemID;

		Bukkit.getPluginManager().registerEvents(this, CypriaMinecraft.plugin);
	}

	public abstract ItemProperties getStaticProperties();

	public ItemStack createItem() {
		return createItem(ItemProperties.EMPTY);
	}

	public ItemStack createItem(ItemProperties dynamicProperties) {
		ItemProperties staticProperties = getStaticProperties();
		ItemProperties properties = staticProperties.clone().set(dynamicProperties);

		Material material = properties.get(ItemPropertyType.MATERIAL);
		String displayName = properties.get(ItemPropertyType.DISPLAY_NAME);
		ItemUnbreakableType unbreakableType = properties.get(ItemPropertyType.UNBREAKABLE);
		boolean hasEnchantGlint = properties.get(ItemPropertyType.ENCHANT_GLINT);
		LinkedHashSet<ItemFlag> itemFlags = properties.getOrDefault(ItemPropertyType.ITEM_FLAGS, new LinkedHashSet<>());
		Multimap<Attribute, AttributeModifier> attributes = properties.get(ItemPropertyType.ATTRIBUTES);
		List<Consumer<PersistentDataContainer>> customDataSetters = properties.get(ItemPropertyType.CUSTOM_DATA);

		ItemStack itemStack = new ItemStack(material);

		if (hasEnchantGlint) itemStack.addUnsafeEnchantment(Enchantment.LUCK_OF_THE_SEA, 1);

		itemStack.editMeta(itemMeta -> {
			itemMeta.setDisplayName(displayName);

			if (unbreakableType != null) {
				itemMeta.setUnbreakable(true);
				if (unbreakableType == ItemUnbreakableType.UNBREAKABLE_HIDDEN) itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}

			if (hasEnchantGlint) itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

			itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));

			itemMeta.setAttributeModifiers(attributes);

			PersistentDataContainer container = itemMeta.getPersistentDataContainer();
			PersistentDataContainer customDataContainer = container.getAdapterContext().newPersistentDataContainer();

			customDataContainer.set(ItemNKey.ID, PersistentDataType.STRING, itemID.getID());

			customDataSetters.forEach(setter -> setter.accept(customDataContainer));

			container.set(ItemNKey.CUSTOM_DATA, PersistentDataType.TAG_CONTAINER, customDataContainer);
		});

		return itemStack;
	}

	public boolean isThisItem(ItemStack itemStack) {
		return ItemManager.getItem(itemStack) == this;
	}

	public ItemID getItemID() {
		return itemID;
	}
}
