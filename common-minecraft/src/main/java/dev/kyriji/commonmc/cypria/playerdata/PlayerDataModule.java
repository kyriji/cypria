package dev.kyriji.commonmc.cypria.playerdata;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.command.commands.dev.DevMultiCommand;
import dev.kyriji.commonmc.cypria.command.commands.dev.ItemCommand;
import dev.kyriji.commonmc.cypria.command.commands.dev.TestCommand;
import dev.kyriji.commonmc.cypria.command.controllers.CommandManager;
import dev.kyriji.commonmc.cypria.command.models.CypriaMultiCommand;
import dev.kyriji.commonmc.cypria.model.CypriaModule;
import dev.kyriji.commonmc.cypria.playerdata.listeners.PlayerListener;
import dev.kyriji.commonmc.cypria.playerdata.listeners.ServerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataModule extends CypriaModule {

	@Override
	public void init() {
		registerListeners();
	}

	private void registerListeners() {
		JavaPlugin plugin = CypriaMinecraft.plugin;

		plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(), plugin);
	}
}
