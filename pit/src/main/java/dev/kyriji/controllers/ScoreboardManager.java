package dev.kyriji.controllers;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import dev.kyriji.ui.PlayerHud;
import dev.kyriji.utils.PlayerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
	private static final Map<UUID, PlayerHud> playerHuds = new HashMap<>();

	public ScoreboardManager(JavaPlugin plugin) {
		plugin.getEventRegistry().registerGlobal(PlayerReadyEvent.class, event -> {
			preparePlayer(event.getPlayer());
		});
	}

	public static void preparePlayer(Player player) {
		PlayerRef playerRef = PlayerUtils.getPlayerRef(player);

		PlayerHud playerHud = new PlayerHud(playerRef);
		playerHuds.put(playerRef.getUuid(), playerHud);

		player.getHudManager().setCustomHud(playerRef, playerHud);
	}

	public static void removePlayer(Player player) {
		PlayerRef playerRef = PlayerUtils.getPlayerRef(player);

		PlayerHud hud = playerHuds.get(playerRef.getUuid());
		if (hud != null) hud.destroy();

		playerHuds.remove(playerRef.getUuid());
	}




}
