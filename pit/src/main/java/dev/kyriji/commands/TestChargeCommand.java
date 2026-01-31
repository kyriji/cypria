package dev.kyriji.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

// AI SLOP
public class TestChargeCommand extends AbstractPlayerCommand {

	public TestChargeCommand() {
		super("testcharge", "Charge signature energy to maximum");
		System.out.println("[Pit] TestChargeCommand initialized!");
	}

	@Override
	protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
		Player player = store.getComponent(ref, Player.getComponentType());
		if (player == null) {
			context.sendMessage(Message.raw("Could not get player component."));
			return;
		}

		int signatureEnergyIndex = EntityStatType.getAssetMap().getIndex("SignatureEnergy");
		if (signatureEnergyIndex == Integer.MIN_VALUE) {
			context.sendMessage(Message.raw("SignatureEnergy stat not found."));
			return;
		}

		EntityStatMap entityStatMap = store.getComponent(ref, EntityStatsModule.get().getEntityStatMapComponentType());
		if (entityStatMap == null) {
			context.sendMessage(Message.raw("Could not get EntityStatMap component."));
			return;
		}

		EntityStatValue signatureEnergyStat = entityStatMap.get(signatureEnergyIndex);
		if (signatureEnergyStat == null) {
			context.sendMessage(Message.raw("SignatureEnergy stat not available on this entity."));
			return;
		}

		float maxValue = signatureEnergyStat.getMax();
		entityStatMap.setStatValue(signatureEnergyIndex, maxValue);

		context.sendMessage(Message.raw("SignatureEnergy charged to maximum: " + maxValue));
	}
}
