package dev.kyriji.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatPrefix {
	private static final List<ChatPrefix> errorPrefixes = new ArrayList<>();

	public static final ChatPrefix SUCCESS = new ChatPrefix("<green><bold>OOPS!</bold></green>");
	public static final ChatPrefix BROADCAST = new ChatPrefix("<red><bold>BROADCAST!</bold></red>");

	static {
		errorPrefixes.add(new ChatPrefix("<red><bold>OOPS!</bold></red>"));
	}

	private final String prefix;

	public ChatPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String apply(String message) {
		return prefix + " " + message;
	}

	public static ChatPrefix error() {
		return errorPrefixes.get(new Random().nextInt(errorPrefixes.size()));
	}
}
