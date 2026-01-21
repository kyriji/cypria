package dev.kyriji.controllers.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.controllers.GameManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemDropSystem extends EntityEventSystem<EntityStore, DropItemEvent> {
	public ItemDropSystem() {
		super(DropItemEvent.class);

		System.out.println("Registered ItemDropSystem");
	}

	@Override
	public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull DropItemEvent dropItemEvent) {
		Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(i);
		Player player = store.getComponent(playerRef, Player.getComponentType());

		if(player == null) return;
		if(player.getWorld() != GameManager.PIT) return;

		dropItemEvent.setCancelled(true);
		System.out.println("Canceled item drop for player: " + player.getDisplayName());
	}

	@Nullable
	@Override
	public Query<EntityStore> getQuery() {
		return PlayerRef.getComponentType();
	}
}
