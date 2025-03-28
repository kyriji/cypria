package dev.kyriji.cypria.islands.base.building.path;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for rendering path segments with varied materials and natural appearance.
 */
public class PathRenderer {
	// Core path materials - primarily dirt path
	private static final Material[] CORE_MATERIALS = {
			Material.DIRT_PATH, Material.DIRT_PATH, Material.DIRT_PATH, Material.DIRT_PATH, Material.DIRT_PATH,
			Material.COARSE_DIRT, Material.PACKED_MUD
	};

	// Mid section materials - more variety
	private static final Material[] MID_MATERIALS = {
			Material.DIRT_PATH, Material.DIRT_PATH,
			Material.COARSE_DIRT, Material.COARSE_DIRT,
			Material.PACKED_MUD,
			Material.DIRT
	};

	// Edge materials
	private static final Material[] EDGE_MATERIALS = {
			Material.DIRT_PATH,
			Material.COARSE_DIRT,
			Material.DIRT,
			Material.PACKED_MUD,
	};

	private final NoiseGenerator noiseGenerator;
	private final int yOffset;
	private final Map<String, Block> blockCache = new HashMap<>();

	public PathRenderer(long seed, int yOffset) {
		this.noiseGenerator = new NoiseGenerator(seed);
		this.yOffset = yOffset;
	}

	/**
	 * Render a complete path segment with variation
	 */
	public void renderPathSegment(PathSegment segment) {
		System.out.println("rendering path segment: " + segment);

		PathType pathType = segment.getPathType();
		int pathRadius = pathType.getSize() / 2;

		// First, create a continuous center line
		List<Location> centerLine = createContinuousCenterLine(segment);

		// Track all blocks to set in this segment
		Map<Block, PathBlockInfo> blocksToSet = new HashMap<>();

		// Generate the surrounding blocks around the center line
		generatePathBlocksAroundCenterLine(centerLine, pathRadius, blocksToSet);

		// Set the actual blocks in the world
		for (Map.Entry<Block, PathBlockInfo> entry : blocksToSet.entrySet()) {
			Block block = entry.getKey();
			PathBlockInfo info = entry.getValue();

			// Select material based on distance and noise
			Material material = selectPathMaterial(
					block.getLocation(),
					info.distanceFromCenter,
					pathRadius
			);

			// Only set block if material is not null (null represents no change/air)
			if (material != null) {
				block.setType(material);
			}
		}
	}

	/**
	 * Create a continuous center line with natural waviness
	 */
	private List<Location> createContinuousCenterLine(PathSegment segment) {
		List<Location> centerPoints = new ArrayList<>();
		double segmentLength = segment.getLength();

		// Create points at closer intervals for more precision
		double pointInterval = 0.5; // Half-block intervals
		Vector perpendicular = segment.getPerpendicularVector();

		// Use a consistent noise pattern for the entire segment to ensure continuity
		double baseX = segment.getStart().getLocation().getX() * 0.05;
		double baseZ = segment.getStart().getLocation().getZ() * 0.05;

		// Higher value = smoother, less frequent waves
		double noiseScale = 20.0;
		double maxWaveAmplitude = segment.getPathType().getSize() * 0.20; // 20% of path size

		// Generate points along the segment with consistent waviness
		for (double distance = 0; distance <= segmentLength; distance += pointInterval) {
			// Get point along straight line
			Location basePoint = segment.getPointAt(distance);

			// Calculate wave offset using noise based on segment position
			double progressAlongSegment = distance / segmentLength; // 0 to 1
			double noiseX = baseX + progressAlongSegment * noiseScale * 0.1; // advance through noise space
			double noiseZ = baseZ + progressAlongSegment * noiseScale * 0.1;

			// Get noise value (-1 to 1) and scale to desired amplitude
			double offsetAmount = noiseGenerator.noise(noiseX, noiseZ) * maxWaveAmplitude;

			// Apply offset perpendicular to path direction
			Vector offsetVector = perpendicular.clone().multiply(offsetAmount);
			Location wavedPoint = basePoint.clone().add(offsetVector);

			centerPoints.add(wavedPoint);
		}

		return centerPoints;
	}

