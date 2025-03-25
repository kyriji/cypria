package dev.kyriji.cypria.manager.queuing.listeners;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.messaging.messages.MessageQueueRequest;
import dev.kyriji.common.cypria.messaging.models.MessageListener;
import dev.kyriji.cypria.manager.CypriaManager;

public class QueueListener {

	public QueueListener() {
		CypriaCommon.getMessageManager().addListener(new MessageListener<>(MessageQueueRequest.class, message -> {
			CypriaManager.queueManager.queuePlayer(message.getPlayerUUID(), message.getDeployment(), (success, successMessage) -> {
				message.respond(new MessageQueueRequest.Response(success, successMessage));
			});
		}));
	}
}
