package dev.kyriji.cypria.manager;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messaging.enums.RunContext;
import dev.kyriji.common.cypria.messaging.messages.MessageInstanceReady;
import dev.kyriji.common.cypria.messaging.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.messaging.models.MessageListener;

import java.util.UUID;

public class CypriaManager {
	public static void main(String[] args) {
		System.out.println("Cypria Manager starting...");

		new CypriaCommon(RunContext.MANAGER);

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
}