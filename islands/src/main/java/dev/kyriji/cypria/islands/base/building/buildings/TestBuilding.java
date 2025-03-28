package dev.kyriji.cypria.islands.base.building.buildings;

import dev.kyriji.cypria.islands.base.building.enums.BuildingSchematic;
import dev.kyriji.cypria.islands.base.building.enums.BuildingType;
import dev.kyriji.cypria.islands.base.building.models.Building;
import dev.kyriji.cypria.islands.base.building.models.BuildingHealth;

public class TestBuilding extends Building {
	public TestBuilding() {
		super(BuildingType.TEST_BUILDING);
	}

	@Override
	public BuildingType getType() {
		return BuildingType.TEST_BUILDING;
	}

	@Override
	public BuildingSchematic getSchematic() {
		return BuildingSchematic.TEST_SCHEMATIC;
	}

	@Override
	public BuildingHealth getHealth() {
		return new BuildingHealth(12);
	}
}
