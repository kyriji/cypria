package dev.kyriji.cypria.hub;

import dev.kyriji.common.cypria.messages.MessageInstanceReady;
import org.bukkit.plugin.java.JavaPlugin;

public final class CypriaHub extends JavaPlugin {

    @Override
    public void onEnable() {
        MessageInstanceReady instanceReadyMessage = new MessageInstanceReady("xacasfwadasd");
        instanceReadyMessage.send(response -> {
            System.out.println(response.success);
        });


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
