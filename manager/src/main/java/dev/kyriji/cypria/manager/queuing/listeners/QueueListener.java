package dev.kyriji.cypria.manager.queuing.listeners;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messaging.messages.MessageQueueRequest;
import dev.kyriji.common.cypria.messaging.models.MessageListener;
import dev.kyriji.cypria.manager.CypriaManager;

public class QueueListener {

	public QueueListener() {
		CypriaCommon.getMessageManager().addListener(new MessageListener<>(MessageQueueRequest.class, message -> {
				try {
					CypriaManager.queueManager.queuePlayer(
							message.getPlayerUUID(),
							message.getDeployment(),
							(success, successMessage) -> {
								message.respond(new MessageQueueRequest.Response(success, successMessage));
							}
					);
				} catch(Exception e) {
					e.printStackTrace();
					message.respond(new MessageQueueRequest.Response(false, "Processing failed: " + e.getMessage()));
				}
		}));
	}
}


