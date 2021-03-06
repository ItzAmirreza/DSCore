package me.prismskey.rpgcore.ArenaManager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.prismskey.rpgcore.DataManager.MobsLevelsConfigManager;
import me.prismskey.rpgcore.Enums.SpecialMobs;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.APIUsages;
import me.prismskey.rpgcore.Utils.NBTMobAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SpawningSystem {
    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    MobsLevelsConfigManager mlcm = new MobsLevelsConfigManager();
    Random r = new Random();


    public void spawn(String arenaName, Phase phase) {
        Arena arena = shortTermStorages.arenaHashMap.get(arenaName);

        //Phase targetPhase = arena.currentPhase;
        //int wave_count = currentphase.wavescount;
        //int current_wave = currentphase.current_wave;
        Location center = phase.center;
        int mobRange = phase.mobSpawnRange;

        for (String cmd : phase.startCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }

        for (DMob dMob : phase.mobs) {

            if (!dMob.isSpecial) {
                int chance = ThreadLocalRandom.current().nextInt(0, 100 + 1);
                if (chance <= dMob.percentage) {
                    Location spawnLocation = findTheRightLocation(center, mobRange, arena, phase);
                    int damage = mlcm.getMobDamage(dMob.getEntityType().name(), String.valueOf(dMob.level));
                    int health = mlcm.getMobHealth(dMob.getEntityType().name(), String.valueOf(dMob.level));
                    Entity theEnt = center.getWorld().spawnEntity(spawnLocation, dMob.getEntityType());

                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "arena"), PersistentDataType.STRING, arenaName);
                    //theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "level"), PersistentDataType.INTEGER, dMob.level);
                    //theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "name"), PersistentDataType.STRING, dMob.mob);


                    if (dMob.isBoss) {
                        theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "isBoss"), PersistentDataType.INTEGER, 1);
                        theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING, phase.name);
                        shortTermStorages.arenaHashMap.get(arenaName).phases.get(phase.name).bossMobsRemaining++;
                    }
                    if (dMob.isFinalBoss) {
                        theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "isFinalBoss"), PersistentDataType.INTEGER, 1);
                        theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING, phase.name);
                        shortTermStorages.arenaHashMap.get(arenaName).phases.get(phase.name).finalBossMobsRemaining++;
                    }


                    //assignEntityData(theEnt, dMob.level, damage, health, dMob.mob);
                    //shortTermStorages.arenaHashMap.get(arenaName).allMobsInArena.add(theEnt);
                    LivingEntity living = (LivingEntity) theEnt;
                    living.setRemoveWhenFarAway(false);
                    //shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.add(theEnt);
                }
            } else {

                int chance = ThreadLocalRandom.current().nextInt(0, 100 + 1);
                if (chance <= dMob.percentage) {

                    Location spawnLocation = findTheRightLocation(center, mobRange, arena, phase);
                    int damage = mlcm.getMobDamage(dMob.mob, String.valueOf(dMob.level));
                    int health = mlcm.getMobHealth(dMob.mob, String.valueOf(dMob.level));

                    //spawn that entity
                    SpecialMobs thatmob = SpecialMobs.valueOf(dMob.mob);
                    //Entity specialMob = spawnSpecialMob(thatmob, spawnLocation);

                    if (spawnLocation.getWorld().getName().equalsIgnoreCase("world")) {

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + spawnLocation.getX() + " " + spawnLocation.getY() + " " + spawnLocation.getZ() + " run function _spawn:" + thatmob.getName());

                    } else {

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute in " + spawnLocation.getWorld().getName() + " positioned " + spawnLocation.getX() + " " + spawnLocation.getY() + " " + spawnLocation.getZ() + " run function _spawn:" + thatmob.getName());
                    }


                    //get that special entity method
                    Bukkit.getScheduler().runTaskLater(Rpgcore.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            //ArrayList<Entity> theEnts = getSpecialEntity(spawnLocation);
                            applyPersistentDataToNearbyArenaMobsAndSetRemoveWhenFarAwayFalse(spawnLocation, arenaName);
                            //theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "arena"), PersistentDataType.STRING, arenaName);
                            //theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "level"), PersistentDataType.INTEGER, dMob.level);
                            //theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "name"), PersistentDataType.STRING, dMob.mob);

                            if(dMob.isBoss) {
                                ArrayList<Entity> bosses = getBossEntities(spawnLocation);
                                for (Entity theEnt : bosses) {
                                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "isBoss"), PersistentDataType.INTEGER, 1);

                                    if(!theEnt.getPersistentDataContainer().has(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING)) {
                                        theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING, phase.name);
                                    }

                                    shortTermStorages.arenaHashMap.get(arenaName).phases.get(phase.name).bossMobsRemaining++;
                                }
                            }

                            if (dMob.isFinalBoss) {
                                ArrayList<Entity> finalBosses = getFinalBossEntities(spawnLocation);
                                for (Entity theEnt : finalBosses) {
                                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "isFinalBoss"), PersistentDataType.INTEGER, 1);

                                    if(!theEnt.getPersistentDataContainer().has(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING)) {
                                        theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "homePhase"), PersistentDataType.STRING, phase.name);
                                    }

                                    shortTermStorages.arenaHashMap.get(arenaName).phases.get(phase.name).finalBossMobsRemaining++;
                                }
                            }


                            //assignEntityData(theEnt, dMob.level, damage, health, dMob.mob);
                            //shortTermStorages.arenaHashMap.get(arenaName).allMobsInArena.add(theEnt);
                            //LivingEntity living = (LivingEntity) theEnt;
                            //living.setRemoveWhenFarAway(false);
                            //shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.add(theEnt);
                        }
                    }, 2);

                }

            }
            shortTermStorages.arenaHashMap.get(arenaName).totalMobs++;
        }


    }

    private void applyPersistentDataToNearbyArenaMobsAndSetRemoveWhenFarAwayFalse(Location loc, String arenaName) {
        for (Entity e : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
            if (!(e instanceof Player) && e instanceof LivingEntity) {
                e.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "arena"), PersistentDataType.STRING, arenaName);
                ((LivingEntity) e).setRemoveWhenFarAway(false);
            }
        }
    }


    private ArrayList<Entity> getBossEntities(Location location) {
        ArrayList<Entity> finalList = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<>(location.getWorld().getNearbyEntities(location, 10, 10d, 10d));
        for (Entity entity : entities) {
            if (!(entity instanceof Player)) {
                if (APIUsages.hasMobNBT(entity, "targetBoss")) {
                    finalList.add(entity);
                }
            }
        }

        return finalList;

    }

    private ArrayList<Entity> getFinalBossEntities(Location location) {
        ArrayList<Entity> finalList = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<>(location.getWorld().getNearbyEntities(location, 10, 10d, 10d));
        for (Entity entity : entities) {
            if (!(entity instanceof Player)) {
                if (APIUsages.hasMobNBT(entity, "targetFinalBoss")) {
                    finalList.add(entity);
                }
            }
        }

        return finalList;

    }


    private Location findTheRightLocation(Location center, int mobRange, Arena thatArena, Phase thatPhase) {
        int low = mobRange - (mobRange * 2);
        int high = mobRange;

        int resultx = r.nextInt(high - low) + low;
        int resultz = r.nextInt(high - low) + low;


        Location location = center.clone().add(resultx, 0, resultz);

        if (checkIFInTheRightRegion(location, thatPhase)) {
            return location;

        } else {
            return findTheRightLocation(center, mobRange, thatArena, thatPhase);
        }


    }


    private boolean checkIFInTheRightRegion(Location thatLocation, Phase targetPhase) {
        //ensure mob does not spawn inside wall such as spiders.
        for (int x = thatLocation.getBlockX() - 1; x <= thatLocation.getBlockX() + 1; x++) {
            for (int z = thatLocation.getBlockZ() - 1; z <= thatLocation.getBlockZ() + 1; z++) {
                Location test = new Location(thatLocation.getWorld(), x, thatLocation.getBlockY(), z);
                if (!(test.getBlock().getType() == Material.WATER || test.getBlock().isEmpty())) {
                    return false;
                }
            }
        }
        BukkitAdapter.adapt(thatLocation);
        RegionManager regions = container.get(BukkitAdapter.adapt(thatLocation.getWorld()));
        //boolean result = regions.getRegion(thatArena.currentPhase.region).contains(thatLocation.getBlockX(), thatLocation.getBlockY(), thatLocation.getBlockZ());
        boolean result = regions.getRegion(targetPhase.region).contains(thatLocation.getBlockX(), thatLocation.getBlockY(), thatLocation.getBlockZ());
        if (result) {
            return true;
        } else {
            return false;
        }

    }

    private void assignEntityData(Entity thatEntity, int level, int damage, int health, String name) {

        /*LivingEntity theEntity = (LivingEntity) thatEntity;
        theEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.valueOf(damage));
        theEntity.setMaxHealth(health);
        theEntity.setHealth(health);
        theEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(Double.valueOf(damage));
        theEntity.setCustomName(Utils.color("&7[&eLvl&7:&6" + level + "&7] &e" + name + " &7[&cHealth " + health + "/Max Health " + health + "&7]"));
        theEntity.setCustomNameVisible(true);
        //[Lvl:#] Name [Health #/Max Health #]*/
        /*LivingEntity living = (LivingEntity) thatEntity;
        int maxhealth = (int) living.getMaxHealth();
        int currenthealth = (int) living.getHealth();
        //int level = entity.getPersistentDataContainer().get(new NamespacedKey(Rpgcore.getInstance(), "level"), PersistentDataType.INTEGER);
        //String name = entity.getPersistentDataContainer().get(new NamespacedKey(Rpgcore.getInstance(), "name"), PersistentDataType.STRING);

        living.setCustomName(Utils.color("&7" + currenthealth + "/" + maxhealth + "&7]"));*/
    }


}
