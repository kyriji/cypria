package dev.kyriji.commonmc.cypria.item.enums;

public enum ItemID {
// 	General
	DIAMOND_SWORD,

// 	Weapons
// 	Gadgets
// 	Trinkets
// 	Armor
	DIAMOND_ARMOR,
	;

	public static ItemID fromString(String string) {
		for (ItemID itemID : ItemID.values()) if (itemID.name().equalsIgnoreCase(string)) return itemID;
		return null;
	}
}
