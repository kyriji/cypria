package dev.kyriji.commonmc.cypria.player.controllers;

import dev.kyriji.commonmc.cypria.player.models.CypriaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager implements Listener {
	public static List<CypriaPlayer> playerList = new ArrayList<>();

	// TODO: wiji replace this with when the player data is loaded
	public void onPlayerDataLoad(UUID uuid) {
		assert getPlayer(uuid) == null;

		CypriaPlayer cypriaPlayer = new CypriaPlayer(uuid);
		playerList.add(cypriaPlayer);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		CypriaPlayer cypriaPlayer = getPlayer(player.getUniqueId());
		assert cypriaPlayer != null;

		cypriaPlayer.attachPlayer(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		CypriaPlayer cypriaPlayer = getPlayer(player.getUniqueId());
		assert cypriaPlayer != null;

	// 	TODO: data saving?

		playerList.remove(cypriaPlayer);
	}

	public static CypriaPlayer getPlayer(UUID uuid) {
		for (CypriaPlayer cypriaPlayer : playerList) if (cypriaPlayer.getUUID().equals(uuid)) return cypriaPlayer;
		return null;
	}
}
