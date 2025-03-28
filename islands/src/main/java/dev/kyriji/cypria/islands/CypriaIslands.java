package dev.kyriji.cypria.islands;

import dev.kyriji.common.cypria.CypriaCommon;
import dev.kyriji.common.cypria.enums.Deployment;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.commonmc.cypria.command.controllers.ConfigManager;
import dev.kyriji.cypria.islands.base.BaseModule;
import dev.kyriji.cypria.islands.commands.TestCommand;
import dev.kyriji.cypria.islands.world.WorldModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class CypriaIslands extends JavaPlugin {
    public static CypriaIslands INSTANCE;
    public static CypriaCommon cypriaCommon;

    @Override
    public void onEnable() {
        INSTANCE = this;

        cypriaCommon = new CypriaCommon(ConfigManager.getLocalConfig(getDataFolder()), Deployment.ISLANDS);

        new CypriaMinecraft.Builder(this)
                .addModules(
                        new BaseModule(),
                        new WorldModule()
                )
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
