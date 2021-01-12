package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {


    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (shortTermStorages.playersInMatch.containsKey(event.getPlayer().getName())) {

            event.getPlayer().performCommand("spawn");

        }
    }
}
