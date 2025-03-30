package dev.kyriji.cypria.islands.base.building.path;

import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class PathNetwork {
	private final Set<PathNode> nodes = new HashSet<>();
	private final Set<PathSegment> segments = new HashSet<>();

	public PathNode createNode(Location location) {
		PathNode node = new PathNode(location);
		nodes.add(node);
		return node;
	}

	public PathSegment createSegment(PathNode start, PathNode end, PathType pathType) {
		if (start.getLocation().getBlockX() != end.getLocation().getBlockX() &&
				start.getLocation().getBlockZ() != end.getLocation().getBlockZ()) {
			AUtil.log("Misaligned nodes: " + start.getLocation() + " and " + end.getLocation());
			throw new IllegalArgumentException("Nodes must be aligned on an axis");
		}

		PathSegment segment = new PathSegment(start, end, pathType);
		segments.add(segment);
		return segment;
	}

	/**
	 * Get all segments in the network
	 */
	public Set<PathSegment> getSegments() {
		return segments;
	}
}