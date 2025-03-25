package dev.kyriji.commonmc.cypria.playerdata.listeners;

import dev.kyriji.bigminecraftapi.controllers.NetworkManager;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messaging.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.messaging.models.MessageListener;
import dev.kyriji.commonmc.cypria.playerdata.controllers.PlayerDataManager;
import org.bukkit.event.Listener;

import java.util.Objects;

public class ServerListener implements Listener {

	public ServerListener() {
		CypriaCommon.getMessageManager().addListener(new MessageListener<>(MessageLoadPlayerData.class, message -> {

			String localAddress = NetworkManager.getIPAddress();
			if(!Objects.equals(localAddress, message.getTargetInstanceAddress())) return;

			PlayerDataManager.loadPlayerData(message.getPlayerUUID()).thenRun(() -> {
				message.respond(new MessageLoadPlayerData.Response(true));
			}).exceptionally(throwable -> {
				message.respond(new MessageLoadPlayerData.Response(false));
				return null;
			});
		}));
	}
}
