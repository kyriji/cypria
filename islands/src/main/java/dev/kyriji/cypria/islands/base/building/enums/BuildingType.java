package dev.kyriji.cypria.islands.base.building.enums;

public enum BuildingType {
	TEST_BUILDING("test_building"),
	;

	private final String id;

	BuildingType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
