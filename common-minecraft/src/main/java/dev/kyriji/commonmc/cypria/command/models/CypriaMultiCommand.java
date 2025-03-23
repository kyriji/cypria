package dev.kyriji.commonmc.cypria.command.models;

import dev.kyriji.commonmc.cypria.enums.PermissionLevel;
import dev.kyriji.commonmc.cypria.misc.ALang;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class CypriaMultiCommand extends CypriaCommand {
	public List<CypriaCommand> subCommands = new ArrayList<>();

	public CypriaMultiCommand(String command) {
		super(command);
	}

	public CypriaMultiCommand(String command, PermissionLevel minimumPermission) {
		super(command, minimumPermission);
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		CypriaCommand subCommand = getSubCommand(args.removeFirst());

		if (subCommand == null) {
			AUtil.send(sender, ALang.UNKNOWN_COMMAND, command);
			return;
		}

		subCommand.execute(sender, command, args);
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		if(args.size() <= 1) {
			List<String> tabComplete = new ArrayList<>();
			for (CypriaCommand testSubCommand : subCommands) {
				if (testSubCommand.getCommand().startsWith(command.toLowerCase())) tabComplete.add(testSubCommand.getCommand());
				for (String alias : testSubCommand.getAliases()) {
					if (alias.startsWith(command.toLowerCase())) tabComplete.add(alias);
				}
			}
			return tabComplete;
		}

		CypriaCommand subCommand = getSubCommand(command);
		if (subCommand == null) return new ArrayList<>();
		return subCommand.getTabComplete(sender, command, args);
	}

	public CypriaCommand getSubCommand(String command) {
		for (CypriaCommand subCommand : subCommands) {
			if (subCommand.getCommand().equalsIgnoreCase(command)) {
				return subCommand;
			}
			for (String alias : subCommand.getAliases()) {
				if (alias.equalsIgnoreCase(command)) {
					return subCommand;
				}
			}
		}
		return null;
	}

	public CypriaMultiCommand registerSubCommand(CypriaCommand command) {
		command.setMultiCommand(this);
		subCommands.add(command);
		return this;
	}
}
