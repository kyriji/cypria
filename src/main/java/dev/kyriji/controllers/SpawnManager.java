package dev.kyriji.controllers;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.player.JoinWorld;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
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
import dev.kyriji.controllers.systems.BlockBreakSystem;
import dev.kyriji.controllers.systems.BlockDamageSystem;
import dev.kyriji.controllers.systems.BlockPlaceSystem;
import dev.kyriji.controllers.systems.BlockUseSystem;
import dev.kyriji.controllers.systems.DamageSystem;
import dev.kyriji.controllers.systems.PlayerSpawnedSystem;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SpawnManager {
	private final JavaPlugin plugin;

	public static final World PIT = Universe.get().getWorld("pit");
	public static final Vector3d SPAWN_POINT = new Vector3d(377.5, 139, 297);
	public static final Region SPAWN_REGION = new Region(
			new Vector3d(362, 137, 277),
			new Vector3d(392, 165, 303)
	);

	public SpawnManager(JavaPlugin plugin) {
		this.plugin = plugin;

		plugin.getEntityStoreRegistry().registerSystem(new BlockBreakSystem());
		plugin.getEntityStoreRegistry().registerSystem(new BlockDamageSystem());
		plugin.getEntityStoreRegistry().registerSystem(new BlockPlaceSystem());
		plugin.getEntityStoreRegistry().registerSystem(new BlockUseSystem());

		plugin.getEntityStoreRegistry().registerSystem(new DamageSystem());
		plugin.getEntityStoreRegistry().registerSystem(new PlayerSpawnedSystem());
	}

	public static void preparePlayer(Player player) {
		Store<EntityStore> store = Objects.requireNonNull(player.getReference()).getStore();
		EntityStatMap stats = Objects.requireNonNull(store.getComponent(player.getReference(), EntityStatMap.getComponentType()));

		stats.setStatValue(DefaultEntityStatTypes.getHealth(), 100);
		stats.setStatValue(DefaultEntityStatTypes.getStamina(), 10);

		player.getInventory().clear();

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

	public static void teleportToSpawn(Player player) {
		player.getPageManager().clearCustomPageAcknowledgements();
		JoinWorld packet = new JoinWorld(false, true, Objects.requireNonNull(PIT).getWorldConfig().getUuid());

		Store<EntityStore> store = Objects.requireNonNull(player.getReference()).getStore();
		PlayerRef playerRef = Objects.requireNonNull(store.getComponent(player.getReference(), PlayerRef.getComponentType()));

		PacketHandler packetHandler = playerRef.getPacketHandler();

		packetHandler.write(packet);
		packetHandler.tryFlush();
		packetHandler.setQueuePackets(true);

		World world = player.getWorld();
		if (world == null) return;

		HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
			if (player.getReference() == null) return;

			world.execute(() -> {
				Teleport teleport = new Teleport(SpawnManager.PIT, SpawnManager.SPAWN_POINT, new Vector3f(0, 0, 0));
				store.addComponent(player.getReference(), Teleport.getComponentType(), teleport);
			});
		}, 250, TimeUnit.MILLISECONDS);
	}

}
