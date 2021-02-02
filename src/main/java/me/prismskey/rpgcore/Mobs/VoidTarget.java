package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Mobs.EnemySpecialsManager;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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

        for (int i = beamLength; i > 0; i--) {
            if (particleLoc.getBlock().getType() != Material.AIR && particleLoc.getBlock().getType() != Material.WATER) {
                break;
            }

            org.bukkit.util.Vector particleMinVector = new org.bukkit.util.Vector(
                    particleLoc.getX() - 0.25,
                    particleLoc.getY() - 0.25,
                    particleLoc.getZ() - 0.25);
            org.bukkit.util.Vector particleMaxVector = new Vector(
                    particleLoc.getX() + 0.25,
                    particleLoc.getY() + 0.25,
                    particleLoc.getZ() + 0.25);

            if (target.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 1, true, true));
                break;
            }

            particleLoc.setX(particleLoc.getX() + xIncrease);
            particleLoc.setY(particleLoc.getY() + yIncrease);
            double z = slope * particleLoc.getX() - slope * startLoc.getX() + startLoc.getZ();
            particleLoc.setZ(z);

            //particleLoc.add(vecOffset);
            world.spawnParticle(Particle.DRIP_LAVA, particleLoc, 0);
        }
    }
}

