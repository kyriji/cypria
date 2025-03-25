package dev.kyriji.common.cypria.playerdata.models;


import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.playerdata.enums.PlayerDataType;

import java.util.Objects;

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

	public void save() {
		CypriaCommon.getPlayerDataManager().savePlayerData(this, Objects.requireNonNull(PlayerDataType.fromClass(this.getClass())));
	}
}