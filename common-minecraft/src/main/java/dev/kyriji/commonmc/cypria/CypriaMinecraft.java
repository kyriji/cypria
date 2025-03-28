package dev.kyriji.commonmc.cypria;

import com.onarandombox.MultiverseCore.MultiverseCore;
import dev.kyriji.bigminecraftapi.controllers.NetworkManager;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.models.CypriaInstance;
import dev.kyriji.commonmc.cypria.command.CommandModule;
import dev.kyriji.commonmc.cypria.command.controllers.CommandManager;
import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import dev.kyriji.commonmc.cypria.controllers.ModuleManager;
import dev.kyriji.commonmc.cypria.event.EventsModule;
import dev.kyriji.commonmc.cypria.item.ItemModule;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import dev.kyriji.commonmc.cypria.misc.FontUtils;
import dev.kyriji.commonmc.cypria.model.CypriaModule;
import dev.kyriji.commonmc.cypria.player.PlayerModule;
import dev.kyriji.commonmc.cypria.playerdata.PlayerDataModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CypriaMinecraft {
	private static CypriaMinecraft INSTANCE;

	public JavaPlugin plugin;

	public MultiverseCore mvCore;

	public ModuleManager moduleManager;

	public CypriaInstance cypriaInstance;

	private CypriaMinecraft(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	private void load() {
		mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		FontUtils.initFont();

		moduleManager = new ModuleManager();
		registerModules();

		cypriaInstance = new CypriaInstance(NetworkManager.getIPAddress(), CypriaCommon.getDeployment());
		CypriaCommon.getRedisManager().registerInstance(cypriaInstance);
	}

	private void registerModules() {
		moduleManager.registerModule(new ItemModule());
		moduleManager.registerModule(new CommandModule());
		moduleManager.registerModule(new PlayerDataModule());
		moduleManager.registerModule(new PlayerModule());
		moduleManager.registerModule(new EventsModule());
	}

	public void shutdown() {
		cypriaInstance.remove();
		moduleManager.shutdown();
	}

	public static CypriaMinecraft get() {
		if(INSTANCE == null) throw new IllegalStateException("CypriaMinecraft has not been initialized");
		return INSTANCE;
	}

	public static JavaPlugin plugin() {
		if(INSTANCE == null) throw new IllegalStateException("CypriaMinecraft has not been initialized");
		return INSTANCE.plugin;
	}

	public static class Builder {
		private final JavaPlugin plugin;
		private final List<CypriaModule> modules = new ArrayList<>();
		private final List<CypriaCommand> commands = new ArrayList<>();

		public Builder(JavaPlugin plugin) {
			this.plugin = plugin;
		}

		public Builder addModules(CypriaModule... toAdd) {
			modules.addAll(Arrays.asList(toAdd));
			return this;
		}

		public Builder addCommands(CypriaCommand... toAdd) {
			commands.addAll(Arrays.asList(toAdd));
			return this;
		}

		public CypriaMinecraft build() {
			if(INSTANCE != null) throw new IllegalStateException("CypriaMinecraft has already been initialized");
			AUtil.log("initializing CypriaMinecraft");
			INSTANCE = new CypriaMinecraft(plugin);
			INSTANCE.load();

			modules.forEach(module -> INSTANCE.moduleManager.registerModule(module));
			commands.forEach(CommandManager::registerCommand);

			AUtil.log("CypriaMinecraft initialized");
			return INSTANCE;
		}
	}
}