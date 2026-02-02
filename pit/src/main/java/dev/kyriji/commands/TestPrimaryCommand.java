package dev.kyriji.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.InteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.InteractionModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

// AI SLOP
public class TestPrimaryCommand extends AbstractPlayerCommand {

	public TestPrimaryCommand() {
		super("testprimary", "Trigger primary interaction on held item");
		System.out.println("[Pit] TestPrimaryCommand initialized!");
	}

	@Override
	protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
		Player player = store.getComponent(ref, Player.getComponentType());
		if (player == null) {
			context.sendMessage(Message.raw("Could not get player component."));
			return;
		}

		Inventory inventory = player.getInventory();
		ItemStack heldItem = inventory.getActiveHotbarItem();

		if (heldItem == null || heldItem.isEmpty()) {
			context.sendMessage(Message.raw("You must be holding an item!"));
			return;
		}

		String rootInteractionId = heldItem.getItem().getInteractions().get(InteractionType.Primary);
		if (rootInteractionId == null) {
			context.sendMessage(Message.raw("Your held item has no primary interaction."));
			return;
		}

		RootInteraction rootInteraction = RootInteraction.getAssetMap().getAsset(rootInteractionId);
		if (rootInteraction == null) {
			context.sendMessage(Message.raw("Could not find interaction: " + rootInteractionId));
			return;
		}

		InteractionManager interactionManager = store.getComponent(ref, InteractionModule.get().getInteractionManagerComponent());
		if (interactionManager == null) {
			context.sendMessage(Message.raw("Could not get InteractionManager component."));
			return;
		}

		if (!interactionManager.canRun(InteractionType.Primary, rootInteraction)) {
			context.sendMessage(Message.raw("Cannot run interaction - it may be on cooldown or blocked."));
			return;
		}

		InteractionContext interactionContext = InteractionContext.forInteraction(interactionManager, ref, InteractionType.Primary, store);
		InteractionChain chain = interactionManager.initChain(InteractionType.Primary, interactionContext, rootInteraction, false);
		interactionManager.queueExecuteChain(chain);

		context.sendMessage(Message.raw("Triggered primary interaction: " + rootInteractionId));
	}
}
