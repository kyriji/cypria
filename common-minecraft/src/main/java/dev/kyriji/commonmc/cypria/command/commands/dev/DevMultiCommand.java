package dev.kyriji.commonmc.cypria.command.commands.dev;

import dev.kyriji.commonmc.cypria.command.models.CypriaMultiCommand;
import dev.kyriji.commonmc.cypria.enums.PermissionLevel;

public class DevMultiCommand extends CypriaMultiCommand {
	public DevMultiCommand() {
		super("dev", PermissionLevel.ADMINISTRATOR);
	}
}
