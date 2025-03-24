package dev.kyriji.cypria.hub;

import dev.kyriji.common.cypria.messaging.CypriaCommon;
import dev.kyriji.common.cypria.enums.RunContext;
import dev.kyriji.common.cypria.messages.MessageInstanceReady;
import dev.kyriji.common.cypria.messages.MessageLoadPlayerData;
import dev.kyriji.common.cypria.models.MessageListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CypriaHub extends JavaPlugin {

    @Override
    public void onEnable() {
        new CypriaCommon(RunContext.PLUGIN);

        MessageInstanceReady instanceReadyMessage = new MessageInstanceReady();
        instanceReadyMessage.send(response -> {
            System.out.println("Response received");
            System.out.println(response.success);
        });

        CypriaCommon.getMessageManager().addListener(new MessageListener<>(MessageLoadPlayerData.class, message -> {
            System.out.println("Attempting to load playerdata for " + message.getPlayerUUID());
            message.respond(new MessageLoadPlayerData.Response(false));
        }));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
