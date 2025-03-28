package dev.kyriji.cypria.islands.base.building.path;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a segment of a path between two nodes.
 */
public class PathSegment {
	private final PathNode start;
	private final PathNode end;
	private final PathType pathType;
	private final double length;
	private final Vector direction;

	public PathSegment(PathNode start, PathNode end, PathType pathType) {
		this.start = start;
		this.end = end;
		this.pathType = pathType;

		// Calculate length and direction
		Vector startVec = start.getLocation().toVector();
		Vector endVec = end.getLocation().toVector();
		this.direction = endVec.clone().subtract(startVec).normalize();
		this.length = start.getLocation().distance(end.getLocation());

		// Connect this segment to its nodes
		start.addSegment(this);
		end.addSegment(this);
	}

	public PathNode getStart() {
		return start;
	}

	public PathNode getEnd() {
		return end;
	}

	public PathType getPathType() {
		return pathType;
	}

	public double getLength() {
		return length;
	}

	public Vector getDirection() {
		return direction.clone();
	}

	/**
	 * Check if this segment connects nodes that share an X or Z coordinate (straight line)
	 */
	public boolean isStraightLine() {
		return start.getLocation().getBlockX() == end.getLocation().getBlockX() ||
				start.getLocation().getBlockZ() == end.getLocation().getBlockZ();
	}

	/**
	 * Get a point along the segment at a specified distance from the start.
	 */
	public Location getPointAt(double distance) {
		if (distance < 0) distance = 0;
		if (distance > length) distance = length;

		Vector point = start.getLocation().toVector().add(direction.clone().multiply(distance));
		return point.toLocation(start.getLocation().getWorld());
	}

	/**
	 * Get the perpendicular vector to the path direction (in the horizontal plane).
	 */
	public Vector getPerpendicularVector() {
		return new Vector(-direction.getZ(), 0, direction.getX()).normalize();
	}

	/**
	 * Generate a series of points along the path segment.
	 */
	public List<Location> generatePathPoints(double spacing) {
		List<Location> points = new ArrayList<>();

		double totalDistance = 0;
		while (totalDistance <= length) {
			points.add(getPointAt(totalDistance));
			totalDistance += spacing;
		}

		// Ensure the end point is included
		if (totalDistance - spacing < length) {
			points.add(end.getLocation());
		}

		return points;
	}

	/**
	 * Calculate the distance from a location to this segment.
	 */
	public double distanceToSegment(Location location) {
		Vector p = location.toVector();
		Vector a = start.getLocation().toVector();
		Vector b = end.getLocation().toVector();

		Vector ab = b.clone().subtract(a);
		Vector ap = p.clone().subtract(a);

		double t = ap.dot(ab) / ab.lengthSquared();
		t = Math.max(0, Math.min(1, t));

		Vector closestPoint = a.clone().add(ab.clone().multiply(t));
		return p.distance(closestPoint);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PathSegment that = (PathSegment) o;
		return (Objects.equals(start, that.start) && Objects.equals(end, that.end)) ||
				(Objects.equals(start, that.end) && Objects.equals(end, that.start));
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				Math.min(start.hashCode(), end.hashCode()),
				Math.max(start.hashCode(), end.hashCode())
		);
	}

	@Override
	public String toString() {
		return "PathSegment{" +
				"start=" + start.getLocation().getBlockX() + "," + start.getLocation().getBlockZ() +
				", end=" + end.getLocation().getBlockX() + "," + end.getLocation().getBlockZ() +
				", length=" + length +
				", type=" + pathType +
				'}';
	}
}