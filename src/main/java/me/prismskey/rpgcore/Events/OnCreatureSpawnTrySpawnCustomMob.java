package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.Enums.SpecialMobs;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.APIUsages;
import me.prismskey.rpgcore.Utils.NBTMobAPI;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.ArrayList;
import java.util.Random;

public class OnCreatureSpawnTrySpawnCustomMob implements Listener {

    private static Random r = new Random();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (Utils.checkIfInDungeon(event.getLocation())) {
            return;
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BEEHIVE ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BUILD_WITHER ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DISPENSE_EGG ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DROWNED ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.INFECTION ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.LIGHTNING ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SHEARED ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.JOCKEY ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.MOUNT ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.OCELOT_BABY) {
            return;
        }
        Location loc = event.getLocation();
        EntityType type = event.getEntityType();
        Biome biome = loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        ArrayList<SpecialMobs> potentialSpawns = new ArrayList<>();

        if (type == EntityType.SKELETON && (biome == Biome.DARK_FOREST || biome == Biome.DARK_FOREST_HILLS)) {
            potentialSpawns.add(SpecialMobs.DARK_ELF);
        }

        if (potentialSpawns.size() > 0) {
            if (r.nextInt(5) == 0) {
                event.setCancelled(true);
                SpecialMobs thatMob = potentialSpawns.get(r.nextInt(potentialSpawns.size()));
                if (loc.getWorld().getName().toLowerCase().contains("world")) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " run function _spawn:" + thatMob.getName());
                } else {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute in " + loc.getWorld().getName() + " positioned " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " run function _spawn:" + thatMob.getName());
                }
            }
        }
    }
}
