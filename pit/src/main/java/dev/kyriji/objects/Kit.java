package dev.kyriji.objects;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Kit {
	public static final KeyedCodec<String> KIT_TAG = new KeyedCodec<>("Cypria:kit_id", Codec.STRING);

	private final String name;

	private final KitArmor armor;
	private final KitUtility utility;
	private final List<ItemStack> hotbar;
	private final List<ItemStack> storage;

	public Kit(String name) {
		this.name = name;

		this.armor = getArmor();
		this.utility = getUtility();

		this.hotbar = getHotbar().stream().map(this::processItem).toList();
		this.storage = getStorage().stream().map(this::processItem).toList();
	}

	public abstract KitArmor getArmor();

	public abstract KitUtility getUtility();

	public abstract List<ItemStack> getHotbar();

	public abstract List<ItemStack> getStorage();

	public void equip(Player player) {
		Inventory inventory = player.getInventory();
		clearItems(player);

		this.hotbar.forEach(inventory.getHotbar()::addItemStack);
		this.storage.forEach(inventory.getStorage()::addItemStack);

		for(short i = 0; i < 4; i++) {
			if (inventory.getArmor().getItemStack(i) == null)
				inventory.getArmor().setItemStackForSlot(i, armor.toArray()[i]);

			if (inventory.getUtility().getItemStack(i) == null)
				inventory.getUtility().setItemStackForSlot(i, utility.utilityItems.size() > i ? utility.utilityItems.get(i) : ItemStack.EMPTY);
		}

		inventory.setActiveUtilitySlot((byte) 0);
		inventory.setActiveHotbarSlot((byte) 0);
	}

	public static void clearItems(Player player) {
		Inventory inventory = player.getInventory();

		clearContainer(inventory.getHotbar());
		clearContainer(inventory.getStorage());
		clearContainer(inventory.getArmor());
		clearContainer(inventory.getUtility());
	}

	private static ItemContainer clearContainer(ItemContainer container) {
		container.forEach((slot, itemStack) -> {
			if (itemStack.getFromMetadataOrNull(KIT_TAG) != null) container.setItemStackForSlot(slot, ItemStack.EMPTY);
		});

		return container;
	}

	public static ItemContainer clearContainerClone(ItemContainer container) {
		ItemContainer clone = container.clone();

		return clearContainer(clone);
	}

	private ItemStack processItem(ItemStack itemStack) {
		return shouldTag(itemStack) ? itemStack.withMetadata(KIT_TAG, name) : itemStack;
	}

	protected boolean shouldTag(ItemStack itemStack) {
		return !itemStack.getItemId().contains("Arrow");
	}

	public String getName() {
		return name;
	}

	public String getIconItemId() {
		return hotbar.isEmpty() ? "Pit_Sword" : hotbar.get(0).getItemId();
	}

	public class KitArmor {
		public ItemStack helmet;
		public ItemStack chestplate;
		public ItemStack gloves;
		public ItemStack leggings;

		public KitArmor(ItemStack helmet, ItemStack chestplate, ItemStack gloves, ItemStack leggings) {
			this.helmet = processItem(helmet);
			this.chestplate = processItem(chestplate);
			this.gloves = processItem(gloves);
			this.leggings = processItem(leggings);
		}

		public ItemStack[] toArray() {
			return new ItemStack[]{helmet, chestplate, gloves, leggings};
		}
	}

	public class KitUtility {
		public List<ItemStack> utilityItems = new ArrayList<>();

		public KitUtility(ItemStack... utilityItems) {
			if (utilityItems.length > 4) throw new IllegalArgumentException("Cannot have more than 4 utility items");

			this.utilityItems.addAll(Stream.of(utilityItems).map(Kit.this::processItem).toList());
		}
	}
}
