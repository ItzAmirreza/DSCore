package me.prismskey.rpgcore.Events;


import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.Enums.PhaseState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class RegionEnterEvent implements Listener {

    @EventHandler
    public void onEnter(RegionEnteredEvent event) {

        Player player = event.getPlayer();
        String regionName = event.getRegionName();

        if (shortTermStorages.playersInMatch.containsKey(event.getPlayer().getName())) {

            Arena thatArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));

            for (Phase phase : thatArena.phases.values()) {

                if (phase.region.toLowerCase().equalsIgnoreCase(regionName.toLowerCase())) {

                    if (phase.state == PhaseState.LOCKED) {
                        event.setCancelled(true);
                        player.sendMessage(Utils.color("&cYou can not enter next phase unless you finish this phase."));
                    } else {

                        phase.addPlayer(player);
                        player.sendMessage("Debug: entered a phase");
                    }


                }

            }


        }

    }






}