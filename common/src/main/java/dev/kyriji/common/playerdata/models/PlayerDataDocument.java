package dev.kyriji.common.playerdata.models;


import dev.kyriji.common.HytaleCommon;
import dev.kyriji.common.playerdata.enums.PlayerDataType;

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
		PlayerDataType type = PlayerDataType.get(this.getClass());
		return HytaleCommon.getPlayerDataManager().savePlayerData(this, type);
	}
}
