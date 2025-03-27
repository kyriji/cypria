package dev.kyriji.common.cypria.playerdata.models;


import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.playerdata.enums.PlayerDataType;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class PlayerDataDocument {
	protected String uuid;

	public PlayerDataDocument() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public CompletableFuture<Void> save() {
		return CompletableFuture.runAsync(() -> {
			CypriaCommon.getPlayerDataManager().savePlayerData(this, Objects.requireNonNull(PlayerDataType.fromClass(this.getClass())));
		});
	}
}