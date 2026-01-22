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
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.Main;
import dev.kyriji.controllers.PlayerManager;
import dev.kyriji.controllers.GameManager;
import dev.kyriji.objects.DamageEntry;
import dev.kyriji.objects.PitPlayer;
import dev.kyriji.utils.PlayerUtils;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DamageSystem extends DamageEventSystem {
	public static final int ASSIST_TIMEFRAME_SECONDS = 30;
	public static final int BASE_GOLD_REWARD = 10;

	@Override
	public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull Damage damage) {
		Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(i);
		Player player = store.getComponent(playerRef, Player.getComponentType());
		if(player == null) return;

		PitPlayer pitPlayer = PlayerManager.getPitPlayer(player);

		if(player.getWorld() != GameManager.PIT) return;

		EntityStatMap stats = store.getComponent(playerRef, EntityStatMap.getComponentType());
		if(stats == null) return;

		Player attacker = null;
		if(damage.getSource() instanceof Damage.EntitySource entitySource) {
			Ref<EntityStore> entityRef = entitySource.getRef();
			Player attackerPlayer = store.getComponent(entityRef, Player.getComponentType());

			if(attackerPlayer != null) attacker = attackerPlayer;
		}

		UUID key = attacker == null ? null : PlayerUtils.getPlayerRef(attacker).getUuid();
		DamageEntry entry = pitPlayer.damageMap.getOrDefault(key, new DamageEntry(damage.getAmount()));

		entry.addDamage(damage.getAmount());
		entry.updateTimestamp();

		pitPlayer.damageMap.put(key, entry);

		if(damage.getAmount() >= Objects.requireNonNull(stats.get(DefaultEntityStatTypes.getHealth())).get()) {
			killPlayer(attacker, player);

			damage.setCancelled(true);
			return;
		}

		TransformComponent transform = store.getComponent(playerRef, TransformComponent.getComponentType());
		if(transform == null) return;

		Vector3d position = transform.getPosition();

		if(!GameManager.SPAWN_REGION.contains(position)) return;

		damage.setCancelled(true);
	}

	public void killPlayer(@Nullable Player killer, Player victim) {
		PitPlayer victimPitPlayer = PlayerManager.getPitPlayer(victim);

		GameManager.preparePlayer(victim);
		GameManager.teleportToSpawn(victim, true);
		String message = killer == null ? "You were killed!" : "You were killed by " + killer.getDisplayName() + "!";
		victim.sendMessage(Message.raw(message).color(Color.RED));

		calculateAssists(victimPitPlayer, victim, killer);

		victimPitPlayer.addDeath();
		victimPitPlayer.damageMap.clear();

		if(killer == null) return;
		PitPlayer killerPitPlayer = PlayerManager.getPitPlayer(killer);

		killer.sendMessage(Message.raw("You killed " + victim.getDisplayName() + "!").color(Color.GREEN));

		ItemStack potion = new ItemStack("Potion_Health_Greater");
		killer.getInventory().getHotbar().addItemStack(potion);

		killerPitPlayer.addKill();
		killerPitPlayer.addGold(BASE_GOLD_REWARD);
	}

	public void calculateAssists(PitPlayer victimPitPlayer, Player victim, Player killer) {
		float totalDamage = 0f;
		for(DamageEntry value : victimPitPlayer.damageMap.values()) {
			if (value.getTimestamp() < System.currentTimeMillis() - (ASSIST_TIMEFRAME_SECONDS * 1000)) continue;
			totalDamage += value.getDamageAmount();
		}

		final float finalTotalDamage = totalDamage;

		victimPitPlayer.damageMap.forEach((key, value) -> {
			if (key == null) return;

			PlayerUtils.getPlayerFromUUID(key).thenAccept(player -> {
				if (player == null || player == killer) return;
				if (value.getTimestamp() < System.currentTimeMillis() - (ASSIST_TIMEFRAME_SECONDS * 1000)) return;

				PitPlayer assistPitPlayer = PlayerManager.getPitPlayer(key);
				double damageFraction = value.getDamageAmount() / finalTotalDamage;

				assistPitPlayer.addAssist();
				assistPitPlayer.addGold(BASE_GOLD_REWARD * damageFraction);

				player.sendMessage(Message.raw("You assisted in killing " + victim.getDisplayName() + "!")
					.color(Color.YELLOW)
					.insert(Message.raw(String.format(" (%.1f%% of damage)", damageFraction * 100)).color(Color.GRAY))
				);
			});

		});
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
