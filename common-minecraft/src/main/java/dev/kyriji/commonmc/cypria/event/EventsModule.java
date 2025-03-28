package dev.kyriji.commonmc.cypria.event;

import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.event.controllers.EventsManager;
import dev.kyriji.commonmc.cypria.model.CypriaModule;
import org.bukkit.Bukkit;

public class EventsModule extends CypriaModule {
	@Override
	public void init() {
		Bukkit.getPluginManager().registerEvents(new EventsManager(), CypriaMinecraft.plugin());
	}
}
