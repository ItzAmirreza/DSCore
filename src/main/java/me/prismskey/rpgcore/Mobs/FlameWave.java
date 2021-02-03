package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class FlameWave {

    public void start(LivingEntity ent) {
        new BukkitRunnable() {
            int radius = 1;
            final int maxRadius = 10;

            @Override
            public void run() {
                if(radius >= maxRadius) {
                    this.cancel();
                    return;
                }

                for(Location particleLoc : generateSphere(ent.getLocation(), radius, true)) {
                    particleLoc.getWorld().spawnParticle(Particle.FLAME, particleLoc, 0);

                    for(Entity entity : ent.getWorld().getNearbyEntities(ent.getLocation(), 10, 10,10)) {
                        if(entity instanceof LivingEntity && ent != entity) {
                            Vector particleMinVector = new Vector(
                                    particleLoc.getX() - 0.25,
                                    particleLoc.getY() - 0.25,
                                    particleLoc.getZ() - 0.25);
                            Vector particleMaxVector = new Vector(
                                    particleLoc.getX() + 0.25,
                                    particleLoc.getY() + 0.25,
                                    particleLoc.getZ() + 0.25);

                            if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                                entity.setFireTicks(100);
                                ((LivingEntity) entity).damage(15.0, ent);
                            }
                        }
                    }
                }

                radius++;
            }
        }.runTaskTimer(Rpgcore.instance, 0L, 2L);
    }

    public List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
        List<Location> circleBlocks = new ArrayList<>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));
                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }
}
