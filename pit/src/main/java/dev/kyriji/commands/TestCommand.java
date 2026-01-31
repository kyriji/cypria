package dev.kyriji.commands;

import com.hypixel.hytale.codec.EmptyExtraInfo;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.Vector3d;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.common.HytaleCommon;
import dev.kyriji.controllers.EnderChestWindow;
import dev.kyriji.controllers.GameManager;
import dev.kyriji.controllers.PlayerDataManager;
import dev.kyriji.objects.PitPlayer;
import org.bson.BsonDocument;
import org.bson.BsonValue;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TestCommand extends AbstractAsyncPlayerCommand {

	private final RequiredArg<PlayerRef> playerArg;

	public TestCommand() {
		super("test", "A Simple Test Command");

		System.out.println("[Pit] TestCommand initialized!");

		this.playerArg = this.withRequiredArg("player", "A Simple Test Command", ArgTypes.PLAYER_REF);
	}

	@Nonnull
	@Override
	protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
		PlayerRef targetRef = this.playerArg.get(context);

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

		PitPlayer pitPlayer = PlayerDataManager.getPitPlayer(player);
		pitPlayer.enderChest.open();

		return CompletableFuture.completedFuture(null);
	}
}
