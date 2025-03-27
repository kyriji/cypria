package dev.kyriji.commonmc.cypria.player.models;

import org.bukkit.entity.Player;

import java.util.UUID;

public class CypriaPlayer {
	private final UUID uuid;
	private Player player;

	// transient data
	// private CachedEquipmentData cachedEquipmentData;

	// persistent data
	// private Document stats;

	public CypriaPlayer(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void attachPlayer(Player player) {
		assert !hasPlayer();
		this.player = player;
	}

	public boolean hasPlayer() {
		return player != null;
	}

	public Player getPlayer() {
		return player;
	}
}
