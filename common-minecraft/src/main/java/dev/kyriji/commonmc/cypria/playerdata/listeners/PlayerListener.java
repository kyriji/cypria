package dev.kyriji.commonmc.cypria.playerdata.listeners;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.playerdata.controllers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		new Thread(() -> {
			CypriaMinecraft.cypriaInstance.addPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
			PlayerDataManager.loadPlayerData(event.getPlayer().getUniqueId());
		}).start();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		new Thread(() -> {
			CypriaMinecraft.cypriaInstance.removePlayer(event.getPlayer().getUniqueId());
			CypriaCommon.getPlayerDataManager().unloadPlayerData(event.getPlayer().getUniqueId());
		}).start();
	}
}
