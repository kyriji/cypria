package dev.kyriji.commonmc.cypria.controllers;

import dev.kyriji.commonmc.cypria.model.CypriaModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
	public static final List<CypriaModule> modules = new ArrayList<>();

	public ModuleManager() {}

	public void registerModule(CypriaModule module) {
		module.init();
		modules.add(module);
	}

	public void shutdown() {
		for (CypriaModule module : modules) module.shutdown();
	}
}
