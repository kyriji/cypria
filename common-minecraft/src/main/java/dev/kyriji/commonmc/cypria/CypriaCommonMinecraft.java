package dev.kyriji.commonmc.cypria;

import dev.kyriji.commonmc.cypria.command.CommandModule;
import dev.kyriji.commonmc.cypria.controllers.ModuleManager;
import dev.kyriji.commonmc.cypria.item.ItemModule;

public class CypriaCommonMinecraft {
	public static ModuleManager moduleManager;

	public static void init() {
		moduleManager = new ModuleManager();
		registerModules();
	}

	private static void registerModules() {
		moduleManager.registerModule(new ItemModule());
		moduleManager.registerModule(new CommandModule());
	}
}