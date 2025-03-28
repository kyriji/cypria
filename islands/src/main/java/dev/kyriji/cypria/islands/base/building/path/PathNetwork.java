package dev.kyriji.cypria.islands.base.building.path;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a complete network of paths with nodes and segments
 */
public class PathNetwork {
	private final Set<PathNode> nodes = new HashSet<>();
	private final Set<PathSegment> segments = new HashSet<>();

	/**
	 * Create a node and add it to the network
	 */
	public PathNode createNode(Location location) {
		PathNode node = new PathNode(location);
		nodes.add(node);
		return node;
	}

	/**
	 * Create a segment between nodes if they are aligned and add it to the network
	 */
	public PathSegment createSegment(PathNode start, PathNode end, PathType pathType) {
		// Ensure nodes are on the same X or Z axis
		if (start.getLocation().getBlockX() != end.getLocation().getBlockX() &&
				start.getLocation().getBlockZ() != end.getLocation().getBlockZ()) {
			System.out.println("cannot create segment: nodes are not aligned on an axis");
			return null;
		}

		PathSegment segment = new PathSegment(start, end, pathType);
		segments.add(segment);
		return segment;
	}

	/**
	 * Create a more expansive path network for demonstration
	 */
	public static PathNetwork createExampleNetwork(Location center) {
		System.out.println("creating example path network");
		PathNetwork network = new PathNetwork();

		// Main node at the center
		PathNode centerNode = network.createNode(center);

		// === MAIN PATH GRID - widely spaced ===
		// Major road nodes at greater distances
		PathNode northNode = network.createNode(center.clone().add(0, 0, -50));
		PathNode eastNode = network.createNode(center.clone().add(50, 0, 0));
		PathNode southNode = network.createNode(center.clone().add(0, 0, 50));
		PathNode westNode = network.createNode(center.clone().add(-50, 0, 0));

		// Create the outer grid at even greater distances
		PathNode northEastNode = network.createNode(center.clone().add(50, 0, -50));
		PathNode southEastNode = network.createNode(center.clone().add(50, 0, 50));
		PathNode southWestNode = network.createNode(center.clone().add(-50, 0, 50));
		PathNode northWestNode = network.createNode(center.clone().add(-50, 0, -50));

		// Create extended nodes even further out
		PathNode farNorthNode = network.createNode(center.clone().add(0, 0, -100));
		PathNode farEastNode = network.createNode(center.clone().add(100, 0, 0));
		PathNode farSouthNode = network.createNode(center.clone().add(0, 0, 100));
		PathNode farWestNode = network.createNode(center.clone().add(-100, 0, 0));

		// Create even more distant extensions
		PathNode veryFarNorthEastNode = network.createNode(center.clone().add(100, 0, -100));
		PathNode veryFarEastNode = network.createNode(center.clone().add(150, 0, 0));

		// === SMALL PATHS AND OFFSHOOTS ===
		// Small paths at various angles (but still aligned to grid)
		PathNode northSmallBranch1 = network.createNode(center.clone().add(-25, 0, -30));
		PathNode northSmallBranch2 = network.createNode(center.clone().add(-25, 0, -50));
		PathNode northSmallBranch3 = network.createNode(center.clone().add(-25, 0, -80));

		PathNode eastSmallBranch1 = network.createNode(center.clone().add(30, 0, 15));
		PathNode eastSmallBranch2 = network.createNode(center.clone().add(80, 0, 15));

		PathNode southSmallBranch1 = network.createNode(center.clone().add(20, 0, 30));
		PathNode southSmallBranch2 = network.createNode(center.clone().add(20, 0, 70));

		PathNode westSmallBranch1 = network.createNode(center.clone().add(-70, 0, -20));
		PathNode westSmallBranch2 = network.createNode(center.clone().add(-30, 0, -20));

		PathNode randomBranch1 = network.createNode(center.clone().add(-60, 0, 30));
		PathNode randomBranch2 = network.createNode(center.clone().add(-60, 0, 70));
		PathNode randomBranch3 = network.createNode(center.clone().add(-30, 0, 30));

		// More small offshoots
		PathNode offshoot1 = network.createNode(center.clone().add(70, 0, -30));
		PathNode offshoot2 = network.createNode(center.clone().add(70, 0, -60));
		PathNode offshoot3 = network.createNode(center.clone().add(30, 0, -30));

		// === CREATE MAIN ROAD SEGMENTS ===
		// Main cross roads (large paths)
		network.createSegment(centerNode, northNode, PathType.LARGE_PATH);
		network.createSegment(centerNode, eastNode, PathType.LARGE_PATH);
		network.createSegment(centerNode, southNode, PathType.LARGE_PATH);
		network.createSegment(centerNode, westNode, PathType.LARGE_PATH);

		// Outer grid segments (large paths)
		network.createSegment(northNode, northEastNode, PathType.LARGE_PATH);
		network.createSegment(eastNode, southEastNode, PathType.LARGE_PATH);
		network.createSegment(southNode, southWestNode, PathType.LARGE_PATH);
		network.createSegment(westNode, northWestNode, PathType.LARGE_PATH);
		network.createSegment(northEastNode, northWestNode, PathType.LARGE_PATH);
		network.createSegment(northEastNode, southEastNode, PathType.LARGE_PATH);
		network.createSegment(southEastNode, southWestNode, PathType.LARGE_PATH);
		network.createSegment(southWestNode, northWestNode, PathType.LARGE_PATH);

		// Extended road segments
		network.createSegment(northNode, farNorthNode, PathType.LARGE_PATH);
		network.createSegment(eastNode, farEastNode, PathType.LARGE_PATH);
		network.createSegment(southNode, farSouthNode, PathType.LARGE_PATH);
		network.createSegment(westNode, farWestNode, PathType.LARGE_PATH);

		// More extended segments
		network.createSegment(northEastNode, veryFarNorthEastNode, PathType.LARGE_PATH);
		network.createSegment(farEastNode, veryFarEastNode, PathType.LARGE_PATH);

		// === CREATE SMALL PATH SEGMENTS ===
		// North small path branches
		network.createSegment(northNode, northSmallBranch1, PathType.SMALL_PATH);
		network.createSegment(northSmallBranch1, northSmallBranch2, PathType.SMALL_PATH);
		network.createSegment(northSmallBranch2, northSmallBranch3, PathType.SMALL_PATH);

		// East small path branches
		network.createSegment(eastNode, eastSmallBranch1, PathType.SMALL_PATH);
		network.createSegment(eastSmallBranch1, eastSmallBranch2, PathType.SMALL_PATH);

		// South small path branches
		network.createSegment(southNode, southSmallBranch1, PathType.SMALL_PATH);
		network.createSegment(southSmallBranch1, southSmallBranch2, PathType.SMALL_PATH);

		// West small path branches
		network.createSegment(westNode, westSmallBranch1, PathType.SMALL_PATH);
		network.createSegment(westSmallBranch1, westSmallBranch2, PathType.SMALL_PATH);

		// Random branches and connections
		network.createSegment(southWestNode, randomBranch1, PathType.SMALL_PATH);
		network.createSegment(randomBranch1, randomBranch2, PathType.SMALL_PATH);
		network.createSegment(randomBranch1, randomBranch3, PathType.SMALL_PATH);

		// Offshoots from main grid
		network.createSegment(northEastNode, offshoot1, PathType.SMALL_PATH);
		network.createSegment(offshoot1, offshoot2, PathType.SMALL_PATH);
		network.createSegment(offshoot1, offshoot3, PathType.SMALL_PATH);

		System.out.println("created example network with " + network.nodes.size() +
				" nodes and " + network.segments.size() + " segments");
		return network;
	}

	/**
	 * Get all segments in the network
	 */
	public Set<PathSegment> getSegments() {
		return segments;
	}
}