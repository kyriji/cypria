package dev.kyriji.objects;

import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import dev.kyriji.controllers.EnderChestWindow;
import dev.kyriji.data.PitPlayerData;
import dev.kyriji.utils.PlayerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PitPlayer {
	public final UUID uuid;
	public final PitPlayerData data;

	public Map<UUID, DamageEntry> damageMap;
	public int currentStreak;

	public EnderChestWindow enderChest;
	public ItemContainer inventoryStorage;
	public ItemContainer hotbarStorage;
	public ItemContainer armorStorage;

	public PitPlayer(UUID uuid, PitPlayerData data) {
		this.uuid = uuid;
		this.data = data;
		this.damageMap = new HashMap<>();
		this.currentStreak = 0;
	}

	public void onPlayerReady(Player player) {
		if (player.getUuid() != this.uuid) return;

		this.enderChest = new EnderChestWindow(player, data.getEnderChestData());
		this.inventoryStorage = ItemContainer.CODEC.decode(data.getInventoryData(), new ExtraInfo());
		this.hotbarStorage = ItemContainer.CODEC.decode(data.getHotbarData(), new ExtraInfo());
		this.armorStorage = ItemContainer.CODEC.decode(data.getArmorData(), new ExtraInfo());

		player.getInventory().getStorage().clear();
		this.inventoryStorage.forEach((short slot, ItemStack itemStack) -> {
			player.getInventory().getStorage().setItemStackForSlot(slot, itemStack);
		});

		player.getInventory().getHotbar().clear();
		this.hotbarStorage.forEach((short slot, ItemStack itemStack) -> {
			player.getInventory().getHotbar().setItemStackForSlot(slot, itemStack);
		});

		player.getInventory().getArmor().clear();
		this.armorStorage.forEach((short slot, ItemStack itemStack) -> {
			player.getInventory().getArmor().setItemStackForSlot(slot, itemStack);
		});
	}

	public void addKill() {
		data.addKill();
		currentStreak++;
		if (currentStreak > data.getHighestStreak()) {
			data.setHighestStreak(currentStreak);
		}
	}

	public void addAssist() {
		data.addAssist();
	}

	public void addDeath() {
		data.addDeath();
		currentStreak = 0;
	}

	public void addGold(double amount) {
		data.addGold(amount);
	}

	public int getKills() {
		return data.getKills();
	}

	public int getAssists() {
		return data.getAssists();
	}

	public int getDeaths() {
		return data.getDeaths();
	}

	public double getGold() {
		return data.getGold();
	}

	public String getSelectedKit() {
		return data.getSelectedKit();
	}

	public void setSelectedKit(String selectedKit) {
		data.setSelectedKit(selectedKit);
	}

	public int getCurrentStreak() {
		return currentStreak;
	}

	public int getHighestStreak() {
		return data.getHighestStreak();
	}

	public void save() {
		var codec = ItemContainer.CODEC;
		data.setEnderChestData(enderChest.getContentsAsBson());

		PlayerUtils.getPlayerFromUUID(uuid).thenAccept(player -> {
			Inventory inventory = player.getInventory();

			data.setInventoryData(codec.encode(Kit.clearContainerClone(inventory.getStorage()), new ExtraInfo()));
			data.setHotbarData(codec.encode(Kit.clearContainerClone(inventory.getHotbar()), new ExtraInfo()));
			data.setArmorData(codec.encode(Kit.clearContainerClone(inventory.getArmor()), new ExtraInfo()));
		}).thenAccept(_void -> data.save()).orTimeout(3, TimeUnit.SECONDS).exceptionally(ex -> {
			throw new RuntimeException("Failed to save PitPlayer data for player " + uuid, ex);
		});
	}
}
