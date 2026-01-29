package dev.kyriji.controllers;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.shape.Box2D;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.packets.player.JoinWorld;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import dev.kyriji.ui.ShopUI;
import dev.kyriji.systems.BlockBreakSystem;
import dev.kyriji.systems.BlockDamageSystem;
import dev.kyriji.systems.BlockPlaceSystem;
import dev.kyriji.systems.BlockUseSystem;
import dev.kyriji.systems.DamageSystem;
import dev.kyriji.systems.ItemDropSystem;
import dev.kyriji.systems.ItemPickupSystem;
import dev.kyriji.systems.PlayerSpawnedSystem;
import dev.kyriji.objects.PitNPC;
import dev.kyriji.utils.PlayerUtils;
import dev.kyriji.utils.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GameManager {

	public static final World PIT = Universe.get().getWorld("pit");
	public static final Vector3d SPAWN_POINT = new Vector3d(377.5, 139, 297);
	public static final Region SPAWN_REGION = new Region(
			new Vector3d(362, 137, 277),
			new Vector3d(392, 165, 303)
	);

	public static List<PitNPC> spawnedNPCs;
	private boolean npcsSpawned = false;

	public GameManager(JavaPlugin plugin) {
		assert PIT != null;
		PIT.getWorldConfig().getChunkConfig().setKeepLoadedRegion(new Box2D(250, 150, 500, 400));

		plugin.getEntityStoreRegistry().registerSystem(new BlockBreakSystem());
		plugin.getEntityStoreRegistry().registerSystem(new BlockDamageSystem());
		plugin.getEntityStoreRegistry().registerSystem(new BlockPlaceSystem());
		plugin.getEntityStoreRegistry().registerSystem(new BlockUseSystem());
		plugin.getEntityStoreRegistry().registerSystem(new ItemPickupSystem());
		plugin.getEntityStoreRegistry().registerSystem(new ItemDropSystem());

		plugin.getEntityStoreRegistry().registerSystem(new DamageSystem());
		plugin.getEntityStoreRegistry().registerSystem(new PlayerSpawnedSystem());

		plugin.getEventRegistry().registerGlobal(PlayerReadyEvent.class, event -> {
			if (!npcsSpawned) {
				npcsSpawned = true;
				spawnNPCs();
			}
			preparePlayer(event.getPlayer());
			teleportToSpawn(event.getPlayer(), false);
		});
	}

	public void spawnNPCs() {
		if (PIT == null) return;

		spawnedNPCs = new ArrayList<>();

		PIT.execute(() -> {
			PIT.getEntityStore().getStore().forEachEntityParallel(NPCEntity.getComponentType(),
					(index, archetypeChunk, commandBuffer) -> {
						NPCEntity npc = archetypeChunk.getComponent(index, Objects.requireNonNull(NPCEntity.getComponentType()));
						if (npc == null) return;

						npc.setToDespawn();
					}
			);

			Store<EntityStore> store = PIT.getEntityStore().getStore();

			Vector3d position = new Vector3d(387.5, 139, 293.5);
			Vector3f rotation = new Vector3f(0, 90, 0);

			spawnedNPCs.add(PitNPC.spawn(store, "Slothian_Villager", position, rotation, PIT, playerUuid -> {
				PlayerUtils.getPlayerFromUUID(playerUuid).thenAccept(player -> {
					if (player == null) return;

					Ref<EntityStore> playerRef = player.getReference();
					if (playerRef == null) return;

					player.getPageManager().openCustomPage(playerRef, playerRef.getStore(),
						new ShopUI(PlayerUtils.getPlayerRef(player), itemStack -> {
							// Give the purchased item to the player
							player.getInventory().getHotbar().addItemStack(itemStack);
						}));
				});
			}));
		});
	}

	public static void preparePlayer(Player player) {
		Store<EntityStore> store = Objects.requireNonNull(player.getReference()).getStore();
		EntityStatMap stats = Objects.requireNonNull(store.getComponent(player.getReference(), EntityStatMap.getComponentType()));

		float maxHealth = Objects.requireNonNull(stats.get(DefaultEntityStatTypes.getHealth())).getMax();
		float maxStamina = Objects.requireNonNull(stats.get(DefaultEntityStatTypes.getStamina())).getMax();

		stats.setStatValue(DefaultEntityStatTypes.getHealth(), maxHealth);
		stats.setStatValue(DefaultEntityStatTypes.getStamina(), maxStamina);

		player.getInventory().clear();

		World world = player.getWorld();
		if (world == null) return;

		WorldMapManager wmm = world.getWorldMapManager();
		Map<String, WorldMapManager.MarkerProvider> providers = wmm.getMarkerProviders();
		providers.clear();

		ItemStack sword = new ItemStack("Weapon_Sword_Iron");
		ItemStack bow = new ItemStack("Weapon_Shortbow_Iron");
		ItemStack arrows = new ItemStack("Weapon_Arrow_Crude", 100);

		ItemStack helmet = new ItemStack("Armor_Iron_Head");
		ItemStack chestplate = new ItemStack("Armor_Iron_Chest");
		ItemStack gloves = new ItemStack("Armor_Iron_Hands");
		ItemStack leggings = new ItemStack("Armor_Iron_Legs");

		player.getInventory().getHotbar().addItemStack(sword);
		player.getInventory().getHotbar().addItemStack(bow);

		player.getInventory().getStorage().addItemStack(arrows);

		player.getInventory().getArmor().setItemStackForSlot((short) 0, helmet);
		player.getInventory().getArmor().setItemStackForSlot((short) 1, chestplate);
		player.getInventory().getArmor().setItemStackForSlot((short) 2, gloves);
		player.getInventory().getArmor().setItemStackForSlot((short) 3, leggings);
	}

	public static void teleportToSpawn(Player player, boolean fade) {
		player.getPageManager().clearCustomPageAcknowledgements();
		Store<EntityStore> store = Objects.requireNonNull(player.getReference()).getStore();

		if(fade) {
			JoinWorld packet = new JoinWorld(false, true, Objects.requireNonNull(PIT).getWorldConfig().getUuid());
			PlayerRef playerRef = Objects.requireNonNull(store.getComponent(player.getReference(), PlayerRef.getComponentType()));

			PacketHandler packetHandler = playerRef.getPacketHandler();

			packetHandler.write(packet);
			packetHandler.tryFlush();
			packetHandler.setQueuePackets(true);
		}


		World world = player.getWorld();
		if (world == null) return;

		HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
			if (player.getReference() == null) return;

			world.execute(() -> {
				Teleport teleport = new Teleport(GameManager.PIT, GameManager.SPAWN_POINT, new Vector3f(0, 0, 0));
				store.addComponent(player.getReference(), Teleport.getComponentType(), teleport);
			});
		}, 250, TimeUnit.MILLISECONDS);
	}

	public static void cleanup() {
		spawnedNPCs.forEach(PitNPC::despawn);
	}
}
