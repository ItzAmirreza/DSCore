package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import java.util.ArrayList;
import java.util.Collection;

public class MobDeathEvent implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

        if (!(e.getEntity() instanceof Player)) {

            Player killer = e.getEntity().getKiller();

            if (killer != null) {

                if (shortTermStorages.playersInMatch.containsKey(killer.getName())) {

                    String arenaName = shortTermStorages.playersInMatch.get(killer.getName());

                    Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
                    boolean result = false;
                    for (Entity entity : thatArena.currentPhase.spawnedEntities) {
                        if (!(entity.isDead())) {
                            result = true;
                        } else {
                            entity.isDead();
                            shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.get(shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.indexOf(entity)).remove();
                        }

                    }

                    if (!result) {

                        onFinishedWave waveEvent = new onFinishedWave(shortTermStorages.arenaHashMap.get(arenaName), shortTermStorages.arenaHashMap.get(arenaName).currentPhase.current_wave, shortTermStorages.arenaHashMap.get(arenaName).currentPhase);
                        Bukkit.getPluginManager().callEvent(waveEvent);

                    }

                }

            }

        }

    }



}

