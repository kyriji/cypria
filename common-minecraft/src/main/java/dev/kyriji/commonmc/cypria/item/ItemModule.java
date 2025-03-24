package dev.kyriji.commonmc.cypria.item;

import dev.kyriji.commonmc.cypria.item.abilities.ItemAbilitiesModule;
import dev.kyriji.commonmc.cypria.item.controllers.ItemManager;
import dev.kyriji.commonmc.cypria.item.stats.ItemStatsModule;
import dev.kyriji.commonmc.cypria.model.CypriaModule;

public class ItemModule extends CypriaModule {

	@Override
	public void init() {
		registerSubModule(new ItemAbilitiesModule());
		registerSubModule(new ItemStatsModule());

		ItemManager.init();
	}
}
