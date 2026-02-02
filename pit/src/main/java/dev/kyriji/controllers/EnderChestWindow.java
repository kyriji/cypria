package dev.kyriji.controllers;

import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ContainerWindow;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.bson.BsonDocument;
import org.bson.BsonValue;

public class EnderChestWindow {
	public static int SIZE = 45;

	private final Player player;
	private final ContainerWindow window;
	private final ItemContainer container;

	public EnderChestWindow(Player player, BsonValue contentsBson) {
		this.player = player;

		if (contentsBson == null) {
			this.container = new SimpleItemContainer((short) SIZE);
		} else {
			System.out.println("Loading ender chest contents from Bson: " + contentsBson);
			this.container = ItemContainer.CODEC.decode(contentsBson, new ExtraInfo());
		}

		if (this.container == null) throw new IllegalStateException("Failed to decode ender chest contents.");
		this.window = new ContainerWindow(this.container);
	}

	public void open() {
		Ref<EntityStore> playerRef = player.getReference();
		if (playerRef == null) return;

		if (window.getId() > 0) {
			try { window.close(playerRef, playerRef.getStore()); } catch (IllegalStateException e) {}
		}

		player.getPageManager().setPageWithWindows(playerRef, playerRef.getStore(), Page.Inventory, true, window);
	}

	public ContainerWindow getWindow() {
		return window;
	}

	public ItemContainer getContainer() {
		return container;
	}

	public ItemStack[] getContents() {
		ItemStack[] contents = new ItemStack[SIZE];

		for(int i = 0; i < SIZE; i++) {
			contents[i] = container.getItemStack((short) i);
		}

		return contents;
	}

	public BsonValue getContentsAsBson() {
		return ItemContainer.CODEC.encode(container, new ExtraInfo());
	}
}
