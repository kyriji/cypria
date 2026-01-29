package dev.kyriji.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import dev.kyriji.controllers.PlayerDataManager;
import dev.kyriji.objects.PitPlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShopUI extends InteractiveCustomUIPage<ShopUI.ShopUIData> {

	private static class ShopItem {
		String name;
		String description;
		String iconItemId;
		int price;
		String category;
		ItemStack itemStack;

		ShopItem(String name, String description, String iconItemId, int price, String category, ItemStack itemStack) {
			this.name = name;
			this.description = description;
			this.iconItemId = iconItemId;
			this.price = price;
			this.category = category;
			this.itemStack = itemStack;
		}
	}

	private final List<ShopItem> allItems = new ArrayList<>();
	private String currentCategory = "Weapons";
	private final Consumer<ItemStack> onPurchase;

	private void initializeShopItems() {
		allItems.clear();
		// Weapons
		allItems.add(new ShopItem("Adamantite Sword", "A reliable sword", "Weapon_Sword_Adamantite", 100, "Weapons",
			new ItemStack("Weapon_Sword_Adamantite")));
		allItems.add(new ShopItem("Mithril Sword", "A powerful weapon", "Weapon_Sword_Mithril", 500, "Weapons",
			new ItemStack("Weapon_Sword_Mithril")));
		// Armor
		allItems.add(new ShopItem("Adamantite Helmet", "Basic head protection", "Armor_Adamantite_Head", 75, "Armor",
			new ItemStack("Armor_Adamantite_Head")));
		allItems.add(new ShopItem("Adamantite Chestplate", "Basic chest protection", "Armor_Adamantite_Chest", 100, "Armor",
			new ItemStack("Armor_Adamantite_Chest")));
		allItems.add(new ShopItem("Adamantite Gloves", "Basic glove protection", "Armor_Adamantite_Hands", 90, "Armor",
			new ItemStack("Armor_Adamantite_Hands")));
		allItems.add(new ShopItem("Adamantite Leggings", "Basic leg protection", "Armor_Adamantite_Legs", 90, "Armor",
			new ItemStack("Armor_Adamantite_Legs")));

		// Items
		allItems.add(new ShopItem("Healing Potion", "Restores health", "Potion_Health_Greater", 50, "Items",
			new ItemStack("Potion_Health_Greater")));
		allItems.add(new ShopItem("Lesser Healing Potion", "Restores health", "Potion_Health_Lesser", 25, "Items",
			new ItemStack("Potion_Health_Lesser")));
		allItems.add(new ShopItem("Greater Stamina Potion", "Restores stamina", "Potion_Stamina_Greater", 50, "Items",
			new ItemStack("Potion_Stamina_Greater")));
		allItems.add(new ShopItem("Lesser Stamina Potion", "Restores stamina", "Potion_Stamina_Lesser", 25, "Items",
			new ItemStack("Potion_Stamina_Lesser")));
	}

	private List<ShopItem> getFilteredItems() {
		List<ShopItem> filtered = new ArrayList<>();
		for (ShopItem item : allItems) {
			if (item.category.equals(currentCategory)) {
				filtered.add(item);
			}
		}
		return filtered;
	}


	public ShopUI(@Nonnull PlayerRef playerRef, @Nonnull Consumer<ItemStack> onPurchase) {
		super(playerRef, CustomPageLifetime.CanDismiss, ShopUIData.CODEC);
		this.onPurchase = onPurchase;
	}

	@Override
	public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
		initializeShopItems();

		uiCommandBuilder.append("Shop.ui");

		// Set gold display
		try {
			if (PlayerDataManager.isLoaded(playerRef.getUuid())) {
				PitPlayer pitPlayer = PlayerDataManager.getPitPlayer(playerRef.getUuid());
				double gold = pitPlayer.getGold();
				uiCommandBuilder.set("#GoldAmount.Text", String.format("%.0fg", gold));
			} else {
				uiCommandBuilder.set("#GoldAmount.Text", "0g");
			}
		} catch (Exception e) {
			e.printStackTrace();
			uiCommandBuilder.set("#GoldAmount.Text", "Error");
		}

		uiCommandBuilder.set("#GoldIcon.ItemId", "Ingredient_Bar_Gold");
		uiCommandBuilder.set("#GoldIcon.Visible", true);

		// Get player's gold for affordability check
		double playerGold = 0;
		try {
			if (PlayerDataManager.isLoaded(playerRef.getUuid())) {
				PitPlayer pitPlayer = PlayerDataManager.getPitPlayer(playerRef.getUuid());
				playerGold = pitPlayer.getGold();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Dynamically populate items (support up to 10 slots)
		List<ShopItem> items = getFilteredItems();
		int maxSlots = 10;
		for (int i = 0; i < items.size() && i < maxSlots; i++) {
			ShopItem item = items.get(i);
			String itemPrefix = "#Item" + i;
			boolean canAfford = playerGold >= item.price;

			// Show the item slot
			uiCommandBuilder.set(itemPrefix + ".Visible", true);

			// Set item data
			uiCommandBuilder.set(itemPrefix + " #ItemName.Text", item.name);
			uiCommandBuilder.set(itemPrefix + " #ItemDescription.Text", item.description);
			uiCommandBuilder.set(itemPrefix + " #ItemPrice.Text", item.price + "g");
			uiCommandBuilder.set(itemPrefix + " #ItemIcon.ItemId", item.iconItemId);
			uiCommandBuilder.set(itemPrefix + " #ItemIcon.Visible", true);

			// Set button enabled state based on affordability
//			uiCommandBuilder.set(itemPrefix + " #BuyButton" + i + ".Enabled", canAfford);
		}

		// Hide unused item slots
		for (int i = items.size(); i < maxSlots; i++) {
			uiCommandBuilder.set("#Item" + i + ".Visible", false);
		}

		// Event bindings
		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton",
			EventData.of("Action", "Close"));

		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CategoryWeapons",
			EventData.of("Action", "CategoryWeapons"));
		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CategoryArmor",
			EventData.of("Action", "CategoryArmor"));
		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CategoryItems",
			EventData.of("Action", "CategoryItems"));

		for (int i = 0; i < items.size() && i < maxSlots; i++) {
			uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BuyButton" + i,
				EventData.of("Action", "BuyItem" + i));
		}
	}

	@Override
	public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ShopUIData data) {
		if (data.action == null) {
			return;
		}

		if ("Close".equals(data.action)) {
			close();
			return;
		}

		if (data.action.equals("CategoryWeapons")) {
			currentCategory = "Weapons";
			rebuild();
			return;
		}
		if (data.action.equals("CategoryArmor")) {
			currentCategory = "Armor";
			rebuild();
			return;
		}
		if (data.action.equals("CategoryItems")) {
			currentCategory = "Items";
			rebuild();
			return;
		}

		// Handle BuyItem0, BuyItem1, BuyItem2, etc.
		if (data.action.startsWith("BuyItem")) {
			try {
				int itemIndex = Integer.parseInt(data.action.substring(7));
				handleItemPurchase(ref, store, itemIndex);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleItemPurchase(Ref<EntityStore> ref, Store<EntityStore> store, int itemIndex) {
		if (!PlayerDataManager.isLoaded(playerRef.getUuid())) {
			return;
		}

		List<ShopItem> items = getFilteredItems();
		if (itemIndex < 0 || itemIndex >= items.size()) {
			return;
		}

		try {
			PitPlayer pitPlayer = PlayerDataManager.getPitPlayer(playerRef.getUuid());
			ShopItem item = items.get(itemIndex);

			if (pitPlayer.getGold() >= item.price) {
				// Deduct gold
				pitPlayer.addGold(-item.price);

				// Give item to player via consumer
				onPurchase.accept(item.itemStack);

				// Rebuild UI to reflect new gold amount and button states
				rebuild();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class ShopUIData {
		public static final BuilderCodec<ShopUIData> CODEC =
				BuilderCodec.builder(ShopUIData.class, ShopUIData::new)
						.append(new KeyedCodec<>("Action", Codec.STRING),
								(d, v) -> d.action = v, d -> d.action)
						.add()
						.build();

		public String action;
	}
}
