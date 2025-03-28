package dev.kyriji.cypria.hub;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.command.commands.dev.TestCommand;
import dev.kyriji.commonmc.cypria.command.controllers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CypriaHub extends JavaPlugin {
    public CypriaCommon cypriaCommon;

    @Override
    public void onEnable() {
		cypriaCommon = new CypriaCommon(ConfigManager.getLocalConfig(getDataFolder()), Deployment.HUB);

        new CypriaMinecraft.Builder(this)
                .addCommands(
                        new TestCommand()
                )
                .build();
    }


    @Override
    public void onDisable() {
        cypriaCommon.shutdown();
        CypriaMinecraft.get().shutdown();
    }
}
