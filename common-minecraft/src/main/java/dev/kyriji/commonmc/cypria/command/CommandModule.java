package dev.kyriji.commonmc.cypria.command;

import dev.kyriji.commonmc.cypria.command.commands.dev.ItemCommand;
import dev.kyriji.commonmc.cypria.command.commands.dev.DevMultiCommand;
import dev.kyriji.commonmc.cypria.command.commands.dev.TestCommand;
import dev.kyriji.commonmc.cypria.command.controllers.CommandManager;
import dev.kyriji.commonmc.cypria.command.models.CypriaMultiCommand;
import dev.kyriji.commonmc.cypria.model.CypriaModule;

public class CommandModule extends CypriaModule {

	@Override
	public void init() {
		registerCommands();
	}

	private void registerCommands() {
		CypriaMultiCommand multiCommand = new DevMultiCommand()
				.registerSubCommand(new ItemCommand())
				.registerSubCommand(new TestCommand());
		CommandManager.registerCommand(multiCommand);
	}
}
