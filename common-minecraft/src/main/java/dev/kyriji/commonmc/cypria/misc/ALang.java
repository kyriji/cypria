package dev.kyriji.commonmc.cypria.misc;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ALang {
	// Generic Messages
	public static final Message NO_PERMISSION_COMMAND = error("You do not have permission to do this.");

	// Dev Command
	public static final Message DEV_MESSAGE = info(ChatColor.BLUE, "dev", "%s");

	// Item Dev Command
	public static final Message COMMAND_DEV_ITEM_NOT_HOLDING_ITEM = error("You are not holding an item.");
	public static final Message COMMAND_DEV_ITEM_NO_META = error("The item you are holding has no meta data.");


	private static Message info(ChatColor chatColor, String prefix, String message) {
		return new Message(message, chatColor + "&l" + prefix.toUpperCase() + "!&7");
	}

	private static Message error(String message) {
		return new Message(message, "&c&lERROR!&7", "&c&lOOPS!&7", "&c&lNOPE!&7", "&c&lSORRY!&7");
	}

	public static class Message {
		private final String message;
		private final List<String> prefixes = new ArrayList<>();

		public Message(String message, String... prefixes) {
			this.message = message;
			this.prefixes.addAll(Arrays.asList(prefixes));
		}

		public String message() {
			return prefixes.get(new Random().nextInt(prefixes.size())) + message;
		}
	}
}
