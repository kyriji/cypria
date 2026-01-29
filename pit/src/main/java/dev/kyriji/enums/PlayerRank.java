package dev.kyriji.enums;

import com.hypixel.hytale.server.core.entity.entities.Player;

import java.util.function.UnaryOperator;

public enum PlayerRank {
	DEVELOPER("group.developer", "<dark_gray>[</dark_gray><blue>Dev</blue><dark_gray>]</dark_gray>",
			name -> "<blue>" + name + "</blue>", message -> message),
	DEFAULT("group.default", "",
			name -> "<blue>" + name + "</blue>", message -> message);

	private final String permission;
	private final String prefix;
	private final UnaryOperator<String> nameFormat;
	private final UnaryOperator<String> messageFormat;

	PlayerRank(String permission, String prefix, UnaryOperator<String> nameFormat, UnaryOperator<String> messageFormat) {
		this.permission = permission;
		this.prefix = prefix;
		this.nameFormat = nameFormat;
		this.messageFormat = messageFormat;
	}

	public String getPermission() {
		return permission;
	}

	public String getPrefix() {
		return prefix;
	}

	public String formatName(String input) {
		return nameFormat.apply(input);
	}

	public String formatMessage(String input) {
		return messageFormat.apply(input);
	}

	public static PlayerRank getRank(Player player) {
		for (PlayerRank rank : values()) if (player.hasPermission(rank.getPermission())) return rank;
		throw new RuntimeException("Could not find rank for player " + player.getDisplayName());
	}
}
