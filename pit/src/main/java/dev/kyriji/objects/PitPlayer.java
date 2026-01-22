package dev.kyriji.objects;

import dev.kyriji.data.PitPlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PitPlayer {
	public final UUID uuid;
	public final PitPlayerData data;

	public Map<UUID, DamageEntry> damageMap;
	public int currentStreak;

	public PitPlayer(UUID uuid, PitPlayerData data) {
		this.uuid = uuid;
		this.data = data;
		this.damageMap = new HashMap<>();
		this.currentStreak = 0;
	}

	public void addKill() {
		data.addKill();
		currentStreak++;
		if (currentStreak > data.getHighestStreak()) {
			data.setHighestStreak(currentStreak);
		}
	}

	public void addAssist() {
		data.addAssist();
	}

	public void addDeath() {
		data.addDeath();
		currentStreak = 0;
	}

	public void addGold(double amount) {
		data.addGold(amount);
	}

	public int getKills() {
		return data.getKills();
	}

	public int getAssists() {
		return data.getAssists();
	}

	public int getDeaths() {
		return data.getDeaths();
	}

	public double getGold() {
		return data.getGold();
	}

	public int getCurrentStreak() {
		return currentStreak;
	}

	public int getHighestStreak() {
		return data.getHighestStreak();
	}

	public void save() {
		data.save();
	}
}
