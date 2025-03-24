package dev.kyriji.commonmc.cypria.command.controllers;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager {
	public static List<CypriaCommand> commands = new ArrayList<>();

	public static void registerCommand(CypriaCommand command) {
		assert command.isBaseLevel();

		PluginCommand pluginCommand = Objects.requireNonNull(CypriaMinecraft.plugin.getCommand(command.getCommand()));
		pluginCommand.setExecutor(command);
		pluginCommand.setTabCompleter(command);
		commands.add(command);
	}
}
