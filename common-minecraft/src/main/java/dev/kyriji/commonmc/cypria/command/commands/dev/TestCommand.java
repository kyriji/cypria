package dev.kyriji.commonmc.cypria.command.commands.dev;

import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import dev.kyriji.commonmc.cypria.misc.ALang;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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

		ItemStack itemStack = player.getInventory().getItemInMainHand();
		ItemMeta itemMeta = itemStack.getItemMeta();
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		return null;
	}
}
