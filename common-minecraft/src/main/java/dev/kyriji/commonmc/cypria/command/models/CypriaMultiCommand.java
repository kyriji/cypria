package dev.kyriji.commonmc.cypria.command.models;

import dev.kyriji.commonmc.cypria.enums.PermissionLevel;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import dev.kyriji.commonmc.cypria.misc.FontUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.awt.font.FontRenderContext;
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
	public String getUsage() {
		List<String> subCommandNames = new ArrayList<>();
		for (CypriaCommand subCommand : subCommands) subCommandNames.add(subCommand.getCommand());
		return "<" + String.join("|", subCommandNames) + ">";
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		CypriaCommand subCommand = !args.isEmpty() ? getSubCommand(args.removeFirst()) : null;

		String header;
		if (subCommand == null) {
			header = FontUtils.createHeader(ChatColor.GOLD, ChatColor.LIGHT_PURPLE, 15, "help");
			AUtil.bypass(sender, header);
			for (CypriaCommand cypriaCommand : subCommands) AUtil.bypass(sender, "&d> &6" +
					getBaseCommand(command) + " " + cypriaCommand.getCommand() + " &7" + cypriaCommand.getUsage());
			AUtil.bypass(sender, FontUtils.createPlainFooter(ChatColor.LIGHT_PURPLE, header));
			return;
		}

		new FontRenderContext(null, false, false);

		subCommand.execute(sender, command, args);
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		assert !args.isEmpty();

		if(args.size() == 1) {
			String subCommand = args.getFirst().toLowerCase();
			List<String> tabComplete = new ArrayList<>();
			for (CypriaCommand testSubCommand : subCommands) {
				if (testSubCommand.getCommand().startsWith(subCommand)) tabComplete.add(testSubCommand.getCommand());
				for (String alias : testSubCommand.getAliases()) if (alias.startsWith(command.toLowerCase())) tabComplete.add(alias);
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
