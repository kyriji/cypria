package dev.kyriji.cypria.islands.base.building.path;

import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PathManager implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		handleBlockClick(event, event.getPlayer(), event.getBlock());
	}

	@EventHandler
	public void onDamage(BlockDamageEvent event) {
		handleBlockClick(event, event.getPlayer(), event.getBlock());
	}

	public void handleBlockClick(Cancellable event, Player player, Block block) {
		ItemStack itemStack = player.getInventory().getItemInMainHand();
		if (itemStack.getType() != Material.BLAZE_ROD) return;

		event.setCancelled(true);
		AUtil.debug(player, "creating path network at clicked location");

		// Create and render the path at the clicked location
		new Path(block.getLocation());
		AUtil.debug(player, "path created");
	}
}