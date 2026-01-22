package dev.kyriji.utils;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerUtils {

	public static PlayerRef getPlayerRef(Player player) {
		Store<EntityStore> store = Objects.requireNonNull(player.getReference()).getStore();

		return Objects.requireNonNull(store.getComponent(player.getReference(), PlayerRef.getComponentType()));
	}

	public static CompletableFuture<Player> getPlayerFromRef(PlayerRef playerRef) {
		UUID worldUUID = Objects.requireNonNull(playerRef.getWorldUuid());
		World world = Objects.requireNonNull(Universe.get().getWorld(worldUUID));

		CompletableFuture<Player> future = new CompletableFuture<>();

		world.execute(() -> {
			Store<EntityStore> store = Objects.requireNonNull(playerRef.getReference()).getStore();

			Player player = store.getComponent(playerRef.getReference(), Player.getComponentType());

			future.complete(player);
		});

		return future;
	}

	public static CompletableFuture<Player> getPlayerFromUUID(UUID uuid) {
		for(PlayerRef playerRef : Universe.get().getPlayers()) {
			if (!playerRef.getUuid().equals(uuid)) continue;

			return getPlayerFromRef(playerRef);
		}

		return CompletableFuture.completedFuture(null);
	}
}
