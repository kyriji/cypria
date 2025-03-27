package dev.kyriji.commonmc.cypria.playerdata.controllers;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.playerdata.enums.PlayerDataType;
import dev.kyriji.commonmc.cypria.player.controllers.PlayerManager;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerDataManager {

	public static CompletableFuture<Void> loadPlayerData(UUID playerUUID) {
		return CompletableFuture.runAsync(() -> {
			for(PlayerDataType value : PlayerDataType.values()) {
				CypriaCommon.getPlayerDataManager().loadPlayerData(playerUUID, value);
			}
			PlayerManager.onPlayerDataLoad(playerUUID);
		});
	}
}
