package dev.kyriji.objects;

import java.util.UUID;

public class PitPlayer {
	public UUID uuid;

	public int kills;
	public int deaths;

	public int currentStreak;
	public double gold;

	public PitPlayer(UUID uuid) {
		this.uuid = uuid;

		 load();
	}

	public void load() {
		this.kills = 0;
		this.deaths = 0;

		this.currentStreak = 0;
		this.gold = 0.0;
	}

	public void save() {

	}


}
