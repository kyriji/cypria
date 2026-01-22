package dev.kyriji.common.models;

import dev.kyriji.common.HytaleCommon;
import dev.kyriji.common.enums.Deployment;

import java.util.*;

public class HytaleInstance {
	private final String address;
	private final Deployment deployment;
	private final Map<UUID, String> players;

	public HytaleInstance(String address, Deployment deployment) {
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

		HytaleCommon.getRedisManager().updateInstance(this);
	}

	public void removePlayer(UUID uuid) {
		players.remove(uuid);

		HytaleCommon.getRedisManager().updateInstance(this);
	}

	public void setPlayers(Map<UUID, String> players) {
		this.players.clear();
		this.players.putAll(players);
	}

	public void remove() {
		HytaleCommon.getRedisManager().removeInstance(this);
	}
}
