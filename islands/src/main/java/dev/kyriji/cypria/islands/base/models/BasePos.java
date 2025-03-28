package dev.kyriji.cypria.islands.base.models;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;

public class BasePos {
	private int x;
	private int y;
	private int z;

	public BasePos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location toLocation(Base base) {
		return new Location(base.getWorld(), base.getCenter().getBlockX() + x,
				base.getCenter().getBlockY() + y , base.getCenter().getBlockZ() + z);
	}

	public BlockVector3 toFAWEBlockVector(Base base) {
		return BlockVector3.at(base.getCenter().getBlockX() + x, base.getCenter().getBlockY() + y,
				base.getCenter().getBlockZ() + z);
	}
}
