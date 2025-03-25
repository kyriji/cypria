package dev.kyriji.cypria.manager;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.cypria.manager.queuing.controllers.QueueManager;
import dev.kyriji.cypria.manager.queuing.controllers.ServerRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static dev.kyriji.common.cypria.CypriaCommon.gson;

public class CypriaManager {
	private static final String CONFIG_RESOURCE = "/config.json";

	public static ServerRegistry serverRegistry;
	public static QueueManager queueManager;

	public static void main(String[] args) {
		CypriaCommon cypriaCommon = new CypriaCommon(getLocalConfig(), Deployment.MANAGER);

		serverRegistry = new ServerRegistry();
		queueManager = new QueueManager();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down CypriaManager...");
			cypriaCommon.shutdown();
		}));
	}

	public static JsonObject getLocalConfig() {
		try (InputStream is = CypriaManager.class.getResourceAsStream(CONFIG_RESOURCE)) {
			if(is == null) {
				throw new IllegalStateException("Config resource not found: " + CONFIG_RESOURCE);
			}

			try (JsonReader reader = new JsonReader(new InputStreamReader(is))) {
				return gson.fromJson(reader, JsonObject.class);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config from resources", e);
		}
	}
}