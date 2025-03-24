package dev.kyriji.commonmc.cypria.command.commands;

import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TestCommand extends CypriaCommand {
	public TestCommand() {
		super("test2");
	}

	@Override
	public String getUsage() {
		return "<args...>";
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		AUtil.debug(sender, "&e" + String.join(" ", args));
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		return null;
	}
}
