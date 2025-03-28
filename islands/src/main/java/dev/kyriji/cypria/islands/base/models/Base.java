package dev.kyriji.cypria.islands.base.models;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.cypria.islands.base.building.models.Building;
import dev.kyriji.cypria.islands.base.controllers.BaseManager;
import dev.kyriji.cypria.islands.world.data.CypriaWorld;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Base {
	private UUID uuid;
	private final List<Building> buildingList = new ArrayList<>();

	private transient CypriaWorld cypriaWorld;
	private transient World world;

	// worldedit things
	private transient com.sk89q.worldedit.world.World faweWorld;

	public Base() {
	}

	public Base(CypriaWorld cypriaWorld) {
		this.uuid = UUID.randomUUID();

		initTransient(cypriaWorld);
	}

	public void initTransient(CypriaWorld cypriaWorld) {
		this.cypriaWorld = cypriaWorld;
		this.world = cypriaWorld.getWorld();
		this.faweWorld = BukkitAdapter.adapt(world);

		BaseManager.baseList.add(this);
	}

	public void place() {
		prepareWorld();
		placeBuildings();
	}

	private void prepareWorld() {
		Location center = getCenter();

		// TODO: temp clear
		try (EditSession editSession = WorldEdit.getInstance().newEditSession(faweWorld)) {
			Region region = new CuboidRegion(faweWorld,
					BukkitAdapter.asBlockVector(center).add(-100, -100, -100),
					BukkitAdapter.asBlockVector(center).add(100, 100, 100));
			editSession.setBlocks(region, BlockTypes.AIR);
		}

		Region region = new CuboidRegion(faweWorld,
				BukkitAdapter.asBlockVector(center).add(-10, -1, -10),
				BukkitAdapter.asBlockVector(center).add(10, -1, 10));

		try (EditSession editSession = WorldEdit.getInstance().newEditSession(faweWorld)) {
			editSession.setBlocks(region, BlockTypes.IRON_BLOCK);
		}

		faweWorld.setBlock(BukkitAdapter.asBlockVector(center).add(0, -1, 0), BlockTypes.DIAMOND_BLOCK.getDefaultState());
	}

	private void placeBuildings() {
		for (Building building : buildingList) building.place();
	}

	public void destroy() {
		for (Building building : buildingList) building.destroy();
		BaseManager.baseList.remove(this);

		// TODO: remove when moved to scalable
		MVWorldManager worldManager = CypriaMinecraft.get().mvCore.getMVWorldManager();
		// worldManager.deleteWorld(world.getName());
	}

	public void putBuilding(Building building, BasePos position) {
		building.initTransient(this);
		building.setData(position);
		buildingList.add(building);
	}

	public UUID getUUID() {
		return uuid;
	}

	public List<Building> getBuildingList() {
		return buildingList;
	}

	public World getWorld() {
		return world;
	}

	public Location getCenter() {
		return new Location(world, 0, 70, 0);
	}

	public com.sk89q.worldedit.world.World getFAWEWorld() {
		return faweWorld;
	}
}
