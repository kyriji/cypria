package dev.kyriji.cypria.manager.queuing.controllers;

import dev.kyriji.bigminecraftapi.BigMinecraftAPI;
import dev.kyriji.bigminecraftapi.objects.MinecraftInstance;
import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.common.cypria.models.CypriaInstance;

import java.util.*;

public class ServerRegistry {

	public ServerRegistry() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				removeStaleInstances();
			}
		}, 0, 1000 * 10);
	}

	public void removeStaleInstances() {
		List<CypriaInstance> cypriaInstances = CypriaCommon.getRedisManager().getInstances();
		List<MinecraftInstance> minecraftInstances = new ArrayList<>();

		for(Deployment value : Deployment.values()) {
			List<MinecraftInstance> deploymentInstances = BigMinecraftAPI.getNetworkManager().getInstances(value.getDeploymentName());
			minecraftInstances.addAll(deploymentInstances);
		}

		for(CypriaInstance cypriaInstance : cypriaInstances) {
			if(minecraftInstances.stream().noneMatch(instance -> instance.getIp().equals(cypriaInstance.getAddress()))) {
				cypriaInstance.remove();
			}
		}
	}

}
