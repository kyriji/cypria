package dev.kyriji.commonmc.cypria.misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AUtil {

	public static void send(CommandSender sender, ALang.Message message, Object... args) {
		color(sender, message.message().formatted(args));
	}

	public static void debug(CommandSender sender, String message) {
		color(sender, "&3&lDEBUG!&7 " + message);
	}

	public static void bypass(CommandSender sender, String message) {
		color(sender, message);
	}

	private static void color(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static boolean isNullOrAir(ItemStack itemStack) {
		return itemStack == null || itemStack.getType().isAir();
	}

	public static void log(String message) {
		System.out.println(message);
	}

	public static boolean isKyro(UUID uuid) {
		return uuid.toString().equals("c3f4a3b0-4d8a-4f0d-a0c6-7f0a9e1d6b1c");
	}
}
