package dev.kyriji.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.kyriji.utils.ChatUtils;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

// AI SLOP
public class BroadcastTestCommand extends AbstractAsyncPlayerCommand {

	public BroadcastTestCommand() {
		super("broadcasttest", "Test broadcast functionality");
	}

	@Nonnull
	@Override
	protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {

		// ChatUtils.broadcast("Test 1: Simple raw message");

		// ChatUtils.broadcastNotification(
		// 	Message.raw("Test 2: Default style notification"),
		// 	null,
		// 	null,
		// 	null,
		// 	NotificationStyle.Default
		// );
		//
		// ChatUtils.broadcastNotification(
		// 	Message.raw("Test 3: Success style"),
		// 	null,
		// 	null,
		// 	null,
		// 	NotificationStyle.Success
		// );
		//
		// ChatUtils.broadcastNotification(
		// 	Message.raw("Test 4: Warning style"),
		// 	null,
		// 	null,
		// 	null,
		// 	NotificationStyle.Warning
		// );
		//
		// ChatUtils.broadcastNotification(
		// 	Message.raw("Test 5: Danger style"),
		// 	null,
		// 	null,
		// 	null,
		// 	NotificationStyle.Danger
		// );

		ChatUtils.broadcastNotification(
			Message.raw("Test 6: Primary message"),
			Message.raw("With secondary message"),
			null,
			null,
			NotificationStyle.Default
		);

		Message coloredMessage = Message.raw("Test 7: Colored message").color("#FF0000");
		ChatUtils.broadcastNotification(
			coloredMessage,
			null,
			null,
			null,
			NotificationStyle.Default
		);

		Message translatedMessage = Message.translation("server.chat.playerMessage")
			.param("username", playerRef.getUsername())
			.param("message", "Test 8: Translation with params");
		ChatUtils.broadcastNotification(
			translatedMessage,
			null,
			null,
			null,
			NotificationStyle.Default
		);

		ChatUtils.broadcastNotification(
			Message.raw("Test 9: Notification with icon"),
			null,
			"Icons/AssetNotifications/Trash.png",
			null,
			NotificationStyle.Warning
		);

		ItemStack testItem = new ItemStack("Rock_Stone", 1);
		ChatUtils.broadcastNotification(
			Message.raw("Test 10: Notification with item"),
			null,
			null,
			testItem.toPacket(),
			NotificationStyle.Default
		);

		ChatUtils.broadcastNotification(
			Message.raw("Test 11: All fields together"),
			Message.raw("Secondary message with icon and item"),
			"Icons/AssetNotifications/IconCheckmark.png",
			new ItemStack("Rock_Bedrock", 4).toPacket(),
			NotificationStyle.Success
		);

		ChatUtils.broadcastMessage("Test 12: Chat message to all players");

		context.sendMessage(Message.raw("Broadcast tests completed!"));

		return CompletableFuture.completedFuture(null);
	}
}
