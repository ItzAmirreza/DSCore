package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EarthBlast {

    public void start(LivingEntity ent) {
        Player target = getRandomPlayerTarget(ent);

        if(target == null || target == ent) return;

        if(ent instanceof Creature) {
            ((Creature)ent).setTarget(target);
        }

        new BukkitRunnable() {

            int tries = 0;

            @Override
            public void run() {
                if(tries > 3) {
                    this.cancel();
                    return;
                }

                FallingBlock fallingBlock = new CustomFallingBlock(ent.getEyeLocation()).spawn();
                fallingBlock.setDropItem(false);
                fallingBlock.setHurtEntities(true);
                fallingBlock.setVelocity(ent.getLocation().getDirection().multiply(2));

                new BukkitRunnable() {

                    int ticks = 0;

                    @Override
                    public void run() {
                        if(fallingBlock.isDead()) {
                            fallingBlock.remove();
                            fallingBlock.getLocation().getBlock().setType(Material.AIR);
                            this.cancel();
                            return;
                        }
                        Vector fallingBlockMinVector = new Vector(
                                fallingBlock.getLocation().getX() - 0.25,
                                fallingBlock.getLocation().getY() - 0.25,
                                fallingBlock.getLocation().getZ() - 0.25);
                        Vector fallingBlockMaxVector = new Vector(
                                fallingBlock.getLocation().getX() + 0.25,
                                fallingBlock.getLocation().getY() + 0.25,
                                fallingBlock.getLocation().getZ() + 0.25);

                        if (target.getBoundingBox().overlaps(fallingBlockMinVector, fallingBlockMaxVector)) {
                            target.setVelocity(fallingBlock.getVelocity());
                            target.damage(20.0, fallingBlock);
                            if(fallingBlock.isOnGround()) {
                                fallingBlock.getLocation().getBlock().setType(Material.AIR);
                            } else {
                                fallingBlock.remove();
                            }
                            //fallingBlock.getWorld().strikeLightningEffect(fallingBlock.getLocation());
                            fallingBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, fallingBlock.getLocation(), 0);
                            this.cancel();
                            return;
                        }
                        if(fallingBlock.isOnGround()) {
                            fallingBlock.getLocation().getBlock().setType(Material.AIR);
                            //fallingBlock.getWorld().strikeLightningEffect(fallingBlock.getLocation());
                            fallingBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, fallingBlock.getLocation(), 0);
                            this.cancel();
                            return;
                        }
                        if(ticks >= 60) {
                            fallingBlock.remove();
                            //fallingBlock.getWorld().strikeLightningEffect(fallingBlock.getLocation());
                            fallingBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, fallingBlock.getLocation(), 0);
                            this.cancel();
                            return;
                        }

                        ticks++;
                    }
                }.runTaskTimer(Rpgcore.instance, 0L, 1L);
                tries++;
            }
        }.runTaskTimer(Rpgcore.instance, 0L, 60L);
    }

    public static Player getRandomPlayerTarget(LivingEntity ent) {
        List<Entity> nearbyPlayers = ent.getNearbyEntities(20, 20, 20)
                .stream()
                .filter(e -> e instanceof Player)
                .filter(e -> e != ent)
                .collect(Collectors.toList());

        if(nearbyPlayers.isEmpty()) return null;

        return (Player) nearbyPlayers.get(new Random().nextInt(nearbyPlayers.size()));
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
