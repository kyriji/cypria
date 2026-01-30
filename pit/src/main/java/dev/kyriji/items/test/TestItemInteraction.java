package dev.kyriji.items.test;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class TestItemInteraction extends SimpleInstantInteraction {
	public static final BuilderCodec<TestItemInteraction> CODEC = BuilderCodec.builder(
			TestItemInteraction.class, TestItemInteraction::new, SimpleInstantInteraction.CODEC
	).build();

	public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

	@Override
	protected void firstRun(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
		CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
		if (commandBuffer == null) {
			interactionContext.getState().state = InteractionState.Failed;
			LOGGER.atInfo().log("CommandBuffer is null");
			return;
		}
		World world = commandBuffer.getExternalData().getWorld(); // just to show how to get the world if needed
		Store<EntityStore> store = commandBuffer.getExternalData().getStore(); // just to show how to get the store if needed
		Ref<EntityStore> ref = interactionContext.getEntity();
		Player player = commandBuffer.getComponent(ref, Player.getComponentType());
		if (player == null) {
			interactionContext.getState().state = InteractionState.Failed;
			LOGGER.atInfo().log("Player is null");
			return;
		}
		ItemStack itemStack = interactionContext.getHeldItem();
		if (itemStack == null) {
			interactionContext.getState().state = InteractionState.Failed;
			LOGGER.atInfo().log("ItemStack is null");
			return;
		}
		player.sendMessage(Message.raw("You have used the custom item +" + itemStack.getItemId()));
	}
}
