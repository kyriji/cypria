package dev.kyriji.objects;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import dev.kyriji.controllers.EnderChestWindow;
import dev.kyriji.data.PitPlayerData;
import dev.kyriji.utils.PlayerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PitPlayer {
	public final UUID uuid;
	public final PitPlayerData data;

	public Map<UUID, DamageEntry> damageMap;
	public int currentStreak;

	public EnderChestWindow enderChest;

	public PitPlayer(UUID uuid, PitPlayerData data) {
		this.uuid = uuid;
		this.data = data;
		this.damageMap = new HashMap<>();
		this.currentStreak = 0;
	}

	public void onPlayerReady(Player player) {
		if (player.getUuid() != this.uuid) return;

		this.enderChest = new EnderChestWindow(player, data.getEnderChestData());
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
		data.setEnderChestData(enderChest.getContentsAsBson());
		data.save();
	}
}
