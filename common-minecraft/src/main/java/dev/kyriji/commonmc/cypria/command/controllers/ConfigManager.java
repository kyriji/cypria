package dev.kyriji.commonmc.cypria.command.controllers;

import com.google.gson.JsonObject;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;

import java.io.*;

import static dev.kyriji.common.cypria.CypriaCommon.gson;

public class ConfigManager {
	public static JsonObject getLocalConfig(File dataFolder) {
		if(!dataFolder.exists()) dataFolder.mkdirs();

		String configExamplePath = "/config.example.json";

		File configFile = new File(dataFolder, "config.json");

		if(!configFile.exists()) {
			try {
				configFile.createNewFile();

				try (InputStream is = CypriaMinecraft.class.getResourceAsStream(configExamplePath)) {
					if(is == null) {
						throw new IllegalStateException("Config resource not found: " + configExamplePath);
					}

					try (FileOutputStream fos = new FileOutputStream(configFile)) {
						byte[] buffer = new byte[1024];
						int length;
						while ((length = is.read(buffer)) > 0) {
							fos.write(buffer, 0, length);
						}
					}
				}
			} catch(IOException e) {
				throw new RuntimeException("Failed to create config file or copy example config", e);
			}
		}

		try (FileReader reader = new FileReader(configFile)) {
			return gson.fromJson(reader, JsonObject.class);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read config file", e);
		}
	}
}
