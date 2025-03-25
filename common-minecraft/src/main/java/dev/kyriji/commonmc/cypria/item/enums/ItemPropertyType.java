package dev.kyriji.commonmc.cypria.item.enums;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

// don't be fooled, the type T is very important. NEVER DELETE.
public class ItemPropertyType<T> {
	public static final ItemPropertyType<Material> MATERIAL = new ItemPropertyType<>();
	public static final ItemPropertyType<String> DISPLAY_NAME = new ItemPropertyType<>();
	public static final ItemPropertyType<ItemUnbreakableType> UNBREAKABLE = new ItemPropertyType<>();
	public static final ItemPropertyType<Boolean> ENCHANT_GLINT = new ItemPropertyType<>();

	public static final ItemPropertyType<LinkedHashSet<ItemFlag>> ITEM_FLAGS = new ItemPropertyType<>();
	public static final ItemPropertyType<Multimap<Attribute, AttributeModifier>> ATTRIBUTES = new ItemPropertyType<>();

	public static final ItemPropertyType<List<Consumer<PersistentDataContainer>>> CUSTOM_DATA = new ItemPropertyType<>();
}
