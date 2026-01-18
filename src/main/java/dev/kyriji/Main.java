package dev.kyriji;

import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.kyriji.commands.TestCommand;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {

	private static Main instance;

	public Main(@Nonnull JavaPluginInit init) {
		super(init);

		instance = this;

		System.out.println("[HytaleDemo] Plugin loaded!");
	}

	@Override
	protected void setup() {
		getCommandRegistry().registerCommand(new TestCommand());

		getEventRegistry().register(BootEvent.class, event -> {
			System.out.println("[HytaleDemo] Server booted!");

		});
	}

	public static Main getInstance() {
		return instance;
	}
}