	/**
	 * Generate blocks around the center line of the path
	 */
	private void generatePathBlocksAroundCenterLine(List<Location> centerLine,
													int pathRadius,
													Map<Block, PathBlockInfo> blocksToSet) {
		// Create a continuous center line first
		for (Location center : centerLine) {
			// Force the centermost blocks to always be path blocks
			Block centerBlock = center.getBlock().getRelative(0, yOffset, 0);
			String centerKey = centerBlock.getX() + "," + centerBlock.getY() + "," + centerBlock.getZ();
			blockCache.put(centerKey, centerBlock);
			blocksToSet.put(centerBlock, new PathBlockInfo(0.0)); // Distance 0 = center

			// Add surrounding blocks
			World world = center.getWorld();
			final int centerX = center.getBlockX();
			final int centerY = center.getBlockY() + yOffset;
			final int centerZ = center.getBlockZ();

			// Scan in a square around the center point
			for (int xOffset = -pathRadius; xOffset <= pathRadius; xOffset++) {
				for (int zOffset = -pathRadius; zOffset <= pathRadius; zOffset++) {
					// Skip the center block we already added
					if (xOffset == 0 && zOffset == 0) continue;

					final int x = centerX + xOffset;
					final int z = centerZ + zOffset;

					double distance = Math.sqrt(xOffset * xOffset + zOffset * zOffset);

					// Normalize distance to [0,1] range where 0 is center and 1 is edge
					double normalizedDistance = distance / pathRadius;

					if (normalizedDistance <= 1.0) {
						// Add or update path block candidate
						String blockKey = x + "," + centerY + "," + z;
						Block block = blockCache.computeIfAbsent(blockKey,
								k -> world.getBlockAt(x, centerY, z));

						PathBlockInfo existingInfo = blocksToSet.get(block);
						if (existingInfo == null || normalizedDistance < existingInfo.distanceFromCenter) {
							// Either new block or closer to this center than previously recorded
							blocksToSet.put(block, new PathBlockInfo(normalizedDistance));
						}
					}
				}
			}
		}
	}

	/**
	 * Select an appropriate path material based on location and distance from center
	 */
	private Material selectPathMaterial(Location location, double normalizedDistance, int pathRadius) {
		// Use different noise scales for variation
		double nx = location.getX() * 0.15;
		double nz = location.getZ() * 0.15;
		double noise = noiseGenerator.getNormalizedNoise(nx, nz, 1.0);

		Material[] materialOptions;
		double skipChance = 0.0; // Chance of not placing a block

		// Select material array based on distance from center
		if (normalizedDistance < 0.3) {
			// Core center - almost always path blocks
			materialOptions = CORE_MATERIALS;
			skipChance = 0.0; // Never skip in the center
		} else if (normalizedDistance < 0.65) {
			// Mid section - more variety
			materialOptions = MID_MATERIALS;
			skipChance = normalizedDistance * 0.15; // Small chance of skipping, increasing with distance
		} else {
			// Edge section - gradually blending with terrain
			materialOptions = EDGE_MATERIALS;

			// Progressive skip chance:
			// - At 0.65 distance: ~20% skip chance
			// - At 0.80 distance: ~50% skip chance
			// - At 0.90 distance: ~70% skip chance
			// - At 1.00 distance: ~95% skip chance
			double edgeFactor = (normalizedDistance - 0.65) / 0.35;
			skipChance = 0.3 + (edgeFactor * edgeFactor * 0.5);
		}

		// Apply noise to make skip pattern more natural
		double skipNoise = noiseGenerator.getNormalizedNoise(nx * 2.5, nz * 2.5, 1.0);
		if (skipNoise < skipChance) {
			return null; // null means don't place a block (leave as is)
		}

		// Adjust noise to select from material options
		double materialNoise = noiseGenerator.getNormalizedNoise(nx * 1.7, nz * 1.7, 1.0);
		int index = (int)(materialNoise * materialOptions.length);
		if (index >= materialOptions.length) index = materialOptions.length - 1;

		return materialOptions[index];
	}

	/**
	 * Render an entire path network
	 */
	public void renderPathNetwork(List<PathSegment> segments) {
		for (PathSegment segment : segments) {
			renderPathSegment(segment);
		}
	}

	/**
	 * Helper class to track information about blocks in the path
	 */
	private static class PathBlockInfo {
		final double distanceFromCenter;

		PathBlockInfo(double distanceFromCenter) {
			this.distanceFromCenter = distanceFromCenter;
		}
	}
}