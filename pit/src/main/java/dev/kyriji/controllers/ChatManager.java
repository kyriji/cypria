package dev.kyriji.controllers;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import dev.kyriji.enums.PlayerRank;
import dev.kyriji.utils.PlayerUtils;
import fi.sulku.hytale.TinyMsg;

public class ChatManager {

	public ChatManager(JavaPlugin plugin) {
		plugin.getEventRegistry().registerGlobal(PlayerChatEvent.class, event -> {
			Player player = PlayerUtils.getPlayerFromRef(event.getSender()).join();
			if (player == null) throw new RuntimeException("Player not found");

			PlayerRank rank = PlayerRank.getRank(player);
			String message = rank.getPrefix() + " " + rank.formatName(player.getDisplayName()) + rank.formatMessage(": " + event.getContent());
			event.setFormatter((playerRef, content) -> TinyMsg.parse(message));
		});
	}
}
