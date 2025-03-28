package dev.kyriji.cypria.islands.world.data;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.cypria.islands.world.controllers.WorldManager;
import org.bukkit.*;

public class CypriaWorld {
	private final int id;
	private final World world;

	private boolean inUse;

	public CypriaWorld(int id) {
		this.id = id;
		this.world = getOrCreateWorld();
	}

	// TODO: clean when moved to scalable
	private World getOrCreateWorld() {
		MVWorldManager worldManager = CypriaMinecraft.get().mvCore.getMVWorldManager();
		World world = Bukkit.getWorld(getName());
		if (world != null) {
			if (worldManager.isMVWorld(getName())) return world;
			boolean success = worldManager.deleteWorld(getName());
			if (!success) throw new IllegalStateException("Failed to delete world " + getName());
		}

		boolean success = worldManager.addWorld(
				getName(),
				World.Environment.NORMAL,
				null,
				WorldType.NORMAL,
				false,
				"CleanroomGenerator:."
		);
		if (!success) throw new IllegalStateException("Failed to create world " + getName());

		world = Bukkit.getWorld(getName());

		MultiverseWorld multiverseWorld = worldManager.getMVWorld(getName());
		multiverseWorld.setSpawnLocation(new Location(world, 0, 70, 0));
		multiverseWorld.setGameMode(GameMode.CREATIVE);

		return world;
	}

	public void acquire() {
		if (inUse) throw new IllegalStateException("World is already in use");
		inUse = true;
		WorldManager.notifyStateChange();
	}

	public String getName() {
		return "world" + id;
	}

	public boolean isInUse() {
		return inUse;
	}

	public World getWorld() {
		return world;
	}
}
