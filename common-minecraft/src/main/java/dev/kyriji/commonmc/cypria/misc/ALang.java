package dev.kyriji.commonmc.cypria.misc;

import org.bukkit.ChatColor;

public class ALang {
	public static final Message NO_PERMISSION_COMMAND = error("You do not have permission to do this.");

	private static Message info(ChatColor chatColor, String prefix, String message) {
		return new Message("" + chatColor + ChatColor.BOLD + prefix.toUpperCase() + "!&7 " + message);
	}

	private static Message error(String message) {
		return new Message(message);
	}

	public record Message(String message) { }
}
