package dev.kyriji.controllers;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.objects.PitPlayer;
import dev.kyriji.utils.PlayerUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerManager {
	private static final List<PitPlayer> pitPlayers = new ArrayList<>();

	public PlayerManager(JavaPlugin plugin) {
		plugin.getEventRegistry().registerGlobal(PlayerConnectEvent.class, event -> {
			UUID uuid = event.getPlayerRef().getUuid();

			registerPlayer(uuid);
		});

		plugin.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, event -> {
			UUID uuid = event.getPlayerRef().getUuid();

			PitPlayer pitPlayer = getPitPlayer(uuid);
			pitPlayer.save();
			pitPlayers.remove(pitPlayer);
		});
	}

	public void registerPlayer(UUID uuid) {
		pitPlayers.forEach(pitPlayer -> {
			if (pitPlayer.uuid.equals(uuid)) throw new RuntimeException("Player already registered: " + uuid);
		});

		PitPlayer pitPlayer = new PitPlayer(uuid);
		pitPlayers.add(pitPlayer);
	}

	public static PitPlayer getPitPlayer(Player player) {
		return getPitPlayer(PlayerUtils.getPlayerRef(player).getUuid());
	}

	public static PitPlayer getPitPlayer(UUID uuid) {
		for(PitPlayer pitPlayer : pitPlayers) {
			if (pitPlayer.uuid.equals(uuid)) return pitPlayer;
		}

		throw new RuntimeException("PitPlayer not found for UUID: " + uuid);
	}
}
