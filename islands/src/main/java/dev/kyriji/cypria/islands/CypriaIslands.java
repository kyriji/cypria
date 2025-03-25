package dev.kyriji.cypria.islands;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.command.controllers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CypriaIslands extends JavaPlugin {
    public CypriaCommon cypriaCommon;

    @Override
    public void onEnable() {
        cypriaCommon = new CypriaCommon(ConfigManager.getLocalConfig(getDataFolder()), Deployment.ISLANDS);
        CypriaMinecraft.init(this);
    }

    @Override
    public void onDisable() {
        cypriaCommon.shutdown();
        CypriaMinecraft.shutdown();
    }
}
