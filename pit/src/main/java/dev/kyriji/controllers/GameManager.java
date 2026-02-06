package dev.kyriji.controllers;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.shape.Box2D;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.protocol.packets.player.JoinWorld;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
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
import dev.kyriji.objects.PitNPC;
import dev.kyriji.objects.PitPlayer;
import dev.kyriji.systems.*;
import dev.kyriji.ui.ShopUI;
import dev.kyriji.utils.PlayerUtils;
import dev.kyriji.utils.Region;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

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

			World world = event.getPlayer().getWorld();
			if (world != null) {
				world.execute(() -> {
					if (world != PIT) return;

					preparePlayer(event.getPlayer());
					teleportToSpawn(event.getPlayer(), false);
				});
			}
		});

		PacketAdapters.registerInbound((PacketHandler handler, Packet packet) -> {
			if(packet instanceof SyncInteractionChains interactionPacket) {
				boolean open = false;

				for(SyncInteractionChain update : interactionPacket.updates) {
					if(update.interactionType != InteractionType.Use) return;

					if(update.data == null || update.data.blockPosition == null) return;

					Vector3i blockPos = new Vector3i(
							update.data.blockPosition.x,
							update.data.blockPosition.y,
							update.data.blockPosition.z
					);

					BlockType block = GameManager.PIT.getBlockType(blockPos);

					if (block == null) return;
					String id = Objects.requireNonNull(block.getItem()).getBlockId();

					if (id.equals("Furniture_Temple_Dark_Chest_Small")) open = true;
				}

				if(open) {
					UUID uuid = Objects.requireNonNull(handler.getAuth()).getUuid();
					PitPlayer targetPitPlayer = PlayerDataManager.getPitPlayer(uuid);

					PIT.execute(() -> targetPitPlayer.enderChest.open());
				}
			}
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
		EntityStatMap stats = store.getComponent(player.getReference(), EntityStatMap.getComponentType());

		if (stats == null) return;

		var healthStat = stats.get(DefaultEntityStatTypes.getHealth());
		if (healthStat == null) return;
		float maxHealth = healthStat.getMax();

		var staminaStat = stats.get(DefaultEntityStatTypes.getStamina());
		if (staminaStat == null) return;
		float maxStamina = staminaStat.getMax();

		stats.setStatValue(DefaultEntityStatTypes.getHealth(), maxHealth);
		stats.setStatValue(DefaultEntityStatTypes.getStamina(), maxStamina);

		World world = player.getWorld();
		if (world == null) return;

		WorldMapManager wmm = world.getWorldMapManager();
		Map<String, WorldMapManager.MarkerProvider> providers = wmm.getMarkerProviders();
		providers.clear();

		ItemStack helmet = new ItemStack("Armor_Iron_Head");
		ItemStack chestplate = new ItemStack("Armor_Iron_Chest");
		ItemStack gloves = new ItemStack("Armor_Iron_Hands");
		ItemStack leggings = new ItemStack("Armor_Iron_Legs");

		String[] armorNames = {"head", "chest", "hands", "legs"};
		ItemStack[] armorItems = {helmet, chestplate, gloves, leggings};

		for(short i = 0; i < 4; i++) {
			ItemStack armorItem = player.getInventory().getArmor().getItemStack(i);

			if (armorItem != null && armorItem.getItem().getId().toLowerCase().contains(armorNames[i])) continue;
			player.getInventory().getArmor().setItemStackForSlot(i, armorItems[i]);
		}

		List<Map.Entry<ItemStack, Boolean>> ensureList = new ArrayList<>();
		ensureList.add(new AbstractMap.SimpleEntry<>(new ItemStack("Pit_Sword"), false));
		ensureList.add(new AbstractMap.SimpleEntry<>(new ItemStack("Pit_Bow"), false));
		ensureList.add(new AbstractMap.SimpleEntry<>(new ItemStack("Weapon_Arrow_Crude", 100), false));

		Stream.of(player.getInventory().getHotbar(), player.getInventory().getStorage()).forEach(inv ->
				inv.forEach((slot, itemStack) -> {
					for (Map.Entry<ItemStack, Boolean> entry : ensureList) {

						if (!itemStack.getItem().getId().equals(entry.getKey().getItem().getId())) continue;

						if (itemStack.getQuantity() < entry.getKey().getQuantity()) {
							inv.setItemStackForSlot(slot, itemStack.withQuantity(entry.getKey().getQuantity()));
						}

						entry.setValue(true);
						return;
					}
				})
		);

		ensureList.forEach(entry -> {
			if (!entry.getValue()) player.getInventory().getHotbar().addItemStack(entry.getKey());
		});
	}

	public static void teleportToSpawn(Player player, boolean fade) {
		player.getPageManager().clearCustomPageAcknowledgements();
		Store<EntityStore> store = Objects.requireNonNull(player.getReference()).getStore();

		if(fade) {
			JoinWorld packet = new JoinWorld(false, true, Objects.requireNonNull(PIT).getWorldConfig().getUuid());
			PlayerRef playerRef = PlayerUtils.getPlayerRef(player);

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
		if (spawnedNPCs != null) spawnedNPCs.forEach(PitNPC::despawn);
	}
}
