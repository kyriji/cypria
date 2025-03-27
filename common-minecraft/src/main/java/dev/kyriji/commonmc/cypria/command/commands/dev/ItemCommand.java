package dev.kyriji.commonmc.cypria.command.commands.dev;

import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import dev.kyriji.commonmc.cypria.misc.ALang;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemCommand extends CypriaCommand {
	public ItemCommand() {
		super("item");
	}

	@Override
	public String getUsage() {
		return "(print out held item components)";
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		if (!(sender instanceof Player player)) return;

		ItemStack itemStack = player.getInventory().getItemInMainHand();
		if (AUtil.isNullOrAir(itemStack)) {
			AUtil.send(sender, ALang.COMMAND_DEV_ITEM_NOT_HOLDING_ITEM);
			return;
		}

		if (!itemStack.hasItemMeta()) {
			AUtil.send(sender, ALang.COMMAND_DEV_ITEM_NO_META);
			return;
		}

		AUtil.raw(sender, AUtil.colorize(ALang.DEV_MESSAGE.prefix()) + " " +
				AUtil.uncolorize(itemStack.getItemMeta().getAsComponentString()));
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		return null;
	}
}
