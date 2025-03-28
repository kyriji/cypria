package dev.kyriji.commonmc.cypria.player;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.model.CypriaModule;
import dev.kyriji.commonmc.cypria.player.controllers.PlayerManager;
import org.bukkit.Bukkit;

public class PlayerModule extends CypriaModule {
	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(new PlayerManager(), CypriaMinecraft.plugin());
	}
}
