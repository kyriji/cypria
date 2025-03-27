package dev.kyriji.commonmc.cypria.item.models;

import com.google.common.collect.Multimap;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.item.ability.models.ItemAbility;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.item.enums.ItemType;
import dev.kyriji.commonmc.cypria.item.enums.ItemPropertyType;
import dev.kyriji.commonmc.cypria.item.enums.ItemUnbreakableType;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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

import java.util.*;

public abstract class CypriaItem implements Listener {
	private final ItemType itemType;

	private final List<ItemAbility> itemAbilities = new ArrayList<>();

	public CypriaItem(ItemType itemType) {
		this.itemType = itemType;

		Bukkit.getPluginManager().registerEvents(this, CypriaMinecraft.plugin);
	}

	public abstract ItemProperties getStaticProperties();
	public abstract List<String> getLore(ItemProperties properties);

	public void itemAbilities(ItemAbility... itemAbilities) {
		this.itemAbilities.addAll(Arrays.stream(itemAbilities).filter(Objects::nonNull).toList());
	}

	public ItemProperties createFullProperties(ItemProperties staticProperties, ItemProperties dynamicProperties) {
		return staticProperties.clone().set(dynamicProperties);
	}

	public ItemStack createItem() {
		return createItem(ItemProperties.EMPTY);
	}

	public ItemStack createItem(ItemProperties dynamicProperties) {
		ItemProperties properties = createFullProperties(getStaticProperties(), dynamicProperties);

		Material material = properties.get(ItemPropertyType.MATERIAL);
		String displayName = properties.get(ItemPropertyType.DISPLAY_NAME);
		List<TextComponent> lore = getLore(properties).stream()
				.map(line -> Component.text(AUtil.colorize(line))).toList();
		ItemUnbreakableType unbreakableType = properties.get(ItemPropertyType.UNBREAKABLE);
		boolean hasEnchantGlint = properties.getOrDefault(ItemPropertyType.ENCHANT_GLINT, false);
		LinkedHashSet<ItemFlag> itemFlags = properties.getOrDefault(ItemPropertyType.ITEM_FLAGS, new LinkedHashSet<>());
		Multimap<Attribute, AttributeModifier> attributes = properties.get(ItemPropertyType.ATTRIBUTES);
		CustomData data = properties.get(ItemPropertyType.CUSTOM_DATA);

		data.set(CustomProperty.ID, itemType.getID());

		ItemStack itemStack = new ItemStack(material);

		if (hasEnchantGlint) itemStack.addUnsafeEnchantment(Enchantment.LUCK_OF_THE_SEA, 1);

		itemStack.editMeta(itemMeta -> {
			itemMeta.setDisplayName(displayName);
			itemMeta.lore(lore);

			if (unbreakableType != null) {
				itemMeta.setUnbreakable(true);
				if (unbreakableType == ItemUnbreakableType.UNBREAKABLE_HIDDEN) itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}

			if (hasEnchantGlint) itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

			itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));

			itemMeta.setAttributeModifiers(attributes);

			PersistentDataContainer container = itemMeta.getPersistentDataContainer();
			PersistentDataContainer customDataContainer = container.getAdapterContext().newPersistentDataContainer();
			data.write(customDataContainer);
			container.set(CustomData.CONTAINER_KEY, PersistentDataType.TAG_CONTAINER, customDataContainer);
		});

		return itemStack;
	}

	public boolean isThisItem(ItemStack itemStack) {
		return ItemManager.getItem(itemStack) == this;
	}

	public ItemType getItemID() {
		return itemType;
	}

	public List<ItemAbility> getItemAbilities() {
		return itemAbilities;
	}
}
