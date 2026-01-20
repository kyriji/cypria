package dev.kyriji;

import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.kyriji.commands.TestCommand;
import dev.kyriji.controllers.SpawnManager;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {

	private static Main instance;
	private SpawnManager spawnManager;

	public Main(@Nonnull JavaPluginInit init) {
		super(init);

		instance = this;

		System.out.println("[HytaleDemo] Plugin loaded!");
	}

	@Override
	protected void setup() {
		getCommandRegistry().registerCommand(new TestCommand());

		this.spawnManager = new SpawnManager(this);
	}

	public SpawnManager getSpawnManager() {
		return spawnManager;
	}

	public static Main getInstance() {
		return instance;
	}
}