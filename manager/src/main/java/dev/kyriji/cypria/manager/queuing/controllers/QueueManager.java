package dev.kyriji.cypria.manager.queuing.controllers;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.common.cypria.messaging.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.models.CypriaInstance;
import dev.kyriji.cypria.manager.queuing.listeners.QueueListener;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class QueueManager {

	public QueueListener queueListener;

	public QueueManager() {
		this.queueListener = new QueueListener();
	}

	public void queuePlayer(UUID player, Deployment deployment, BiConsumer<Boolean, String> callback) {
		CypriaInstance instance = selectBestInstance(deployment);

		if(instance == null) {
			callback.accept(false, "No instances available");
			return;
		}

		MessageLoadPlayerData loadRequest = new MessageLoadPlayerData(instance.getAddress(), player);
		loadRequest.send(response -> {
			if(response.success) {
				//TODO: Use BMC API to queue player
				callback.accept(true, "Player queued");
			} else {
				callback.accept(false, "Failed to queue player");
			}
		});
	}

	public CypriaInstance selectBestInstance(Deployment deployment) {
		List<CypriaInstance> instances = CypriaCommon.getRedisManager().getInstances().stream()
				.filter(instance -> instance.getDeployment() == deployment).toList();

		System.out.println(instances);
		System.out.println("-------------------");
		CypriaCommon.getRedisManager().getInstances().forEach(instance -> {
			System.out.println(instance.getAddress() + " " + instance.getDeployment());
		});
		System.out.println("-------------------");


		if(instances.isEmpty()) return null;

		//TODO: Implement proper selection logic
		return instances.getFirst();
	}
}
