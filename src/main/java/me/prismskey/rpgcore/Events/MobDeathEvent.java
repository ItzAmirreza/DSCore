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

                Rpgcore.getInstance().getLogger().info(thatArena.name + " total killed mobs: " + thatArena.totalKilledMobs);

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

    }





}

