package dev.kyriji.commonmc.cypria.controllers;

import dev.kyriji.commonmc.cypria.model.CypriaModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
	public ModuleManager() {}

	public void registerModule(CypriaModule module) {
		module.init();
	}
}
