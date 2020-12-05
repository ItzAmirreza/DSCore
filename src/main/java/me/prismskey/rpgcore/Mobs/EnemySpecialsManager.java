package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Enums.MobAbilityCoolDownTimes;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.APIUsages;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class EnemySpecialsManager implements Listener {

    private HashMap<String, Integer> enemySpecialCooldowns;
    private HashSet<UUID> processedEntities;
    private Random random;

    public EnemySpecialsManager() {
        enemySpecialCooldowns = new HashMap<String, Integer>();
        random = new Random();

        processedEntities = new HashSet<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                processedEntities.clear();
                for(Player player: Bukkit.getOnlinePlayers()) {
                    processEnemiesAroundPlayer(player);
                }
            }
        }.runTaskTimer(Rpgcore.instance, 0, 1);
    }

    private void processEnemiesAroundPlayer(Player player) {
        for(Entity e: player.getNearbyEntities(20, 20 , 20)) {
            if(e instanceof LivingEntity) {
                if(processedEntities.contains(e.getUniqueId())) {
                    continue;
                } else {
                    processedEntities.add(e.getUniqueId());
                }
                checkCooldownsForEntity((LivingEntity) e);
            }
        }
    }

    private void checkCooldownsForEntity(LivingEntity e) {
        //Rpgcore.instance.getLogger().info("CheckCooldown");
        if(APIUsages.hasMobNBT(e, "lich")) {
            //Rpgcore.instance.getLogger().info("lich");
            processEnemyCooldown(e, "lifeDrainBeam");
        }
    }

    private void processEnemyCooldown(LivingEntity e, String attackName) {
        String keyString = e.getUniqueId().toString() + ":" + attackName;

        //returns null if keyString is not in the map
        if(enemySpecialCooldowns.putIfAbsent(keyString, MobAbilityCoolDownTimes.LIFE_DRAIN.cooldown) != null) {
            enemySpecialCooldowns.merge(keyString, -1, Integer::sum);
        }
        if(enemySpecialCooldowns.get(keyString) <= 0) {
            tryUseSpecial(e, attackName);
            enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.LIFE_DRAIN.cooldown);
        }
    }

    private void tryUseSpecial(LivingEntity e, String attackName) {
        if(attackName.equals("lifeDrainBeam")) {
            triggerLifeDrainBeam(e);
        }
    }

    private void triggerLifeDrainBeam(LivingEntity ent) {
        Player target = getRandomPlayerTarget(ent);

        if(target == null) return;

        Location startLoc = ent.getLocation();
        Location endLoc = target.getLocation();

        Location particleLoc = startLoc.clone();
        particleLoc.setY(particleLoc.getY() + 1);
        World world = startLoc.getWorld();
        // dir is the Vector direction (offset from 0,0,0) the player is facing in 3D space
        //Vector dir = startLoc.getDirection();
        //Vector vecOffset = dir.clone().multiply(.5);

        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:excalibur.ability.blast master @a " + startLoc.getX() + " " + startLoc.getY() + " " + startLoc.getZ() + " 1 1");

        int beamLength = (int) (startLoc.distance(endLoc)/.5) + 1;
        Rpgcore.instance.getLogger().info("" + beamLength);
        double xIncrease = (endLoc.getX() - startLoc.getX()) / beamLength;
        double yIncrease = (endLoc.getY() - startLoc.getY()) / beamLength;
        double slope = (endLoc.getZ() - startLoc.getZ()) / (endLoc.getX() - startLoc.getX());


        for (int i = 0; i < beamLength; i++) {
            for (Entity entity : world.getNearbyEntities(particleLoc, 5, 5, 5)) {


                if (entity instanceof Damageable) {
                    if (entity == ent) {
                        continue;
                    }

                    Vector particleMinVector = new Vector(
                            particleLoc.getX() - 0.25,
                            particleLoc.getY() - 0.25,
                            particleLoc.getZ() - 0.25);
                    Vector particleMaxVector = new Vector(
                            particleLoc.getX() + 0.25,
                            particleLoc.getY() + 0.25,
                            particleLoc.getZ() + 0.25);

                    if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                        //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                        //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                        ((Damageable) entity).damage(5, ent);
                        ent.setHealth(Math.min(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), ent.getHealth() + 1));
                        return;
                    }

                }
            }
            Rpgcore.instance.getLogger().info("" + particleLoc.getX() + " " + particleLoc.getY() + " " + particleLoc.getZ());
            particleLoc.setX(particleLoc.getX() + xIncrease);
            particleLoc.setY(particleLoc.getY() + yIncrease);
            double z = slope * particleLoc.getX() - slope * startLoc.getX() + startLoc.getZ();
            particleLoc.setZ(z);

            //particleLoc.add(vecOffset);
            world.spawnParticle(Particle.SOUL, particleLoc, 0);
        }
    }


    private Player getRandomPlayerTarget(LivingEntity ent) {
        List<Entity> nearbyPlayers = ent.getNearbyEntities(20, 20, 20)
                .stream()
                .filter(e -> e instanceof Player)
                .collect(Collectors.toList());

        if(nearbyPlayers.size() == 0) return null;

        return (Player) nearbyPlayers.get(random.nextInt(nearbyPlayers.size()));
    }

    @EventHandler
    public void onEnemyDie(EntityDeathEvent event) {

        Iterator<String> iterator = enemySpecialCooldowns.keySet().iterator();

        while(iterator.hasNext()) {
            String keyString = iterator.next();
            if(keyString.contains(event.getEntity().getUniqueId().toString())) {
                iterator.remove();
            }
        }

    }
}
