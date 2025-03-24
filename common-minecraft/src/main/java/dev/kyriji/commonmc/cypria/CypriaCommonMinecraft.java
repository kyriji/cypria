package dev.kyriji.commonmc.cypria;

import dev.kyriji.commonmc.cypria.command.CommandModule;
import dev.kyriji.commonmc.cypria.controllers.ModuleManager;
import dev.kyriji.commonmc.cypria.item.ItemModule;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import dev.kyriji.commonmc.cypria.misc.FontUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class CypriaCommonMinecraft {
	public static JavaPlugin plugin;

	public static ModuleManager moduleManager;

	public static void init(JavaPlugin plugin) {
		CypriaCommonMinecraft.plugin = plugin;
		AUtil.log("initializing CypriaCommonMinecraft");

		FontUtils.initFont();

		moduleManager = new ModuleManager();
		registerModules();

		AUtil.log("CypriaCommonMinecraft initialized");
	}

	private static void registerModules() {
		moduleManager.registerModule(new ItemModule());
		moduleManager.registerModule(new CommandModule());
	}
}