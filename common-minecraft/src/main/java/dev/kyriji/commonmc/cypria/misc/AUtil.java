package dev.kyriji.commonmc.cypria.misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AUtil {

	public static void send(CommandSender sender, ALang.Message message, Object... args) {
		color(sender, message.message().formatted(args));
	}

	public static void debug(CommandSender sender, String message) {
		color(sender, "&3&lDEBUG!&7 " + message);
	}

	private static void color(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static void log(String message) {
		System.out.println(message);
	}
}
