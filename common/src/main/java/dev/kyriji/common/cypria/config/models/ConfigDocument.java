package dev.kyriji.common.cypria.config.models;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.config.enums.ConfigType;

public abstract class ConfigDocument {
	protected ConfigType type;

	public ConfigDocument() {
	}

	public ConfigType getType() {
		return type;
	}

	public void setType(ConfigType type) {
		this.type = type;
	}

	public void save() {
		CypriaCommon.getConfigManager().saveConfig(this);
	}

}
