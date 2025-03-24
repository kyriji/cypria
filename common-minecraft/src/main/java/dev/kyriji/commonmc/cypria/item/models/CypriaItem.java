package dev.kyriji.commonmc.cypria.item.models;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.item.enums.ItemID;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class CypriaItem implements Listener {
	public ItemID itemID;

	public boolean hasUUID;

	public CypriaItem(ItemID itemID) {
		this.itemID = itemID;

		Bukkit.getPluginManager().registerEvents(this, CypriaMinecraft.plugin);
	}
}
