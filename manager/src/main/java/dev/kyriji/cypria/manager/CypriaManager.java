package dev.kyriji.cypria.manager;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.config.controllers.ConfigManager;
import dev.kyriji.common.cypria.messaging.enums.RunContext;
import dev.kyriji.common.cypria.messaging.messages.MessageInstanceReady;
import dev.kyriji.common.cypria.messaging.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.messaging.models.MessageListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import static dev.kyriji.common.cypria.CypriaCommon.gson;

public class CypriaManager {
	private static final String CONFIG_RESOURCE = "/config.json";

	public static void main(String[] args) {
		System.out.println("Cypria Manager starting...");

		new CypriaCommon(getLocalConfig(), RunContext.MANAGER);

		CypriaCommon.getMessageManager().addListener(new MessageListener<>(MessageInstanceReady.class, message -> {
			message.respond(new MessageInstanceReady.Response(true));

			System.out.println("Sending request");
			MessageLoadPlayerData messageLoadPlayerData = new MessageLoadPlayerData(UUID.randomUUID(), "0.0.0.0");
			messageLoadPlayerData.send(response -> {
				System.out.println("Response received");
				System.out.println(response.success);
			});
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