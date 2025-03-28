package dev.kyriji.commonmc.cypria.command;

import dev.kyriji.commonmc.cypria.command.commands.dev.*;
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
				.registerSubCommand(new TestCommand())
				.registerSubCommand(new TransferCommand());
		CommandManager.registerCommand(multiCommand);
	}
}
