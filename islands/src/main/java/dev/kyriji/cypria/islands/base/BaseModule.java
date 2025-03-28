package dev.kyriji.cypria.islands.base;

import dev.kyriji.cypria.islands.base.building.BuildingModule;
import dev.kyriji.cypria.islands.base.controllers.BaseManager;
import dev.kyriji.cypria.islands.base.models.Base;
import dev.kyriji.commonmc.cypria.model.CypriaModule;

import java.util.ArrayList;

public class BaseModule extends CypriaModule {
	@Override
	public void init() {
		registerSubModule(new BuildingModule());
	}

	@Override
	public void onShutdown() {
		new ArrayList<>(BaseManager.baseList).forEach(Base::destroy);
	}
}
