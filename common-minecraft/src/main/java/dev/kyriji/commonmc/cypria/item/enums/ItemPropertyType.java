package dev.kyriji.commonmc.cypria.item.enums;

import com.google.common.collect.Multimap;
import dev.kyriji.commonmc.cypria.item.models.CustomData;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;

import java.util.LinkedHashSet;

// don't be fooled, the type T is very important. NEVER DELETE.
public class ItemPropertyType<T> {
	public static final ItemPropertyType<Material> MATERIAL = new ItemPropertyType<>();
	public static final ItemPropertyType<String> DISPLAY_NAME = new ItemPropertyType<>();
	public static final ItemPropertyType<ItemUnbreakableType> UNBREAKABLE = new ItemPropertyType<>();
	public static final ItemPropertyType<Boolean> ENCHANT_GLINT = new ItemPropertyType<>();

	public static final ItemPropertyType<LinkedHashSet<ItemFlag>> ITEM_FLAGS = new ItemPropertyType<>();
	public static final ItemPropertyType<Multimap<Attribute, AttributeModifier>> ATTRIBUTES = new ItemPropertyType<>();

	public static final ItemPropertyType<CustomData> CUSTOM_DATA = new ItemPropertyType<>();
}
