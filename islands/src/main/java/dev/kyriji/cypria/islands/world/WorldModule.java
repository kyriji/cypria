package dev.kyriji.cypria.islands.world;

import dev.kyriji.commonmc.cypria.model.CypriaModule;
import dev.kyriji.cypria.islands.world.controllers.WorldManager;

public class WorldModule extends CypriaModule {
	@Override
	public void init() {
		WorldManager.init();
	}
}
