package dev.kyriji.commonmc.cypria.command.commands.dev;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.item.items.weapons.DiamondSword;
import dev.kyriji.commonmc.cypria.playerdata.controllers.PlayerDataManager;
import dev.kyriji.commonmc.cypria.playerdata.controllers.TransferManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TransferCommand extends CypriaCommand {
	public TransferCommand() {
		super("transfer");
	}

	@Override
	public String getUsage() {
		return "(command for testing server transfers)";
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		if (!(sender instanceof Player player)) return;

		Deployment currentDeployment = CypriaCommon.getDeployment();
		Deployment newDeployment = currentDeployment == Deployment.HUB ? Deployment.ISLANDS : Deployment.HUB;

		TransferManager.handlePlayerQueue(player, newDeployment);
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		return null;
	}
}
