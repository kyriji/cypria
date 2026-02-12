package dev.kyriji.items.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector4d;
import com.hypixel.hytale.protocol.ChangeStatBehaviour;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.ValueType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;

import javax.annotation.Nonnull;

public class TeleportToHitLocationInteraction extends SimpleInstantInteraction {

    public float staminaCost = 0.0f;

    @Nonnull
    public static final BuilderCodec<TeleportToHitLocationInteraction> CODEC = BuilderCodec.builder(
            TeleportToHitLocationInteraction.class,
            TeleportToHitLocationInteraction::new,
            SimpleInstantInteraction.CODEC
    ).documentation("Teleports the owner of the interaction chain to the projectile hit location.")
    .append(
            new KeyedCodec<>("StaminaCost", Codec.FLOAT),
            (interaction, cost) -> interaction.staminaCost = cost,
            (interaction) -> interaction.staminaCost
    )
    .documentation("Amount of stamina to consume on a successful teleport. " +
            "If the owner has less than this amount the interaction fails. Default: 0 (free).")
    .add().build();

    @Override
    protected void firstRun(@Nonnull InteractionType type, @Nonnull InteractionContext context, @Nonnull CooldownHandler cooldownHandler) {
        Vector4d hitLocation = context.getMetaStore().getMetaObject(Interaction.HIT_LOCATION);
        if (hitLocation == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        Ref<EntityStore> ownerRef = context.getOwningEntity();
        if (!ownerRef.isValid()) return;

        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        assert commandBuffer != null;

        if (commandBuffer.getComponent(ownerRef, Player.getComponentType()) == null) return;

        EntityStatMap statMap = commandBuffer.getComponent(ownerRef, EntityStatMap.getComponentType());
        if (statMap == null) return;

        int staminaIndex = EntityStatType.getAssetMap().getIndex("Stamina");
        EntityStatValue staminaStat = statMap.get(staminaIndex);

        if (staminaStat == null || staminaStat.get() < staminaCost) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        Int2FloatOpenHashMap costMap = new Int2FloatOpenHashMap(1);
        costMap.put(staminaIndex, -staminaCost);
        statMap.processStatChanges(EntityStatMap.Predictable.SELF, costMap, ValueType.Absolute, ChangeStatBehaviour.Add);

        TransformComponent transform = commandBuffer.getComponent(ownerRef, TransformComponent.getComponentType());
        Vector3f rotation = transform != null ? transform.getRotation().clone() : new Vector3f();

        Vector3d destination = new Vector3d(hitLocation.x, hitLocation.y, hitLocation.z);
        commandBuffer.addComponent(ownerRef, Teleport.getComponentType(), Teleport.createForPlayer(destination, rotation));
    }

    @Override
    protected void simulateFirstRun(@Nonnull InteractionType type, @Nonnull InteractionContext context,
                                    @Nonnull CooldownHandler cooldownHandler) {
        // Teleport is server-authoritative; no client prediction needed
    }
}