package dev.kyriji.cypria.islands.base.building.enums;

public enum BuildingState {
	ACTIVE,
	INACTIVE,
	DISABLED,
	;

	public boolean isActive() {
		return this == ACTIVE;
	}
}
