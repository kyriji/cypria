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
import dev.kyriji.objects.Kit;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

//AI SLOP
public class KitUI extends InteractiveCustomUIPage<KitUI.KitUIData> {

	private static final int MAX_SLOTS = 10;

	private final List<Kit> kits;
	private final Consumer<Kit> onSelect;

	public KitUI(@Nonnull PlayerRef playerRef, @Nonnull List<Kit> kits, @Nonnull Consumer<Kit> onSelect) {
		super(playerRef, CustomPageLifetime.CanDismiss, KitUIData.CODEC);
		this.kits = kits;
		this.onSelect = onSelect;
	}

	@Override
	public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
		uiCommandBuilder.append("KitSelect.ui");

		for (int i = 0; i < kits.size() && i < MAX_SLOTS; i++) {
			Kit kit = kits.get(i);
			String prefix = "#Kit" + i;

			uiCommandBuilder.set(prefix + ".Visible", true);
			uiCommandBuilder.set(prefix + " #KitName.Text", kit.getName());
			uiCommandBuilder.set(prefix + " #KitIcon.ItemId", kit.getIconItemId());
			uiCommandBuilder.set(prefix + " #KitIcon.Visible", true);

			uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#SelectButton" + i,
					EventData.of("Action", "SelectKit" + i));
		}

		for (int i = kits.size(); i < MAX_SLOTS; i++) {
			uiCommandBuilder.set("#Kit" + i + ".Visible", false);
		}

		uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton",
				EventData.of("Action", "Close"));
	}

	@Override
	public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull KitUIData data) {
		if (data.action == null) return;

		if ("Close".equals(data.action)) {
			close();
			return;
		}

		if (data.action.startsWith("SelectKit")) {
			try {
				int index = Integer.parseInt(data.action.substring(9));
				if (index >= 0 && index < kits.size()) {
					onSelect.accept(kits.get(index));
					close();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	public static class KitUIData {
		public static final BuilderCodec<KitUIData> CODEC =
				BuilderCodec.builder(KitUIData.class, KitUIData::new)
						.append(new KeyedCodec<>("Action", Codec.STRING),
								(d, v) -> d.action = v, d -> d.action)
						.add()
						.build();

		public String action;
	}
}
