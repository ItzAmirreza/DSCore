package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import net.raidstone.wgevents.events.RegionLeftEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionQuitEvent implements Listener {

    @EventHandler
    public void onQuit(RegionLeftEvent event) {

        Player player = event.getPlayer();
        String regionName = event.getRegionName();

        if (shortTermStorages.playersInMatch.containsKey(event.getPlayer().getName())) {

            Arena thatArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));

            for (Phase phase : thatArena.phases.values()) {

                if (phase.region.toLowerCase().equalsIgnoreCase(regionName.toLowerCase())) {

                    phase.removePlayer(player);
                    player.sendMessage("Debug: left a phase");
                }

            }


        } /*else {
            if (!player.hasPermission("dscore.bypass")) {
                player.performCommand("spawn");
            }
        }*/


    }

}
