package dev.kyriji.controllers.UI;

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
import dev.kyriji.controllers.PlayerManager;
import dev.kyriji.objects.PitPlayer;

import javax.annotation.Nonnull;

public class ShopUI extends InteractiveCustomUIPage<ShopUI.ShopUIData> {

	public ShopUI(@Nonnull PlayerRef playerRef) {
		super(playerRef, CustomPageLifetime.CanDismiss, ShopUIData.CODEC);
	}

	@Override
	public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
		uiCommandBuilder.append("Shop.ui");

		try {
			if (PlayerManager.isLoaded(playerRef.getUuid())) {
				PitPlayer pitPlayer = PlayerManager.getPitPlayer(playerRef.getUuid());
				double gold = pitPlayer.getGold();
				uiCommandBuilder.set("#GoldAmount.Text", String.format("%.0fg", gold));
			} else {
				uiCommandBuilder.set("#GoldAmount.Text", "0g");
			}
		} catch (Exception e) {
			e.printStackTrace();
			uiCommandBuilder.set("#GoldAmount.Text", "Error");
		}

		// Set gold icon
		uiCommandBuilder.set("#GoldIcon.ItemId", "Ingredient_Bar_Gold");
		uiCommandBuilder.set("#GoldIcon.Visible", true);

		// Set item icons
		uiCommandBuilder.set("#Item1 #ItemIcon.ItemId", "Weapon_Sword_Adamantite");
		uiCommandBuilder.set("#Item1 #ItemIcon.Visible", true);

		uiCommandBuilder.set("#Item2 #ItemIcon.ItemId", "Weapon_Sword_Mithril");
		uiCommandBuilder.set("#Item2 #ItemIcon.Visible", true);

		uiCommandBuilder.set("#Item3 #ItemIcon.ItemId", "Potion_Health_Greater");
		uiCommandBuilder.set("#Item3 #ItemIcon.Visible", true);

		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton",
			EventData.of("Action", "Close"));

		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CategoryWeapons",
			EventData.of("Action", "CategoryWeapons"));
		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CategoryArmor",
			EventData.of("Action", "CategoryArmor"));
		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CategoryItems",
			EventData.of("Action", "CategoryItems"));

		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BuyButton1",
			EventData.of("Action", "BuyItem1"));
		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BuyButton2",
			EventData.of("Action", "BuyItem2"));
		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BuyButton3",
			EventData.of("Action", "BuyItem3"));
	}

	@Override
	public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ShopUIData data) {
		if (data.action == null) {
			return;
		}

		switch (data.action) {
			case "Close":
				close();
				break;

			case "CategoryWeapons":
			case "CategoryArmor":
			case "CategoryItems":
				rebuild();
				break;

			case "BuyItem1":
				handleItemPurchase(ref, store, 1);
				break;

			case "BuyItem2":
				handleItemPurchase(ref, store, 2);
				break;

			case "BuyItem3":
				handleItemPurchase(ref, store, 3);
				break;
		}
	}

	private void handleItemPurchase(Ref<EntityStore> ref, Store<EntityStore> store, int itemId) {
		if (!PlayerManager.isLoaded(playerRef.getUuid())) {
			return;
		}

		try {
			PitPlayer pitPlayer = PlayerManager.getPitPlayer(playerRef.getUuid());
			int itemPrice = getItemPrice(itemId);

			if (pitPlayer.getGold() >= itemPrice) {
				pitPlayer.addGold(-itemPrice);
				rebuild();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getItemPrice(int itemId) {
		return switch (itemId) {
			case 1 -> 100;
			case 2 -> 500;
			case 3 -> 50;
			default -> 0;
		};
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
