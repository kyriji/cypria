package dev.kyriji.controllers.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.RootDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.controllers.SpawnManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class DamageSystem extends DamageEventSystem {
	@Override
	public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull Damage damage) {
		Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(i);
		Player player = store.getComponent(playerRef, Player.getComponentType());
		if(player == null) return;

		if(player.getWorld() != SpawnManager.PIT) return;

		EntityStatMap stats = store.getComponent(playerRef, EntityStatMap.getComponentType());
		if(stats == null) return;

		if(damage.getAmount() >= Objects.requireNonNull(stats.get(DefaultEntityStatTypes.getHealth())).get()) {
			Player killer = null;
			if(damage.getSource() instanceof Player) {
				killer = (Player) damage.getSource();
			}

			killPlayer(killer, player);

			damage.setCancelled(true);
			return;
		}

		TransformComponent transform = store.getComponent(playerRef, TransformComponent.getComponentType());
		if(transform == null) return;

		Vector3d position = transform.getPosition();

		if(!SpawnManager.SPAWN_REGION.contains(position)) return;

		damage.setCancelled(true);
	}

	public void killPlayer(@Nullable Player killer, Player victim) {
		SpawnManager.preparePlayer(victim);
		SpawnManager.teleportToSpawn(victim);
		String message = killer == null ? "You were killed!" : "You were killed by " + killer.getDisplayName() + "!";
		victim.sendMessage(Message.raw(message).color(Color.RED));

		if(killer == null) return;
		killer.sendMessage(Message.raw("You killed " + victim.getDisplayName() + "!").color(Color.GREEN));

		//TODO: Give kill rewards
	}

	@Nullable
	@Override
	public Query<EntityStore> getQuery() {
		return PlayerRef.getComponentType();
	}

	@NonNullDecl
	@Override
	public Set<Dependency<EntityStore>> getDependencies() {
		return Collections.singleton(RootDependency.first());
	}
}
