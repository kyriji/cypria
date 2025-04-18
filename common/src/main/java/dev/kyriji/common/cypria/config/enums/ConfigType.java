package dev.kyriji.common.cypria.config.enums;

import dev.kyriji.common.cypria.config.documents.CoreConfig;
import dev.kyriji.common.cypria.config.models.ConfigDocument;

public enum ConfigType {

	CORE(CoreConfig.class),
	;



	private final Class<? extends ConfigDocument> documentClass;

	ConfigType(Class<? extends ConfigDocument> documentClass) {
		this.documentClass = documentClass;
	}

	public Class<? extends ConfigDocument> getDocumentClass() {
		return documentClass;
	}

	public static ConfigType fromClass(Class<? extends ConfigDocument> clazz) {
		for(ConfigType type : values()) {
			if(type.getDocumentClass().equals(clazz)) {
				return type;
			}
		}
		return null;
	}
}
