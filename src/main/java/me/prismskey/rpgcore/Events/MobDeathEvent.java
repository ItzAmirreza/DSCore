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

        if (!(e.getEntity() instanceof Player)) {

            Player killer = e.getEntity().getKiller();

            if (e.getEntity().getPersistentDataContainer().has(new NamespacedKey(Rpgcore.getInstance(), "isspecial"), PersistentDataType.INTEGER)) {


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
                                shortTermStorages.arenaHashMap.get(arenaName).phases.get(thatArena.currentPhase.name).spawnedEntities.get(shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.indexOf(entity)).remove();
                            }

                        }

                        if (!result) {

                            onFinishedWave waveEvent = new onFinishedWave(shortTermStorages.arenaHashMap.get(arenaName), shortTermStorages.arenaHashMap.get(arenaName).currentPhase.current_wave, shortTermStorages.arenaHashMap.get(arenaName).currentPhase);
                            Bukkit.getPluginManager().callEvent(waveEvent);

                        }

                    }

                } else {
                    List<Entity> entityList = e.getEntity().getNearbyEntities(30, 30, 30);
                    for (Entity entityy : entityList) {
                        if (entityy instanceof Player) {
                            Rpgcore.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&aYes containts player"));
                            Player player = (Player) entityy;
                            Rpgcore.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&agot the player man"));
                            if (shortTermStorages.playersInMatch.containsKey(player.getName())) {
                                Rpgcore.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&aYes in the arena"));
                                String thatArenastr = shortTermStorages.playersInMatch.get(player.getName());
                                Arena thatArena = shortTermStorages.arenaHashMap.get(thatArenastr);
                                boolean result = false;
                                for (Entity entity : thatArena.currentPhase.spawnedEntities) {
                                    if (!(entity.isDead())) {
                                        result = true;
                                    } else {

                                        entity.isDead();
                                        shortTermStorages.arenaHashMap.get(thatArenastr).currentPhase.spawnedEntities.get(shortTermStorages.arenaHashMap.get(thatArenastr).currentPhase.spawnedEntities.indexOf(entity)).remove();
                                        shortTermStorages.arenaHashMap.get(thatArenastr).phases.get(thatArena.currentPhase.name).spawnedEntities.get(shortTermStorages.arenaHashMap.get(thatArenastr).currentPhase.spawnedEntities.indexOf(entity)).remove();
                                    }

                                }

                                if (!result) {
                                    Rpgcore.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&egoing for next"));

                                    onFinishedWave waveEvent = new onFinishedWave(shortTermStorages.arenaHashMap.get(thatArenastr), shortTermStorages.arenaHashMap.get(thatArenastr).currentPhase.current_wave, shortTermStorages.arenaHashMap.get(thatArenastr).currentPhase);
                                    Bukkit.getPluginManager().callEvent(waveEvent);

                                }
                                break;

                            }

                        }

                    }
                }



            }


        }



    }





}

