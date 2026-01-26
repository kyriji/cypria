package dev.kyriji.objects;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.protocol.InteractionSyncData;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.modules.entity.component.Interactable;
import com.hypixel.hytale.server.core.modules.entity.component.Invulnerable;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import it.unimi.dsi.fastutil.Pair;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PitNPC {

	private final NPCEntity npcEntity;
	private final Ref<EntityStore> entityRef;
	private final int networkId;
	private final Consumer<UUID> interactionHandler;

	private PitNPC(NPCEntity npcEntity, Ref<EntityStore> entityRef, int networkId, Consumer<UUID> interactionHandler) {
		this.npcEntity = npcEntity;
		this.entityRef = entityRef;
		this.networkId = networkId;
		this.interactionHandler = interactionHandler;

		if (interactionHandler != null) {
			registerInteractionPacketHandler();
		}
	}

	public static PitNPC spawn(Store<EntityStore> store, String modelAssetId, Vector3d position, Vector3f rotation, World world, Consumer<UUID> interactionHandler) {
		return spawn(store, modelAssetId, position, rotation, world, 1.0f, interactionHandler);
	}

	public static PitNPC spawn(Store<EntityStore> store, String modelAssetId, Vector3d position, Vector3f rotation, World world, float scale, Consumer<UUID> interactionHandler) {
		ModelAsset modelAsset = Objects.requireNonNull(ModelAsset.getAssetMap().getAsset(modelAssetId));
		Model model = Model.createScaledModel(modelAsset, scale);

		NPCPlugin npcPlugin = NPCPlugin.get();
		int roleIndex = npcPlugin.getIndex("Empty_Role");

		int[] networkIdHolder = new int[1];

		Pair<Ref<EntityStore>, NPCEntity> npcPair = npcPlugin.spawnEntity(store, roleIndex, position, rotation, model,
				(npcComponent, holder, storeParam) -> {
					holder.addComponent(Invulnerable.getComponentType(), Invulnerable.INSTANCE);
					holder.ensureComponent(Interactable.getComponentType());

					EntityStore entityStore = storeParam.getExternalData();
					int networkId = entityStore.takeNextNetworkId();
					holder.addComponent(NetworkId.getComponentType(), new NetworkId(networkId));

					networkIdHolder[0] = networkId;
				},
				(npcComponent, ref, storeParam) -> {
					HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
						world.execute(() -> {
							npcComponent.playAnimation(ref, AnimationSlot.Status, "Idle", storeParam);

							Role role = npcComponent.getRole();
							if (role != null) role.getActiveMotionController().setKnockbackScale(0.0);
						});
					}, 500, TimeUnit.MILLISECONDS);
				}
		);

		if (npcPair == null) return null;

		return new PitNPC(npcPair.second(), npcPair.first(), networkIdHolder[0], interactionHandler);
	}

	private void registerInteractionPacketHandler() {
		PacketAdapters.registerInbound((PacketHandler handler, Packet packet) -> {
			if (packet instanceof SyncInteractionChains interactionPacket) {
				for (SyncInteractionChain update : interactionPacket.updates) {
					if (update.interactionData == null) continue;

					for (InteractionSyncData interactionDatum : update.interactionData) {
						if (interactionDatum == null || interactionDatum.entityId != this.networkId + 1) continue;

						UUID playerUuid = Objects.requireNonNull(handler.getAuth()).getUuid();
						if (this.interactionHandler != null) {
							this.interactionHandler.accept(playerUuid);
						}
					}
				}
			}
		});
	}

	public void despawn() {
		if (npcEntity != null) {
			npcEntity.setToDespawn();
		}
	}

	public NPCEntity getNpcEntity() {
		return npcEntity;
	}

	public Ref<EntityStore> getEntityRef() {
		return entityRef;
	}

	public int getNetworkId() {
		return networkId;
	}
}