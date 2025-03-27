package dev.kyriji.commonmc.cypria.item.ability.enums;

public enum ItemAbilityType {
	TEST_ABILITY("test"),
	;

	private final String id;

	ItemAbilityType(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}
}
