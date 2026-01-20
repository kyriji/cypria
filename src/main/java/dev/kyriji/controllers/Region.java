package dev.kyriji.controllers;

import com.hypixel.hytale.math.vector.Vector3d;

public class Region {
	private final Vector3d min;
	private final Vector3d max;

	public Region(Vector3d min, Vector3d max) {
		this.min = min;
		this.max = max;
	}

	public boolean contains(Vector3d point) {
		return point.x >= min.x && point.x <= max.x &&
			   point.y >= min.y && point.y <= max.y &&
			   point.z >= min.z && point.z <= max.z;
	}
}
