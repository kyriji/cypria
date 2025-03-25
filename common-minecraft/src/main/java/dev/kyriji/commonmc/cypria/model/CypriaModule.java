package dev.kyriji.commonmc.cypria.model;

import java.util.ArrayList;
import java.util.List;

public abstract class CypriaModule {
	private final List<CypriaModule> subModules = new ArrayList<>();

	public abstract void init();

	public void onShutdown() {}

	public void shutdown() {
		for (CypriaModule module : subModules) module.shutdown();
		onShutdown();
	}

	public void registerSubModule(CypriaModule module) {
		module.init();
		subModules.add(module);
	}
}
