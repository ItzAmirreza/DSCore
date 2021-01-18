package me.prismskey.rpgcore.ArenaManager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.prismskey.rpgcore.DataManager.MobsLevelsConfigManager;
import me.prismskey.rpgcore.Enums.SpecialMobs;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

        for (DMob dMob : phase.mobs) {

            if (!dMob.isSpecial) {
                int chance = ThreadLocalRandom.current().nextInt(0, 100 + 1);
                if (chance <= dMob.percentage) {
                    Location spawnLocation = findTheRightLocation(center, mobRange, arena, phase);
                    int damage = mlcm.getMobDamage(dMob.getEntityType().name(), String.valueOf(dMob.level));
                    int health = mlcm.getMobHealth(dMob.getEntityType().name(), String.valueOf(dMob.level));
                    Entity theEnt = center.getWorld().spawnEntity(spawnLocation, dMob.getEntityType());
                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "arena"), PersistentDataType.STRING, arenaName);
                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "level"), PersistentDataType.INTEGER, dMob.level);
                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "name"), PersistentDataType.STRING, dMob.mob);
                    assignEntityData(theEnt, dMob.level, damage, health, dMob.mob);
                    shortTermStorages.arenaHashMap.get(arenaName).allMobsInArena.add(theEnt);
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

                    if (spawnLocation.getWorld().getName().equalsIgnoreCase("world")) {

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + spawnLocation.getX() + " " + spawnLocation.getY() + " " + spawnLocation.getZ() + " run function _spawn:" + thatmob.getName());
                    } else {

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute in " + spawnLocation.getWorld().getName() + " positioned " + spawnLocation.getX() + " " + spawnLocation.getY() + " " + spawnLocation.getZ() + " run function _spawn:" + thatmob.getName());
                    }

                    //get that special entity method
                    Entity theEnt = getSpecialEntity(spawnLocation);
                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "arena"), PersistentDataType.STRING, arenaName);
                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "level"), PersistentDataType.INTEGER, dMob.level);
                    theEnt.getPersistentDataContainer().set(new NamespacedKey(Rpgcore.getInstance(), "name"), PersistentDataType.STRING, dMob.mob);
                    assignEntityData(theEnt, dMob.level, damage, health, dMob.mob);
                    shortTermStorages.arenaHashMap.get(arenaName).allMobsInArena.add(theEnt);
                    //shortTermStorages.arenaHashMap.get(arenaName).currentPhase.spawnedEntities.add(theEnt);
                }

            }

        }



    }


    private Entity getSpecialEntity(Location location) {
        ;
        List<Entity> finalList = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<>(location.getWorld().getNearbyEntities(location, 2d, 2d, 2d));
        for (Entity entity : entities){
            if (!(entity instanceof Player)) {
                finalList.add(entity);
            }
        }

        return finalList.get(0);

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

        if( !(thatLocation.getBlock().getType() == Material.WATER || thatLocation.getBlock().isEmpty())) {
            return false;
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

        LivingEntity theEntity = (LivingEntity) thatEntity;
        theEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.valueOf(damage));
        theEntity.setMaxHealth(health);
        theEntity.setHealth(health);
        theEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(Double.valueOf(damage));
        theEntity.setCustomName(Utils.color("&7[&eLvl&7:&6" + level + "&7] &e" + name + " &7[&cHealth " + health + "/Max Health " + health + "&7]"));
        theEntity.setCustomNameVisible(true);
        //[Lvl:#] Name [Health #/Max Health #]
    }



}
