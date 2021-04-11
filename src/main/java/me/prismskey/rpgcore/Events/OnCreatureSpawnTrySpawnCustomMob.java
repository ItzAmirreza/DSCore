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
import java.util.HashMap;
import java.util.Random;

public class OnCreatureSpawnTrySpawnCustomMob implements Listener {

    private static Random r = new Random();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (Utils.checkIfInDungeon(event.getLocation()) || Utils.isCustomMob(event.getEntity())) {
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
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.OCELOT_BABY ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DEFAULT ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            return;
        }
        Location loc = event.getLocation();
        EntityType type = event.getEntityType();
        Biome biome = loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        HashMap<SpecialMobs, Integer> potentialSpawns = new HashMap<>();

        if (type == EntityType.SKELETON) {
            potentialSpawns.put(SpecialMobs.WEREWOLF, 5);
            if (biome == Biome.DARK_FOREST || biome == Biome.DARK_FOREST_HILLS) {
                potentialSpawns.put(SpecialMobs.DARK_ELF, 5);
            }
        }

        if(type == EntityType.PIG) {
            potentialSpawns.put(SpecialMobs.DRYAD, 40);
            if(biome == Biome.MOUNTAINS || biome == Biome.MOUNTAIN_EDGE || biome ==Biome.GRAVELLY_MOUNTAINS
             || biome == Biome.MODIFIED_GRAVELLY_MOUNTAINS || biome == Biome.TAIGA_MOUNTAINS || biome == Biome.WOODED_MOUNTAINS) {
                potentialSpawns.put(SpecialMobs.GRIFFIN, 40);
            }
        }
        if(type == EntityType.ZOMBIE) {
            potentialSpawns.put(SpecialMobs.BANSHEE, 9);
            //potentialSpawns.put(SpecialMobs.EARTH, 20);
            potentialSpawns.put(SpecialMobs.GHOST, 8);
            potentialSpawns.put(SpecialMobs.GOBLIN_SWORD, 7);
            potentialSpawns.put(SpecialMobs.GOBLIN_CHOPPER, 7);
            if(biome == Biome.MOUNTAINS || biome == Biome.MOUNTAIN_EDGE || biome ==Biome.GRAVELLY_MOUNTAINS
                    || biome == Biome.MODIFIED_GRAVELLY_MOUNTAINS || biome == Biome.TAIGA_MOUNTAINS || biome == Biome.WOODED_MOUNTAINS) {
                potentialSpawns.put(SpecialMobs.HARPY, 8);
                potentialSpawns.put(SpecialMobs.URUK_HAI, 8);
            }
            if(biome == Biome.SWAMP || biome == Biome.SWAMP_HILLS || biome == Biome.RIVER) {
                potentialSpawns.put(SpecialMobs.LIZARD_GUY, 10);
            }

        }
        if(type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER) {
            potentialSpawns.put(SpecialMobs.BASILISK, 10);
        }
        if(type == EntityType.PIGLIN) {
            //potentialSpawns.put(SpecialMobs.FIRE, 7);
        }

        if(type == EntityType.PILLAGER || type == EntityType.EVOKER || type == EntityType.VINDICATOR) {
            potentialSpawns.put(SpecialMobs.ILLUSIONER, 5);
        }
        if(type == EntityType.DROWNED) {
            potentialSpawns.put(SpecialMobs.NAGA, 10);
        }
        if(type == EntityType.HUSK) {
            potentialSpawns.put(SpecialMobs.MUMMY, 5);
            potentialSpawns.put(SpecialMobs.SCARAB, 5);
        }
        if(type == EntityType.HUSK || type == EntityType.PIGLIN) {
            potentialSpawns.put(SpecialMobs.PHOENIX, 10);
        }



        for(SpecialMobs mob: potentialSpawns.keySet()) {
            if(r.nextInt(potentialSpawns.get(mob)) == 0) {
                event.setCancelled(true);
                spawnCustomMob(loc, mob);
                break;
            }
        }
    }

    private void spawnCustomMob(Location loc, SpecialMobs thatMob) {
        if (loc.getWorld().getName().toLowerCase().contains("world")) {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " run function _spawn:" + thatMob.getName());
        } else {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute in " + loc.getWorld().getName() + " positioned " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " run function _spawn:" + thatMob.getName());
        }
    }
}
