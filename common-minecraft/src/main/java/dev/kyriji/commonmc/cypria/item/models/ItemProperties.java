package dev.kyriji.commonmc.cypria.item.models;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.kyriji.commonmc.cypria.item.enums.ItemPropertyType;
import dev.kyriji.commonmc.cypria.item.enums.ItemUnbreakableType;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class ItemProperties {
	public static final ItemProperties EMPTY = new ItemProperties();

	private final LinkedHashMap<ItemPropertyType<?>, Object> properties = new LinkedHashMap<>();

	private ItemProperties() {}

	private ItemProperties(ItemProperties properties) {
		this.properties.putAll(properties.properties);
	}

	public CustomData data() {
		return get(ItemPropertyType.CUSTOM_DATA);
	}

	public <T> T get(ItemPropertyType<T> property) {
		return (T) properties.get(property);
	}

	public <T> T getOrDefault(ItemPropertyType<T> property, T defaultValue) {
		return (T) properties.getOrDefault(property, defaultValue);
	}

	public <T> ItemProperties set(ItemPropertyType<T> property, T value) {
		properties.put(property, value);
		return this;
	}

	public <T> ItemProperties setIfAbsent(ItemPropertyType<T> property, T value) {
		properties.putIfAbsent(property, value);
		return this;
	}

	public boolean has(ItemPropertyType<?> property) {
		return properties.containsKey(property);
	}

	public ItemProperties set(ItemProperties properties) {
		this.properties.putAll(properties.properties);
		return this;
	}

	public void remove(ItemPropertyType<?> property) {
		properties.remove(property);
	}

	public ItemProperties clone() {
		return new ItemProperties(this);
	}

	public static ItemProperties fromItemStack(ItemStack itemStack) {
		ItemMeta itemMeta = AUtil.getOrCreateItemMeta(itemStack);
		return new ItemProperties.Builder()
				.material(itemStack.getType())
				.displayName(itemMeta.getDisplayName())
				.unbreakable(ItemUnbreakableType.fromItemStack(itemStack) != null)
				.enchantGlint(itemMeta.hasEnchants())
				.itemFlags(itemMeta.getItemFlags().toArray(new ItemFlag[0]))
				.attributes(itemMeta.getAttributeModifiers())
				.addData(new CustomData(itemMeta.getPersistentDataContainer()))
				.build();
	}

	public static class Builder {
		private final ItemProperties properties = new ItemProperties();

		public Builder() {
			properties.setIfAbsent(ItemPropertyType.CUSTOM_DATA, new CustomData());
		}

		public Builder(ItemProperties properties) {
			this();
			this.properties.set(properties);
		}

		public Builder material(Material material) {
			properties.set(ItemPropertyType.MATERIAL, material);
			return this;
		}

		public Builder displayName(String displayName) {
			properties.set(ItemPropertyType.DISPLAY_NAME, displayName);
			return this;
		}

		public Builder unbreakable(boolean hideUnbreakable) {
			properties.set(ItemPropertyType.UNBREAKABLE, hideUnbreakable ?
					ItemUnbreakableType.UNBREAKABLE_HIDDEN : ItemUnbreakableType.UNBREAKABLE_VISIBLE);
			return this;
		}

		public Builder enchantGlint(boolean enchantGlint) {
			properties.set(ItemPropertyType.ENCHANT_GLINT, enchantGlint);
			return this;
		}

		public Builder itemFlags(ItemFlag... itemFlags) {
			properties.set(ItemPropertyType.ITEM_FLAGS, new LinkedHashSet<>(Arrays.asList(itemFlags)));
			return this;
		}

		public Builder attribute(Attribute attribute, AttributeModifier... modifiers) {
			Multimap<Attribute, AttributeModifier> attributeModifiers =
					properties.getOrDefault(ItemPropertyType.ATTRIBUTES, ArrayListMultimap.create());
			attributeModifiers.putAll(attribute, Arrays.asList(modifiers));
			properties.set(ItemPropertyType.ATTRIBUTES, attributeModifiers);
			return this;
		}

		public Builder attributes(Multimap<Attribute, AttributeModifier> attributeModifiers) {
			properties.set(ItemPropertyType.ATTRIBUTES, attributeModifiers);
			return this;
		}

		public Builder addData(CustomData data) {
			CustomData customData = properties.get(ItemPropertyType.CUSTOM_DATA);
			customData.set(data);
			return this;
		}

		public ItemProperties build() {
			return properties;
		}
	}
}
