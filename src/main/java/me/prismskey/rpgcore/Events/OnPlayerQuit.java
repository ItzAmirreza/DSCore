package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        FileConfiguration config = Rpgcore.instance.getPvpStatesConfiguration();
        RPGPlayerData data = Rpgcore.instance.getDataByUUID(event.getPlayer().getUniqueId());
        if(data == null) {
            Rpgcore.instance.getLogger().info("Error: onPlayerQuit data is null.");
            return;
        }
        config.set(event.getPlayer().getUniqueId().toString(), data.getPvpState());
        Rpgcore.instance.savePvpStatesConfig();
    }
}
