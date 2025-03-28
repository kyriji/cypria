package dev.kyriji.commonmc.cypria.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

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
		sender.sendMessage(colorize(message));
	}

	public static void raw(CommandSender sender, String message) {
		sender.sendMessage(message);
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message).replaceAll("&&", "&");
	}

	public static String uncolorize(String message) {
		return message.replaceAll("\u00A7", "&");
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

	public static ItemMeta getOrCreateItemMeta(ItemStack itemStack) {
		if (isNullOrAir(itemStack)) return null;
		return itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
	}

	public static PersistentDataContainer createPDC(ItemStack itemStack) {
		return itemStack.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
	}
}
