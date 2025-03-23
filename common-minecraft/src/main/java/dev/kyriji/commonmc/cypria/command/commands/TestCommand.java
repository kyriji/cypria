package dev.kyriji.commonmc.cypria.command.commands;

import dev.kyriji.commonmc.cypria.command.models.CypriaMultiCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TestCommand extends CypriaMultiCommand {
	public TestCommand() {
		super("test2");
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		sender.sendMessage("Test command executed!");
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		return null;
	}
}
