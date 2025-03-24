package dev.kyriji.commonmc.cypria.item;

import dev.kyriji.commonmc.cypria.CypriaCommonMinecraft;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.model.CypriaModule;
import org.bukkit.Bukkit;

public class ItemModule extends CypriaModule {

	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(new ItemManager(), CypriaCommonMinecraft.plugin);
	}
}
