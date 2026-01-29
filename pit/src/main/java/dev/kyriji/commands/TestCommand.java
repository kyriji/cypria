package dev.kyriji.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.ui.PlayerHud;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class TestCommand extends AbstractAsyncPlayerCommand {

	private final RequiredArg<PlayerRef> playerArg;
	private final RequiredArg<Item> itemArg;

	public TestCommand() {
		super("test", "A Simple Test Command");

		System.out.println("[Pit] TestCommand initialized!");

		this.playerArg = this.withRequiredArg("player", "A Simple Test Command", ArgTypes.PLAYER_REF);
		this.itemArg = this.withRequiredArg("item", "A Simple Test Command", ArgTypes.ITEM_ASSET);
	}

	@Nonnull
	@Override
	protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
		PlayerRef targetRef = this.playerArg.get(context);
		Item item = this.itemArg.get(context);

		Ref<EntityStore> storeRef = targetRef.getReference();

		if (storeRef == null) {
			context.sendMessage(Message.raw("Could not get player reference."));
			return CompletableFuture.completedFuture(null);
		}

		Store<EntityStore> entityStore = storeRef.getStore();
		Player player = entityStore.getComponent(storeRef, Player.getComponentType());

		if (player == null) {
			context.sendMessage(Message.raw("Could not get player entity."));
			return CompletableFuture.completedFuture(null);
		}

		if (player.getHudManager().getCustomHud() == null) player.getHudManager().setCustomHud(playerRef, new PlayerHud(playerRef));

		return CompletableFuture.completedFuture(null);
	}
}
