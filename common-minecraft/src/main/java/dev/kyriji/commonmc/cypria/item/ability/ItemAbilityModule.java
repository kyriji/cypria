package dev.kyriji.commonmc.cypria.item.ability;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.item.ability.controllers.ItemAbilityManager;
import dev.kyriji.commonmc.cypria.model.CypriaModule;
import org.bukkit.Bukkit;

public class ItemAbilityModule extends CypriaModule {
	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(new ItemAbilityManager(), CypriaMinecraft.plugin);
	}
}
