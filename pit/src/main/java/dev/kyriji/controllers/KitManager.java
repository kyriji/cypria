package dev.kyriji.controllers;

import dev.kyriji.kits.AxeKit;
import dev.kyriji.kits.BasicKit;
import dev.kyriji.kits.DaggersKit;
import dev.kyriji.objects.Kit;

import java.util.ArrayList;
import java.util.List;

public class KitManager {
	private final List<Kit> kits;

	public KitManager() {
		this.kits = new ArrayList<>();

		kits.add(new BasicKit());
		kits.add(new AxeKit());
		kits.add(new DaggersKit());
	}

	public List<Kit> getKits() {
		return kits;
	}

	public Kit getKitByName(String name) {
		return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}
