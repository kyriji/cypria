package dev.kyriji.utils;

import com.hypixel.hytale.protocol.ItemWithAllMetadata;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import fi.sulku.hytale.TinyMsg;

public class ChatUtils {

	// This currently just wraps around the send message but will likely change as we figure out what behavior we want
	public static void broadcastNotification(
		Message primaryMessage,
		Message secondaryMessage,
		String icon,
		ItemWithAllMetadata item,
		NotificationStyle style
	) {
		NotificationUtil.sendNotificationToUniverse(primaryMessage, secondaryMessage, icon, item, style);
	}

	public static void broadcastMessage(String message) {
		for (World world : Universe.get().getWorlds().values())
			world.sendMessage(TinyMsg.parse(ChatPrefix.BROADCAST.apply(message)));
	}
}
