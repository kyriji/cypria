package dev.kyriji.objects;

public class DamageEntry {
	private float damageAmount;
	private long timestamp;

	public DamageEntry(float damageAmount) {
		this.damageAmount = damageAmount;
		this.timestamp = System.currentTimeMillis();
	}

	public float getDamageAmount() {
		return damageAmount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void addDamage(float damage) {
		this.damageAmount += damage;
	}

	public void updateTimestamp() {
		this.timestamp = System.currentTimeMillis();
	}
}
