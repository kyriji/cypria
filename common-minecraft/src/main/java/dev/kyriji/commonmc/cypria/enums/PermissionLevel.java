package dev.kyriji.commonmc.cypria.enums;

import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum PermissionLevel {
	KYRO(null),
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
		if (sender instanceof Player player && AUtil.isKyro(player.getUniqueId())) {
			return KYRO;
		} else if (sender.hasPermission(ADMINISTRATOR.permission)) {
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
