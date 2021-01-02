package me.prismskey.rpgcore.ArenaManager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.prismskey.rpgcore.DataManager.MobsLevelsConfigManager;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class SpawningSystem {
    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    MobsLevelsConfigManager mlcm = new MobsLevelsConfigManager();



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
                Location spawnLocation = findTheRightLocation(center, mobRange, arena);
                int damage = mlcm.getMobDamage(dMob.getEntityType().name(), String.valueOf(dMob.level));
                int health = mlcm.getMobHealth(dMob.getEntityType().name(), String.valueOf(dMob.level));
                Entity theEnt = center.getWorld().spawnEntity(spawnLocation, dMob.getEntityType());
                assignEntityData(theEnt, dMob.level, damage, health);
                shortTermStorages.arenaHashMap.get(arenaName).allMobsInArena.add(theEnt);
                shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.add(theEnt);
            }



        }



    }




    private Location findTheRightLocation(Location center, int mobRange, Arena thatArena) {

        //int random_int = (int)(Math.random() * (mobRange - (mobRange - mobRange * 2) + 1) + (mobRange - mobRange * 2));
        int XORY = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        int random_int = ThreadLocalRandom.current().nextInt((mobRange - mobRange * 2), mobRange + 1);
        //int random_Y = ThreadLocalRandom.current().nextInt(center.getBlockY() - 2, center.getBlockY() + 3 + 1);
        String value = "X";
        if (XORY == 1) {
            value = "Z";
        }

        if (value.equals("X")) {

            Location newLocation = center.add((double) random_int, 0, 0); //random_Y
            if (newLocation.getBlock().isEmpty()) {

                //if (newLocation.subtract(0, (double) 1, 0).getBlock().isEmpty()) {

                //    int num = newLocation.getBlockY();
                //    Location lastloc = newLocation;
               //     while (true) {
                //        lastloc.setY(num);
                //        if (lastloc.getBlock().isEmpty()) {
                //            num = num - 1;
                //        } else {
                //            lastloc.setY(num + 1);
                //            break;
                //        }
                //    }

                //    return lastloc;

                //} else {

                if (checkIFInTheRightRegion(newLocation, thatArena)) {
                    return newLocation;
                } else {

                    return findTheRightLocation(center, mobRange, thatArena);

                }
                //}
            } else {

                return findTheRightLocation(center, mobRange, thatArena);

            }
        } else {

            Location newLocation = center.add(0, 0, (double) random_int); //(double) random_Y

            if (newLocation.getBlock().isEmpty()) {
                if (checkIFInTheRightRegion(newLocation, thatArena)) {
                    return newLocation;
                } else {
                    return findTheRightLocation(center, mobRange, thatArena);
                }

/**
                if (newLocation.subtract(0, (double) 1, 0).getBlock().isEmpty()) {
                    int num = newLocation.getBlockY();
                    Location lastloc = newLocation;
                    while (true) {
                        lastloc.setY(num);
                        if (lastloc.getBlock().isEmpty()) {
                            num = num - 1;
                        } else {
                            lastloc.setY(num + 1);
                            break;
                        }
                    }

                    return lastloc;

                } else {

                    return newLocation;

                }
             */

            } else {

                return findTheRightLocation(center, mobRange, thatArena);

            }

        }


    }


    private boolean checkIFInTheRightRegion(Location thatLocation, Arena thatArena) {
        BukkitAdapter.adapt(thatLocation);
        RegionManager regions = container.get(BukkitAdapter.adapt(thatLocation.getWorld()));
        boolean result = regions.getRegion(thatArena.currentPhase.region).contains(thatLocation.getBlockX(), thatLocation.getBlockY(), thatLocation.getBlockZ());
        if (result) {
            return true;
        } else {
            return false;
        }

    }


    private void assignEntityData(Entity thatEntity, int level, int damage, int health) {

        LivingEntity theEntity = (LivingEntity) thatEntity;
        theEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.valueOf(damage));
        theEntity.setMaxHealth(health);
        theEntity.setHealth(health);
        theEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(Double.valueOf(damage));
        theEntity.setCustomName(Utils.color("&e◈ &6Level " + level + " &e◈"));
        theEntity.setCustomNameVisible(true);

    }



}
