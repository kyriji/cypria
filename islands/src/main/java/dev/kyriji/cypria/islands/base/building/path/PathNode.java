package dev.kyriji.cypria.islands.base.building.path;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a node in a path network (intersection or endpoint).
 */
public class PathNode {
	private final Location location;
	private final List<PathSegment> connectedSegments = new ArrayList<>();

	public PathNode(Location location) {
		this.location = location.clone();
	}

	public Location getLocation() {
		return location.clone();
	}

	public void addSegment(PathSegment segment) {
		if (!connectedSegments.contains(segment)) {
			connectedSegments.add(segment);
		}
	}

	public List<PathSegment> getConnectedSegments() {
		return new ArrayList<>(connectedSegments);
	}

	public boolean isIntersection() {
		return connectedSegments.size() > 2;
	}

	public boolean isEndpoint() {
		return connectedSegments.size() == 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PathNode pathNode = (PathNode) o;
		return Objects.equals(location.getBlockX(), pathNode.location.getBlockX()) &&
				Objects.equals(location.getBlockY(), pathNode.location.getBlockY()) &&
				Objects.equals(location.getBlockZ(), pathNode.location.getBlockZ());
	}

	@Override
	public int hashCode() {
		return Objects.hash(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	@Override
	public String toString() {
		return "PathNode{" +
				"location=" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() +
				", connections=" + connectedSegments.size() +
				'}';
	}
}