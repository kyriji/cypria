package dev.kyriji.cypria.islands.base.building.path;

import org.bukkit.Location;

import java.util.List;

/**
 * Main path generation class - coordinates the path creation process
 */
public class Path {
	private final PathNetwork network;
	private final PathRenderer renderer;
	private final long seed;

	/**
	 * Create a path from center location (static example path)
	 */
	public Path(Location center) {
		System.out.println("creating path at " + center.getBlockX() + "," + center.getBlockZ());
		this.seed = System.currentTimeMillis();
		this.network = PathNetwork.createExampleNetwork(center);
		this.renderer = new PathRenderer(seed, 8); // 8 is the y-offset

		// Render the path
		renderPath();
	}

	/**
	 * Render the entire path network
	 */
	public void renderPath() {
		System.out.println("rendering path with " + network.getSegments().size() + " segments");
		renderer.renderPathNetwork(List.copyOf(network.getSegments()));
	}

	/**
	 * Get the path network
	 */
	public PathNetwork getNetwork() {
		return network;
	}
}