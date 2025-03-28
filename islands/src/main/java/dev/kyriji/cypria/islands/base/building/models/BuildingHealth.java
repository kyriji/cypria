package dev.kyriji.cypria.islands.base.building.models;

public class BuildingHealth {
	private final double maxHealth;
	private double health;

	public BuildingHealth(double maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	public void damage(double amount) {
		health -= amount;
		if (health < 0) health = 0;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public double getHealth() {
		return health;
	}

	public boolean isDead() {
		return health <= 0;
	}
}
