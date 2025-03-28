package dev.kyriji.cypria.islands.commands;

import dev.kyriji.commonmc.cypria.command.models.CypriaCommand;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import dev.kyriji.cypria.islands.base.building.buildings.TestBuilding;
import dev.kyriji.cypria.islands.base.models.Base;
import dev.kyriji.cypria.islands.base.models.BasePos;
import dev.kyriji.cypria.islands.world.controllers.WorldManager;
import dev.kyriji.cypria.islands.world.data.CypriaWorld;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand extends CypriaCommand {
	public static Base base;

	public TestCommand() {
		super("test");
	}

	@Override
	public String getUsage() {
		return "base test command";
	}

	@Override
	public void execute(CommandSender sender, String command, List<String> args) {
		if (!(sender instanceof Player player)) return;

		if (base == null) {
			AUtil.debug(player, "creating base");

			CypriaWorld cypriaWorld = WorldManager.acquireWorld();

			base = new Base(cypriaWorld);
			base.putBuilding(new TestBuilding(), new BasePos(0, 0, 3));
			base.place();

			Location location = base.getCenter();
			location.setYaw(-180);
			player.teleport(location);

			AUtil.debug(player, "created base");
		} else {
			AUtil.debug(player, "releasing world");
			base.destroy();
			base = null;
			AUtil.debug(player, "released world");
		}
	}

	@Override
	public List<String> getTabComplete(CommandSender sender, String command, List<String> args) {
		return null;
	}
}
