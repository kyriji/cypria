package dev.kyriji.cypria;

import dev.kyriji.cypria.controllers.MessageManager;
import dev.kyriji.cypria.messages.MessageInstanceReady;
import dev.kyriji.cypria.models.MessageListener;

public class CypriaManager {
	public static void main(String[] args) {
		System.out.println("Cypria Manager starting...");

		new CypriaCommon();

		CypriaCommon.getMessageManager().addListener(new MessageListener<MessageInstanceReady>(message -> {
			message.respond(new MessageInstanceReady.InstanceReadyResponse(true));
		}));
	}
}