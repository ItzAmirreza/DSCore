package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Mobs.EnemySpecialsManager;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class VoidTarget {

    public void start(LivingEntity ent) {
        Player target = EnemySpecialsManager.getRandomPlayerTarget(ent);

        if (target == null) return;

        Location startLoc = ent.getLocation();
        Location endLoc = target.getLocation();

        Location particleLoc = startLoc.clone();
        particleLoc.setY(particleLoc.getY() + 1);
        World world = startLoc.getWorld();

        int beamLength = (int) (startLoc.distance(endLoc) / .5) + 1;
        Rpgcore.instance.getLogger().info("" + beamLength);
        double xIncrease = (endLoc.getX() - startLoc.getX()) / beamLength;
        double yIncrease = (endLoc.getY() - startLoc.getY()) / beamLength;
        double slope = (endLoc.getZ() - startLoc.getZ()) / (endLoc.getX() - startLoc.getX());

        //for (int i = beamLength; i > 0; i--) {
        new BukkitRunnable() {
            int i = 0;
            boolean done = false;
            public void run() {

                for (int j = 0; j < 3; j++) {
                    if (done) {
                        this.cancel();
                        break;
                    }
                    if (i >= beamLength) {
                        this.cancel();
                        break;
                    }
                    if (particleLoc.getBlock().getType() != Material.AIR && particleLoc.getBlock().getType() != Material.WATER) {
                        this.cancel();
                        break;
                    }

                    int get = 0;
                    List<Entity> entities = new ArrayList<>(world.getNearbyEntities(particleLoc, 5, 5, 5));
                    while (get < entities.size()) {
                        if (get >= entities.size()) {
                            break;
                        }
                        Entity entity = entities.get(get);
                        if (entity instanceof Player && entity != ent) {
                            org.bukkit.util.Vector particleMinVector = new org.bukkit.util.Vector(
                                    particleLoc.getX() - 0.25,
                                    particleLoc.getY() - 0.25,
                                    particleLoc.getZ() - 0.25);
                            org.bukkit.util.Vector particleMaxVector = new Vector(
                                    particleLoc.getX() + 0.25,
                                    particleLoc.getY() + 0.25,
                                    particleLoc.getZ() + 0.25);

                            if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                                //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                                //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 20, false, false, true));
                                this.cancel();
                                done = true;
                                break;
                            }
                        }
                        get++;
                    }

                    Rpgcore.instance.getLogger().info("" + particleLoc.getX() + " " + particleLoc.getY() + " " + particleLoc.getZ());
                    particleLoc.setX(particleLoc.getX() + xIncrease);
                    particleLoc.setY(particleLoc.getY() + yIncrease);
                    double z = slope * particleLoc.getX() - slope * startLoc.getX() + startLoc.getZ();
                    particleLoc.setZ(z);

                    //particleLoc.add(vecOffset);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(128, 128, 128), 2);
                    world.spawnParticle(Particle.REDSTONE, particleLoc.getX(), particleLoc.getY(), particleLoc.getZ(), 0, 0, 0, 0, dust);
                    i++;
                }
            }
        }.runTaskTimer(Rpgcore.getInstance(), 0, 1);
    }
}

