package dev.kyriji.cypria.islands.base.building.path;

/**
 * Defines different types of paths and their properties.
 */
public enum PathType {
	LARGE_PATH(11),
	SMALL_PATH(7);

	private final int size;

	PathType(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}