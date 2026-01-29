package dev.kyriji.controllers;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import dev.kyriji.common.HytaleCommon;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.data.PitPlayerData;
import dev.kyriji.objects.PitPlayer;
import dev.kyriji.utils.PlayerUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
	public static final PlayerDataType PIT_PLAYER_DATA = new PlayerDataType("pit_players", PitPlayerData.class);

	private static final ConcurrentHashMap<UUID, PitPlayer> pitPlayers = new ConcurrentHashMap<>();

	public PlayerDataManager(JavaPlugin plugin) {
		PlayerDataType.register(PIT_PLAYER_DATA);

		plugin.getEventRegistry().registerGlobal(PlayerConnectEvent.class, event -> {
			UUID uuid = event.getPlayerRef().getUuid();
			loadPlayer(uuid);
		});

		plugin.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, event -> {
			UUID uuid = event.getPlayerRef().getUuid();

			PitPlayer pitPlayer = pitPlayers.get(uuid);
			if (pitPlayer != null) {
				pitPlayer.save();
				pitPlayers.remove(uuid);
				HytaleCommon.getPlayerDataManager().unloadPlayerData(uuid);
			}
		});
	}

	public void loadPlayer(UUID uuid) {
		HytaleCommon.getPlayerDataManager().loadPlayerData(uuid, PitPlayerData.class)
				.thenAccept(data -> {
					PitPlayer pitPlayer = new PitPlayer(uuid, data);
					pitPlayers.put(uuid, pitPlayer);
					System.out.println("Loaded PitPlayer data for " + uuid);
				})
				.exceptionally(ex -> {
					ex.printStackTrace();
					return null;
				});
	}

	public static PitPlayer getPitPlayer(Player player) {
		return getPitPlayer(PlayerUtils.getPlayerRef(player).getUuid());
	}

	public static PitPlayer getPitPlayer(UUID uuid) {
		PitPlayer pitPlayer = pitPlayers.get(uuid);
		if (pitPlayer == null) {
			throw new RuntimeException("PitPlayer not found for UUID: " + uuid + " (data may still be loading)");
		}
		return pitPlayer;
	}

	public static boolean isLoaded(UUID uuid) {
		return pitPlayers.containsKey(uuid);
	}
}
