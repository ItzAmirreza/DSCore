package me.prismskey.rpgcore.Events;


import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.ArenaManager.SpawningSystem;
import me.prismskey.rpgcore.Enums.PhaseState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class RegionEnterEvent implements Listener {

    SpawningSystem spawningSystem = new SpawningSystem();

    @EventHandler
    public void onEnter(RegionEnteredEvent event) {

        Player player = event.getPlayer();
        String regionName = event.getRegionName();

        if (shortTermStorages.playersInMatch.containsKey(event.getPlayer().getName())) {

            Arena thatArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));

            /*for (Phase phase : thatArena.phases.values()) {

                if (phase.region.toLowerCase().equalsIgnoreCase(regionName.toLowerCase())) {

                    if (phase.state == PhaseState.LOCKED) {
                        event.setCancelled(true);
                        player.sendMessage(Utils.color("&cYou can not enter next phase unless you finish this phase."));
                    } else {

                        phase.addPlayer(player);
                        player.sendMessage("Debug: entered a phase");
                    }


                }

            }*/

            for(Phase phase : thatArena.phases.values()) {
                if(phase.region.toLowerCase().equalsIgnoreCase(regionName.toLowerCase())) {
                    if(phase.firstTime) {
                        spawningSystem.spawn(thatArena.name, phase);
                        shortTermStorages.arenaHashMap.get(thatArena.name).phases.get(phase.name).firstTime = false;
                        shortTermStorages.arenaHashMap.get(thatArena.name).announceToAllPlayers("&bA new wave of mobs has spawned!");
                    }
                }
            }

            /*if (thatArena.currentPhase.region.equalsIgnoreCase(regionName)) {
                if (thatArena.currentPhase.firstTime) {
                    spawningSystem.spawn(thatArena.name);
                    shortTermStorages.arenaHashMap.get(thatArena.name).currentPhase.firstTime = false;
                    shortTermStorages.arenaHashMap.get(thatArena.name).phases.get(thatArena.currentPhase.name).firstTime = false;
                    shortTermStorages.arenaHashMap.get(thatArena.name).announceToAllPlayers("&bYou have entered a new phase!");
                }
            }*/

        } else {

            if (!player.hasPermission("dscore.bypass")) {
                player.performCommand("spawn");
            }
        }

    }






}
