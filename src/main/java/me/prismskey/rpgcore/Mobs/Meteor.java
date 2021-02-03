package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Meteor {
    public void start(LivingEntity ent) {
        Player target = EnemySpecialsManager.getRandomPlayerTarget(ent);

        if (target == null) return;

        Location startLoc = target.getLocation().clone().add(2, 10, 2);
        Location endLoc = target.getLocation().clone().add(0, -3, 0);

        target.sendMessage(ChatColor.RED + "You are being targeted for a meteor strike. Get out of the way!");

        for(Entity e: ent.getNearbyEntities(10, 10, 10)) {
            if(e.getUniqueId() != target.getUniqueId() && e instanceof Player) {
                ((Player) e).sendMessage(ChatColor.RED + target.getName() + " is being targeted for a meteor strike. Get away from them!");
            }
        }

        Location particleLoc = startLoc.clone();
        particleLoc.setY(particleLoc.getY() + 1);
        World world = startLoc.getWorld();

        int beamLength = (int) (startLoc.distance(endLoc) / .5) + 1;
        Rpgcore.instance.getLogger().info("" + beamLength);
        double xIncrease = (endLoc.getX() - startLoc.getX()) / beamLength;
        double yIncrease = (endLoc.getY() - startLoc.getY()) / beamLength;
        double slope = (endLoc.getZ() - startLoc.getZ()) / (endLoc.getX() - startLoc.getX());


        new BukkitRunnable() {
            int particles = 0;
            boolean wasInClearSpace = false; //in case the particle spawns in a ceiling
            @Override
            public void run() {

                if(Utils.isClearSpace(particleLoc)) {
                    wasInClearSpace = true;
                }

                //if hit the ground
                if (wasInClearSpace && !Utils.isClearSpace(particleLoc)) {
                    particleLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, particleLoc, 1);
                    particleLoc.getWorld().playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    for(Entity e: particleLoc.getWorld().getNearbyEntities(particleLoc, 5, 5, 5)) {
                        if(e instanceof Player) {
                            ((Damageable) e).damage(40, ent);
                        }
                    }

                    this.cancel();
                }
                if(particles >= beamLength) {
                    this.cancel();
                }

                //Rpgcore.instance.getLogger().info("" + particleLoc.getX() + " " + particleLoc.getY() + " " + particleLoc.getZ());
                particleLoc.setX(particleLoc.getX() + xIncrease);
                particleLoc.setY(particleLoc.getY() + yIncrease);
                double z = slope * particleLoc.getX() - slope * startLoc.getX() + startLoc.getZ();
                particleLoc.setZ(z);

                //particleLoc.add(vecOffset);
                world.spawnParticle(Particle.FLAME, particleLoc, 0);
                particles++;
            }
        }.runTaskTimer(Rpgcore.getInstance(), 0, 2);


    }

}
