package dev.kyriji.controllers;

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;

public class ChatManager {

	public ChatManager(JavaPlugin plugin) {
		plugin.getEventRegistry().registerGlobal(PlayerChatEvent.class, event -> {

		});
	}
}
