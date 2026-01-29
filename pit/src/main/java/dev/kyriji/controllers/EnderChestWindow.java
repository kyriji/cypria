package dev.kyriji.controllers;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ContainerWindow;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class EnderChestWindow {
	public static int SIZE = 45;

	private final Player player;
	private final ContainerWindow window;
	private final ItemContainer container;

	public EnderChestWindow(Player player) {
		this.player = player;

		this.container = new SimpleItemContainer((short) SIZE);
		this.window = new ContainerWindow(this.container);
	}

	public void open() {
		Ref<EntityStore> playerRef = player.getReference();
		if (playerRef == null) return;

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
}
