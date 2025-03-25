package dev.kyriji.commonmc.cypria.playerdata.listeners;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messaging.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.messaging.models.MessageListener;
import dev.kyriji.common.cypria.messaging.models.RedisMessage;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.playerdata.controllers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerListener implements Listener {

	public ServerListener() {
		CypriaCommon.getMessageManager().addListener(new MessageListener<>(MessageLoadPlayerData.class, message -> {
			PlayerDataManager.loadPlayerData(message.getPlayerUUID()).thenRun(() -> {
				message.respond(new MessageLoadPlayerData.Response(true));
			}).exceptionally(throwable -> {
				message.respond(new MessageLoadPlayerData.Response(false));
				return null;
			});
		}));
	}
}
