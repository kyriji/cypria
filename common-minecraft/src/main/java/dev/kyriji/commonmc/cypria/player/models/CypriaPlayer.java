package dev.kyriji.commonmc.cypria.player.models;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.playerdata.documents.InventoryData;
import dev.kyriji.common.cypria.playerdata.enums.PlayerDataType;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CypriaPlayer {
	private final UUID uuid;
	private Player player;

	// transient data
	// private CachedEquipmentData cachedEquipmentData;

	// persistent data
	 private final InventoryData inventoryData;

	public CypriaPlayer(UUID uuid) {
		this.uuid = uuid;

		System.out.println("Creating CypriaPlayer for " + uuid);
		this.inventoryData = CypriaCommon.getPlayerDataManager().getPlayerData(uuid, PlayerDataType.INVENTORY);
	}

	public UUID getUUID() {
		return uuid;
	}

	public void attachPlayer(Player player) {
		assert !hasPlayer();
		this.player = player;

		loadInventory();
	}

	public CompletableFuture<Void> save() {
		return CompletableFuture.runAsync(this::saveInventory)
		.exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});
	}

	public CompletableFuture<Void> saveInventory() {
		return CompletableFuture.runAsync(() -> {
			new BukkitRunnable() {
				@Override
				public void run() {
					List<byte[]> serializedInventory = new ArrayList<>();

					ItemStack[] contents = player.getInventory().getContents();
					for (@Nullable ItemStack itemStack : contents) {
						if(itemStack == null) serializedInventory.add(null);
						else serializedInventory.add(itemStack.serializeAsBytes());
					}

					inventoryData.setInventory(serializedInventory);
					inventoryData.save();
				}
			}.runTask(CypriaMinecraft.plugin);
		});
	}

	public void loadInventory() {
		List<byte[]> serializedInventory = inventoryData.getInventory();

		for (int i = 0; i < serializedInventory.size(); i++) {
			byte[] serializedItem = serializedInventory.get(i);
			if(serializedItem == null) player.getInventory().setItem(i, null);
			else player.getInventory().setItem(i, ItemStack.deserializeBytes(serializedItem));
		}
	}


	public boolean hasPlayer() {
		return player != null;
	}

	public Player getPlayer() {
		return player;
	}
}
