package dev.kyriji.cypria.islands.base.building.enums;

public enum BuildingSchematic {
	TEST_SCHEMATIC("test"),
	;

	private final String schematicName;

	BuildingSchematic(String schematicName) {
		this.schematicName = schematicName;
	}

	public String getFileName() {
		return schematicName;
	}
}
