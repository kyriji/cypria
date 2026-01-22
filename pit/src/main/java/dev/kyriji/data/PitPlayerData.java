package dev.kyriji.data;

import dev.kyriji.common.playerdata.models.PlayerDataDocument;

import java.util.UUID;

public class PitPlayerData extends PlayerDataDocument {

	private int kills;
	private int assists;
	private int deaths;
	private int highestStreak;
	private double gold;

	public PitPlayerData() {
		super();
	}

	public PitPlayerData(UUID uuid) {
		this.uuid = uuid.toString();
		this.kills = 0;
		this.assists = 0;
		this.deaths = 0;
		this.highestStreak = 0;
		this.gold = 0.0;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getHighestStreak() {
		return highestStreak;
	}

	public void setHighestStreak(int highestStreak) {
		this.highestStreak = highestStreak;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public void addKill() {
		this.kills++;
	}

	public void addAssist() {
		this.assists++;
	}

	public void addDeath() {
		this.deaths++;
	}

	public void addGold(double amount) {
		this.gold += amount;
	}
}
