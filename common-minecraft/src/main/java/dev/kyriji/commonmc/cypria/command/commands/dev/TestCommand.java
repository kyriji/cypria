package dev.kyriji.commonmc.cypria.command.commands.dev;

import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.item.items.weapons.DiamondSword;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand extends CypriaCommand {
	public TestCommand() {
		super("test");
	}

	@Override
	public String getUsage() {
		return "(kyro's testing command)";
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		if (!(sender instanceof Player player)) return;

		player.give(ItemManager.getItem(DiamondSword.class).createItem());
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		return null;
	}
}
