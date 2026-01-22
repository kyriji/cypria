package dev.kyriji;

import com.hypixel.hytale.codec.util.RawJsonReader;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.Config;
import dev.kyriji.commands.TestCommand;
import dev.kyriji.common.HytaleCommon;
import dev.kyriji.common.enums.Deployment;
import dev.kyriji.controllers.PlayerManager;
import dev.kyriji.controllers.GameManager;
import dev.kyriji.controllers.UIManager;
import dev.kyriji.objects.PitConfig;
import dev.kyriji.objects.PitPlayer;
import dev.kyriji.utils.PlayerUtils;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {

	private static Main instance;
	private static HytaleCommon hytaleCommon;

	private boolean hasInitialized = false;

	private GameManager gameManager;
	private PlayerManager playerManager;
	private UIManager uiManager;

	Config<PitConfig> config;

	public Main(@Nonnull JavaPluginInit init) {
		super(init);

		instance = this;
		this.config = this.withConfig("Pit", PitConfig.CODEC);

		System.out.println("[Pit] Plugin loaded!");
	}

	@Override
	protected void setup() {
		config.save();
		hytaleCommon = new HytaleCommon(config.get().toJsonObject(), Deployment.PIT);

		getCommandRegistry().registerCommand(new TestCommand());
		getEventRegistry().register(BootEvent.class, event -> initializeSystems());

		if (HytaleServer.get().isBooted()) initializeSystems();

		Universe.get().getPlayers().forEach(playerRef -> {
			PlayerUtils.getPlayerFromRef(playerRef).thenAccept(player -> {
				playerManager.loadPlayer(playerRef.getUuid());

				if (PlayerManager.isLoaded(playerRef.getUuid())) {
					GameManager.preparePlayer(player);
					UIManager.preparePlayer(player);
				}
			});
		});
	}

	@Override
	public void shutdown() {
		//TODO: Fix errors with data saving during hot-reload
		Universe.get().getPlayers().forEach(playerRef -> {
			PitPlayer pitPlayer = PlayerManager.getPitPlayer(playerRef.getUuid());

			pitPlayer.save();

			PlayerUtils.getPlayerFromRef(playerRef).thenAccept(UIManager::removePlayer);
		});

		AssetModule.get().unregisterPack(getIdentifier().toString());
		GameManager.cleanup();
		hytaleCommon.shutdown();
	}

	private void initializeSystems() {
		if (this.hasInitialized) return;
		this.gameManager = new GameManager(this);
		this.playerManager = new PlayerManager(this);
		this.uiManager = new UIManager(this);

		this.hasInitialized = true;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public UIManager getUiManager() {
		return uiManager;
	}

	public static Main getInstance() {
		return instance;
	}
}