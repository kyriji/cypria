package dev.kyriji.cypria.islands.world.controllers;

import dev.kyriji.cypria.islands.world.data.CypriaWorld;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {
	private static final List<CypriaWorld> worldList = new ArrayList<>();
	private static final int TOTAL_WORLDS = 3;

	public static void init() {
		for (int i = 0; i < TOTAL_WORLDS; i++) {
			CypriaWorld world = new CypriaWorld(i + 1);
			worldList.add(world);
		}
	}

	public static CypriaWorld acquireWorld() {
		for (CypriaWorld world : worldList) {
			if (!world.isInUse()) {
				world.acquire();
				return world;
			}
		}
		throw new IllegalStateException("No available worlds");
	}

	public static int getAvailableWorlds() {
		return worldList.stream()
				.mapToInt(world -> world.isInUse() ? 0 : 1)
				.sum();
	}

	public static void notifyStateChange() {
		int availableWorlds = getAvailableWorlds();
	// 	TODO: wiji send whatever message to whatever servers need it that the count of available worlds has changed
	}
}
