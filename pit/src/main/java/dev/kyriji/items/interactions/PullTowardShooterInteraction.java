// AI SLOP
package dev.kyriji.items.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeStatBehaviour;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.ValueType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.knockback.KnockbackComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;

import javax.annotation.Nonnull;

public class PullTowardShooterInteraction extends SimpleInstantInteraction {

    public float staminaCost = 0.0f;
    public float force = 20.0f;
    public float velocityY = 10.0f;

    @Nonnull
    public static final BuilderCodec<PullTowardShooterInteraction> CODEC = BuilderCodec.builder(
            PullTowardShooterInteraction.class,
            PullTowardShooterInteraction::new,
            SimpleInstantInteraction.CODEC
    ).append(
            new KeyedCodec<>("StaminaCost", Codec.FLOAT),
            (i, v) -> i.staminaCost = v,
            (i) -> i.staminaCost
    ).add().append(
            new KeyedCodec<>("Force", Codec.FLOAT),
            (i, v) -> i.force = v,
            (i) -> i.force
    ).add().append(
            new KeyedCodec<>("VelocityY", Codec.FLOAT),
            (i, v) -> i.velocityY = v,
            (i) -> i.velocityY
    ).add().build();

    @Override
    protected void firstRun(@Nonnull InteractionType type, @Nonnull InteractionContext context, @Nonnull CooldownHandler cooldownHandler) {
        Ref<EntityStore> targetRef = context.getTargetEntity();
        if (targetRef == null || !targetRef.isValid()) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        Ref<EntityStore> ownerRef = context.getOwningEntity();
        if (!ownerRef.isValid()) return;

        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        assert commandBuffer != null;

        if (staminaCost > 0) {
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
        }

        TransformComponent ownerTransform = commandBuffer.getComponent(ownerRef, TransformComponent.getComponentType());
        TransformComponent targetTransform = commandBuffer.getComponent(targetRef, TransformComponent.getComponentType());
        if (ownerTransform == null || targetTransform == null) return;

        Vector3d direction = ownerTransform.getPosition().clone().subtract(targetTransform.getPosition());
        if (direction.squaredLength() > 1e-8) {
            direction.normalize();
        }

        KnockbackComponent knockback = new KnockbackComponent();
        knockback.setVelocity(new Vector3d(
                direction.getX() * force,
                velocityY,
                direction.getZ() * force
        ));
        knockback.setVelocityType(ChangeVelocityType.Set);
        commandBuffer.tryRemoveComponent(targetRef, KnockbackComponent.getComponentType());
        commandBuffer.addComponent(targetRef, KnockbackComponent.getComponentType(), knockback);
    }

    @Override
    protected void simulateFirstRun(@Nonnull InteractionType type, @Nonnull InteractionContext context, @Nonnull CooldownHandler cooldownHandler) {
        // server-authoritative
    }
}
