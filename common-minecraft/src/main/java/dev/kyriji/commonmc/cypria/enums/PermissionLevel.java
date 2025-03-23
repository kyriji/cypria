package dev.kyriji.commonmc.cypria.enums;

import org.bukkit.command.CommandSender;

public enum PermissionLevel {
	ADMINISTRATOR("cypria.admin"),
	MODERATOR("cypria.mod"),
	DEFAULT(null),
	;

	PermissionLevel(String permission) {
		this.permission = permission;
	}

	public final String permission;

	public boolean hasPermission(PermissionLevel level) {
		return this.ordinal() <= level.ordinal();
	}

	public static PermissionLevel getPermissionLevel(CommandSender sender) {
		if (sender.hasPermission(ADMINISTRATOR.permission)) {
			return ADMINISTRATOR;
		} else if (sender.hasPermission(MODERATOR.permission)) {
			return MODERATOR;
		} else {
			return DEFAULT;
		}
	}

	public static boolean hasPermission(CommandSender sender, PermissionLevel minimumPermission) {
		return getPermissionLevel(sender).hasPermission(minimumPermission);
	}
}
