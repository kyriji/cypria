package dev.kyriji.cypria;

import dev.kyriji.cypria.messages.MessageInstanceReady;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hub extends JavaPlugin {

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
