package dev.kyriji.commonmc.cypria.item.enums;

public enum ItemID {
// 	General
	DIAMOND_SWORD("diamond_sword"),

// 	Weapons
// 	Gadgets
// 	Trinkets
// 	Armor
	DIAMOND_ARMOR("diamond_armor"),
	;

	private final String id;

	ItemID(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public static ItemID fromString(String identifier) {
		for (ItemID itemID : ItemID.values()) if (itemID.getID().equalsIgnoreCase(identifier)) return itemID;
		return null;
	}
}
