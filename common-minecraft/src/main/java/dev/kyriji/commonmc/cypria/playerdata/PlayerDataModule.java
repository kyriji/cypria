package dev.kyriji.commonmc.cypria.playerdata;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.model.CypriaModule;
import dev.kyriji.commonmc.cypria.playerdata.listeners.ServerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataModule extends CypriaModule {

	@Override
	public void init() {
		registerListeners();
	}

	private void registerListeners() {
		JavaPlugin plugin = CypriaMinecraft.plugin;

		plugin.getServer().getPluginManager().registerEvents(new ServerListener(), plugin);
	}
}
