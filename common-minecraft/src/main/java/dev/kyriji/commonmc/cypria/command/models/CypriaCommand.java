package dev.kyriji.commonmc.cypria.command.models;

import dev.kyriji.commonmc.cypria.enums.PermissionLevel;
import dev.kyriji.commonmc.cypria.misc.ALang;
import dev.kyriji.commonmc.cypria.misc.AUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CypriaCommand implements CommandExecutor, TabCompleter {
	private final String command;
	private PermissionLevel minimumPermission;
	private final List<String> aliases = new ArrayList<>();

	private CypriaMultiCommand multiCommand;
	private boolean isBaseLevel = true;

	public CypriaCommand(String command) {
		this(command, PermissionLevel.DEFAULT);
	}

	public CypriaCommand(String command, PermissionLevel minimumPermission) {
		this.command = command;
		this.minimumPermission = minimumPermission;
	}

	public abstract String getUsage();
	public abstract void execute(CommandSender sender, String command, List<String> args);
	public abstract List<String> getTabComplete(CommandSender sender, String command, List<String> args);

	public String getBaseCommand(String label) {
		// TODO: this could be done better if the previous labels were passed
		if (label == null) label = command;
		return (isBaseLevel ? "/" : multiCommand.getBaseCommand(null) + " ") + label;
	}

	public String getCommand() {
		return command;
	}

	public PermissionLevel getMinimumPermission() {
		return minimumPermission;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public CypriaCommand addAliases(String... aliases) {
		this.aliases.addAll(Arrays.asList(aliases));
		return this;
	}

	public CypriaMultiCommand getMultiCommand() {
		return multiCommand;
	}

	public void setMultiCommand(CypriaMultiCommand multiCommand) {
		this.multiCommand = multiCommand;
		this.isBaseLevel = false;
	}

	public boolean isBaseLevel() {
		return isBaseLevel;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!PermissionLevel.hasPermission(sender, minimumPermission)) {
			AUtil.send(sender, ALang.NO_PERMISSION_COMMAND);
			return false;
		}
		List<String> argsList = new ArrayList<>(Arrays.asList(args));
		execute(sender, label, argsList);
		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!PermissionLevel.hasPermission(sender, minimumPermission)) return null;
		List<String> argsList = new ArrayList<>(Arrays.asList(args));
		return getTabComplete(sender, label, argsList);
	}
}
