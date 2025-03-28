package dev.kyriji.commonmc.cypria.player.controllers;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.player.models.CypriaPlayer;
import dev.kyriji.commonmc.cypria.playerdata.controllers.PlayerDataManager;
import org.bukkit.Bukkit;
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

	public static void onPlayerDataLoad(UUID uuid) {
		assert getPlayer(uuid) == null;

		CypriaPlayer cypriaPlayer = new CypriaPlayer(uuid);
		playerList.add(cypriaPlayer);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		CypriaMinecraft.cypriaInstance.addPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		PlayerDataManager.loadPlayerData(event.getPlayer().getUniqueId()).thenRun(() -> {
			Player player = event.getPlayer();
			CypriaPlayer cypriaPlayer = getPlayer(player.getUniqueId());
			assert cypriaPlayer != null;

			cypriaPlayer.attachPlayer(player);
		})
		.exceptionally(ex -> {
			ex.printStackTrace(); // Print the exception for debugging
			return null;
		});
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		CypriaMinecraft.cypriaInstance.removePlayer(event.getPlayer().getUniqueId());
		CypriaCommon.getPlayerDataManager().unloadPlayerData(event.getPlayer().getUniqueId());

		Player player = event.getPlayer();
		CypriaPlayer cypriaPlayer = getPlayer(player.getUniqueId());
		assert cypriaPlayer != null;

		if (!CypriaCommon.getPlayerDataManager().isFrozen(event.getPlayer().getUniqueId()))  {
			cypriaPlayer.save();
		}

		playerList.remove(cypriaPlayer);
	}

	public static CypriaPlayer getPlayer(UUID uuid) {
		for (CypriaPlayer cypriaPlayer : playerList) if (cypriaPlayer.getUUID().equals(uuid)) return cypriaPlayer;
		return null;
	}
}
