package dev.kyriji.commonmc.cypria;

import dev.kyriji.bigminecraftapi.controllers.NetworkManager;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.models.CypriaInstance;
import dev.kyriji.commonmc.cypria.command.CommandModule;
import dev.kyriji.commonmc.cypria.controllers.ModuleManager;
import dev.kyriji.commonmc.cypria.item.ItemModule;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import dev.kyriji.commonmc.cypria.misc.FontUtils;
import dev.kyriji.commonmc.cypria.playerdata.PlayerDataModule;
import org.bukkit.plugin.java.JavaPlugin;

public class CypriaMinecraft {
	public static JavaPlugin plugin;

	public static ModuleManager moduleManager;

	public static CypriaInstance cypriaInstance;

	public static void init(JavaPlugin plugin) {
		CypriaMinecraft.plugin = plugin;

		AUtil.log("initializing CypriaCommonMinecraft");

		FontUtils.initFont();

		moduleManager = new ModuleManager();
		registerModules();

		cypriaInstance = new CypriaInstance(NetworkManager.getIPAddress(), CypriaCommon.getDeployment());
		CypriaCommon.getRedisManager().registerInstance(cypriaInstance);

		AUtil.log("CypriaCommonMinecraft initialized");
	}

	private static void registerModules() {
		moduleManager.registerModule(new ItemModule());
		moduleManager.registerModule(new CommandModule());
		moduleManager.registerModule(new PlayerDataModule());
	}

	public static void shutdown() {
		cypriaInstance.remove();
		moduleManager.shutdown();
	}
}