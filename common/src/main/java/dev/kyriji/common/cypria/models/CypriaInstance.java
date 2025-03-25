package dev.kyriji.common.cypria.models;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;

import java.util.*;

public class CypriaInstance {
	private final String address;
	private final Deployment deployment;
	private final Map<UUID, String> players;

	public CypriaInstance(String address, Deployment deployment) {
		this.address = address;
		this.deployment = deployment;

		this.players = new HashMap<>();
	}

	public String getAddress() {
		return address;
	}

	public Deployment getDeployment() {
		return deployment;
	}

	public Map<UUID, String> getPlayers() {
		return players;
	}

	public void addPlayer(UUID uuid, String name) {
		players.put(uuid, name);

		CypriaCommon.getRedisManager().updateInstance(this);
	}

	public void removePlayer(UUID uuid) {
		players.remove(uuid);

		CypriaCommon.getRedisManager().updateInstance(this);
	}

	public void setPlayers(Map<UUID, String> players) {
		this.players.clear();
		this.players.putAll(players);

		CypriaCommon.getRedisManager().updateInstance(this);
	}

	public void remove() {
		CypriaCommon.getRedisManager().removeInstance(this);
	}
}
