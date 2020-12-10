package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.DataManager.ConfigLoader;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {


    public ConfigLoader configloader = new ConfigLoader();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        FileConfiguration config = shortTermStorages.pvpStatesConfiguration;
        RPGPlayerData data = configloader.getDataByUUID(event.getPlayer().getUniqueId());
        if(data == null) {
            Rpgcore.instance.getLogger().info("Error: onPlayerQuit data is null.");
            return;
        }
        config.set(event.getPlayer().getUniqueId().toString(), data.getPvpState());
        configloader.savePvpStatesConfig();
    }
}
