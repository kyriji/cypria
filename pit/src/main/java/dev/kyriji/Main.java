package dev.kyriji;

import com.hypixel.hytale.assetstore.event.LoadedAssetsEvent;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.Config;
import dev.kyriji.asset.AssetManager;
import dev.kyriji.commands.TestChargeCommand;
import dev.kyriji.commands.TestCommand;
import dev.kyriji.commands.TestPrimaryCommand;
import dev.kyriji.common.HytaleCommon;
import dev.kyriji.common.enums.Deployment;
import dev.kyriji.controllers.ChatManager;
import dev.kyriji.controllers.GameManager;
import dev.kyriji.controllers.KitManager;
import dev.kyriji.controllers.PlayerDataManager;
import dev.kyriji.controllers.ScoreboardManager;
import dev.kyriji.items.interactions.TeleportToHitLocationInteraction;
import dev.kyriji.items.test.TestItemInteraction;
import dev.kyriji.objects.PitConfig;
import dev.kyriji.utils.ChatUtils;
import dev.kyriji.utils.PlayerUtils;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {
	private static Main instance;
	private static HytaleCommon hytaleCommon;

	private boolean hasInitialized = false;

	private GameManager gameManager;
	private PlayerDataManager playerDataManager;
	private ScoreboardManager scoreboardManager;
	private ChatManager chatManager;
	private KitManager kitManager;

	public Config<PitConfig> config;

	public Main(@Nonnull JavaPluginInit init) {
		super(init);

		instance = this;
		this.config = this.withConfig("Pit", PitConfig.CODEC);

		System.out.println("[Pit] Plugin loaded!");
	}

	@Override
	protected void setup() {
		PitConfig pitConfig = config.get();
		if (pitConfig.mongoConfigURI == null || pitConfig.mongoConfigDatabase == null) {
			throw new IllegalStateException("Config file is missing required fields.");
		}

		// config.save();

		hytaleCommon = new HytaleCommon(pitConfig.toJsonObject(), Deployment.PIT, false);

		this.getCodecRegistry(Interaction.CODEC).register("test_interaction", TestItemInteraction.class, TestItemInteraction.CODEC);
		this.getCodecRegistry(Interaction.CODEC).register("TeleportToHitLocation", TeleportToHitLocationInteraction.class, TeleportToHitLocationInteraction.CODEC);

		getCommandRegistry().registerCommand(new TestCommand());
		getCommandRegistry().registerCommand(new TestChargeCommand());
		getCommandRegistry().registerCommand(new TestPrimaryCommand());
//		getCommandRegistry().registerCommand(new BroadcastTestCommand());

		getEventRegistry().register(BootEvent.class, _event -> initializeSystems());
		getEventRegistry().register(LoadedAssetsEvent.class, EntityStatType.class, AssetManager::onEntityStatsLoaded);

		if (HytaleServer.get().isBooted()) initializeSystems();

		Universe.get().getPlayers().forEach(playerRef -> {
			PlayerUtils.getPlayerFromRef(playerRef).thenAccept(player -> {
				playerDataManager.loadPlayer(playerRef.getUuid()).thenAccept(pitPlayer -> {
					pitPlayer.onPlayerReady(player);

					World world = player.getWorld();
					if (world != null) {
						world.execute(() -> {
							GameManager.preparePlayer(player);
							ScoreboardManager.preparePlayer(player);
						});
					}
				});
			});

			ChatUtils.broadcastMessage("Plugin loaded");
			ChatUtils.broadcastNotification(
					Message.raw("Plugin loaded"),
					null,
					"Icons/AssetNotifications/EditorIcon.png",
					null,
					NotificationStyle.Success
			);
		});
	}

	@Override
	public void shutdown() {
		 Universe.get().getPlayers().forEach(playerRef -> {
		 	PlayerUtils.getPlayerFromRef(playerRef).thenAccept(ScoreboardManager::removePlayer);
		 });

		 GameManager.cleanup();
		 hytaleCommon.shutdown();
	}

	private void initializeSystems() {
		if (this.hasInitialized) return;
		this.gameManager = new GameManager(this);
		this.playerDataManager = new PlayerDataManager(this);
		this.scoreboardManager = new ScoreboardManager(this);
		this.chatManager = new ChatManager(this);
		this.kitManager = new KitManager();

		String id = (new PluginIdentifier(this.getManifest())).toString();
		AssetModule.get().registerPack(id, this.getFile(), this.getManifest());

		this.hasInitialized = true;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public PlayerDataManager getPlayerManager() {
		return playerDataManager;
	}

	public ScoreboardManager getUiManager() {
		return scoreboardManager;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}

	public KitManager getKitManager() {
		return kitManager;
	}

	public static Main getInstance() {
		return instance;
	}
}