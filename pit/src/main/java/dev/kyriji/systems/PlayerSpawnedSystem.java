package dev.kyriji.systems;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerSpawnedSystem extends PlayerSystems.PlayerSpawnedSystem {
	@Nonnull
	@Override
	public Query<EntityStore> getQuery() {
		return Player.getComponentType();
	}

	@Override
	public void onEntityAdded(@Nonnull Ref<EntityStore> ref, @Nonnull AddReason reason, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
		Player player = store.getComponent(ref, Player.getComponentType());
		if (player == null) return;

		player.setFirstSpawn(true);

	}
}
