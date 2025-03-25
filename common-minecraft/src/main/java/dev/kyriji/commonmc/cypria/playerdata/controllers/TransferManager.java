package dev.kyriji.commonmc.cypria.playerdata.controllers;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.common.cypria.messaging.messages.MessageQueueRequest;
import dev.kyriji.commonmc.cypria.misc.ALang;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class TransferManager {

	public static void handlePlayerQueue(Player player, Deployment deployment) {

		PlayerDataManager.savePlayerData(player).thenRun(() -> {
			CompletableFuture<Void> unfreeze = CypriaCommon.getPlayerDataManager().freezePlayerData(player.getUniqueId());

			MessageQueueRequest message = new MessageQueueRequest(player.getUniqueId(), deployment);
			message.send(response -> {
				if(!response.success) {
					unfreeze.complete(null);
					AUtil.send(player, ALang.PLAYER_TRANSFER_FAILED);
				}
			});
		});

	}
}
