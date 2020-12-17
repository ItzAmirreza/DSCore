package me.prismskey.rpgcore.ArenaManager;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.concurrent.ThreadLocalRandom;

public class SpawningSystem {

    public void spawn(String arenaName) {
        Arena arena = shortTermStorages.arenaHashMap.get(arenaName);

        Phase currentphase = arena.currentPhase;
        //int wave_count = currentphase.wavescount;
        //int current_wave = currentphase.current_wave;
        Location center = currentphase.center;
        int mobRange = currentphase.mobSpawnRange;

        for (DMob dMob : currentphase.mobs) {

            int chance = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            if (chance <= dMob.percentage) {
                Location spawnLocation = findTheRightLocation(center, mobRange);
                Entity theEnt = center.getWorld().spawnEntity(spawnLocation, dMob.getEntityType());
                shortTermStorages.arenaHashMap.get(arenaName).allMobsInArena.add(theEnt);
                shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.add(theEnt);
            }

        }



    }




    private Location findTheRightLocation(Location center, int mobRange) {
        //int random_int = (int)(Math.random() * (mobRange - (mobRange - mobRange * 2) + 1) + (mobRange - mobRange * 2));
        int XORY = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        int random_int = ThreadLocalRandom.current().nextInt((mobRange - mobRange * 2), mobRange + 1);
        String value = "X";
        if (XORY == 1) {
            value = "Y";
        }

        if (value.equals("X")) {

            Location newLocation = center.add(0, (double) random_int, 0);
            if (newLocation.getBlock().isEmpty()) {
                return newLocation;

            } else {

                return findTheRightLocation(center, mobRange);

            }
        } else {

            Location newLocation = center.add((double) random_int, 0, 0);
            if (newLocation.getBlock().isEmpty()) {

                return newLocation;

            } else {

                return findTheRightLocation(center, mobRange);

            }

        }


    }
}
