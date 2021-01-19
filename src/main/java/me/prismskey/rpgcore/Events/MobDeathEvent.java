package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MobDeathEvent implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

            if (e.getEntity().getPersistentDataContainer().has(new NamespacedKey(Rpgcore.getInstance(), "arena"), PersistentDataType.STRING)) {

                String arenaName = e.getEntity().getPersistentDataContainer().get(new NamespacedKey(Rpgcore.getInstance(), "arena"), PersistentDataType.STRING);

                Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
                thatArena.totalKilledMobs++;

                if(e.getEntity().getPersistentDataContainer().has(new NamespacedKey(Rpgcore.getInstance(), "isBoss"), PersistentDataType.INTEGER)) {
                    String homePhaseName = e.getEntity().getPersistentDataContainer().get(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING);
                    shortTermStorages.arenaHashMap.get(arenaName).phases.get(homePhaseName).bossMobsRemaining--;
                    Rpgcore.getInstance().getLogger().info("value: " + shortTermStorages.arenaHashMap.get(arenaName).phases.get(homePhaseName).bossMobsRemaining);
                    if(shortTermStorages.arenaHashMap.get(arenaName).phases.get(homePhaseName).bossMobsRemaining == 0) {
                        for(String cmd: shortTermStorages.arenaHashMap.get(arenaName).phases.get(homePhaseName).endCommands) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        }
                        thatArena.announceToAllPlayers(Utils.color("&aSection cleared! You may now proceed."));
                    }
                }

                if(e.getEntity().getPersistentDataContainer().has(new NamespacedKey(Rpgcore.getInstance(), "isFinalBoss"), PersistentDataType.INTEGER)) {
                    String homePhaseName = e.getEntity().getPersistentDataContainer().get(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING);
                    shortTermStorages.arenaHashMap.get(arenaName).phases.get(homePhaseName).finalBossMobsRemaining--;
                    if(shortTermStorages.arenaHashMap.get(arenaName).phases.get(homePhaseName).finalBossMobsRemaining == 0) {
                        //TODO: end the arena
                    }
                }


                /*boolean result = false;
                for (Entity entity : thatArena.currentPhase.spawnedEntities) {
                    if (!(entity.isDead())) {
                        result = true;
                    } else {

                        entity.isDead();
                        shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.get(shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.indexOf(entity)).remove();
                        shortTermStorages.arenaHashMap.get(arenaName).phases.get(thatArena.currentPhase.name).spawnedEntities.get(shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.indexOf(entity)).remove();
                    }

                }

                if (!result) {

                    onFinishedWave waveEvent = new onFinishedWave(shortTermStorages.arenaHashMap.get(arenaName), shortTermStorages.arenaHashMap.get(arenaName).currentPhase.current_wave, shortTermStorages.arenaHashMap.get(arenaName).currentPhase);
                    Bukkit.getPluginManager().callEvent(waveEvent);

                }*/

            }

    }





}

