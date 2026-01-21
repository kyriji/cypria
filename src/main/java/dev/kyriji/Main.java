package dev.kyriji;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.commands.TestCommand;
import dev.kyriji.controllers.PlayerManager;
import dev.kyriji.controllers.GameManager;
import dev.kyriji.controllers.UIManager;
import dev.kyriji.objects.PitPlayer;
import dev.kyriji.utils.PlayerUtils;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {

	private static Main instance;
	private boolean hasInitialized = false;

	private GameManager gameManager;
	private PlayerManager playerManager;
	private UIManager uiManager;

	public Main(@Nonnull JavaPluginInit init) {
		super(init);

		instance = this;

		System.out.println("[HytaleDemo] Plugin loaded!");
	}

	@Override
	protected void setup() {
		getCommandRegistry().registerCommand(new TestCommand());
		getEventRegistry().register(BootEvent.class, event -> initializeSystems());

		if (HytaleServer.get().isBooted()) initializeSystems();

		Universe.get().getPlayers().forEach(playerRef -> {
			this.playerManager.registerPlayer(playerRef.getUuid());

			PlayerUtils.getPlayerFromRef(playerRef).thenAccept(player -> {
				GameManager.preparePlayer(player);
				UIManager.preparePlayer(player);
			});
		});
	}

	@Override
	public void shutdown() {
		Universe.get().getPlayers().forEach(playerRef -> {
			PitPlayer pitPlayer = PlayerManager.getPitPlayer(playerRef.getUuid());

			pitPlayer.save();

			PlayerUtils.getPlayerFromRef(playerRef).thenAccept(UIManager::removePlayer);
		});

		AssetModule.get().unregisterPack(getIdentifier().toString());
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