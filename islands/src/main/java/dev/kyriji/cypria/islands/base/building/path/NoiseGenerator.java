package dev.kyriji.cypria.islands.base.building.path;

import java.util.Random;

/**
 * Generates deterministic noise for natural path variation.
 */
public class NoiseGenerator {
	private final long seed;
	private final Random random;

	// Simplex noise parameters
	private static final int GRAD3[][] = {
			{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
			{1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
			{0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}
	};

	private int perm[] = new int[512];

	public NoiseGenerator(long seed) {
		this.seed = seed;
		this.random = new Random(seed);
		initPermutationTable();
	}

	private void initPermutationTable() {
		// Initialize permutation table with randomized indices 0..255
		for (int i = 0; i < 256; i++) {
			perm[i] = i;
		}

		// Shuffle
		for (int i = 0; i < 255; i++) {
			int j = random.nextInt(256 - i) + i;
			int temp = perm[i];
			perm[i] = perm[j];
			perm[j] = temp;
		}

		// Copy to avoid overflow
		for (int i = 0; i < 256; i++) {
			perm[i + 256] = perm[i];
		}
	}

	/**
	 * Simplex noise function for 2D coordinates
	 */
	public double noise(double x, double y) {
		// Skew input space to determine simplex cell
		double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
		double s = (x + y) * F2;
		int i = fastFloor(x + s);
		int j = fastFloor(y + s);

		double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
		double t = (i + j) * G2;

		// Unskew back to (x,y) space
		double X0 = i - t;
		double Y0 = j - t;

		// The x,y distances from the cell origin
		double x0 = x - X0;
		double y0 = y - Y0;

		// Determine which simplex we're in
		int i1, j1;
		if (x0 > y0) {   // lower triangle, XY order: (0,0)->(1,0)->(1,1)
			i1 = 1;
			j1 = 0;
		} else {         // upper triangle, YX order: (0,0)->(0,1)->(1,1)
			i1 = 0;
			j1 = 1;
		}

		// Offsets for corners
		double x1 = x0 - i1 + G2;
		double y1 = y0 - j1 + G2;
		double x2 = x0 - 1.0 + 2.0 * G2;
		double y2 = y0 - 1.0 + 2.0 * G2;

		// Calculate contribution from each corner
		double n0 = getCornerNoise(x0, y0, i, j);
		double n1 = getCornerNoise(x1, y1, i + i1, j + j1);
		double n2 = getCornerNoise(x2, y2, i + 1, j + 1);

		// Add contributions and scale to [-1, 1]
		return 70.0 * (n0 + n1 + n2);
	}

	private double getCornerNoise(double x, double y, int gi_x, int gi_y) {
		// Work out the hashed gradient indices
		int gi = perm[(perm[gi_x & 0xFF] + gi_y) & 0xFF] % 12;

		// Calculate the contribution from the three corners
		double t = 0.5 - x * x - y * y;
		if (t < 0) return 0.0;

		t *= t;
		return t * t * dot(GRAD3[gi], x, y);
	}

	private double dot(int[] g, double x, double y) {
		return g[0] * x + g[1] * y;
	}

	private int fastFloor(double x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	/**
	 * Get a noise value between 0 and 1
	 */
	public double getNormalizedNoise(double x, double y, double scale) {
		return (noise(x / scale, y / scale) + 1) / 2.0;
	}

	/**
	 * Get a value between min and max based on noise
	 */
	public double getScaledNoise(double x, double y, double scale, double min, double max) {
		return min + getNormalizedNoise(x, y, scale) * (max - min);
	}
}