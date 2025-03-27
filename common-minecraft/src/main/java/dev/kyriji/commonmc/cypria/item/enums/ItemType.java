package dev.kyriji.commonmc.cypria.item.enums;

public enum ItemType {
// 	General
	DIAMOND_SWORD("diamond_sword"),

// 	Weapons
// 	Gadgets
// 	Trinkets
// 	Armor
	DIAMOND_ARMOR("diamond_armor"),
	;

	private final String id;

	ItemType(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public static ItemType fromString(String identifier) {
		for (ItemType itemType : ItemType.values()) if (itemType.getID().equalsIgnoreCase(identifier)) return itemType;
		return null;
	}
}
