package dev.kyriji.cypria.manager;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messages.MessageInstanceReady;
import dev.kyriji.common.cypria.models.MessageListener;

public class CypriaManager {
	public static void main(String[] args) {
		System.out.println("Cypria Manager starting...");

		new CypriaCommon();

		CypriaCommon.getMessageManager().addListener(new MessageListener<MessageInstanceReady>(message -> {
			message.respond(new MessageInstanceReady.InstanceReadyResponse(true));
		}));
	}
}