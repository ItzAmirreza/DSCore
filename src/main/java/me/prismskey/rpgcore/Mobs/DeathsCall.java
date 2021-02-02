package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Mobs.EnemySpecialsManager;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class DeathsCall {

    Random r = new Random();

    public void start(LivingEntity ent) {
        Player target = EnemySpecialsManager.getRandomPlayerTarget(ent);
        Location abovePlayer = target.getLocation().clone().add(0, -1, 0);
        if (target == null) return;
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 0);
        int deathRays = r.nextInt(4) + 50;
        for (int i = 0; i < deathRays; i++) {
            int xrand = r.nextInt(11) - 5;
            int zrand = r.nextInt(11) - 5;
            Location particleSpawn = abovePlayer.clone();
            particleSpawn.add(xrand, 0, zrand);
            new BukkitRunnable() {
                float trail = 0;

                @Override
                public void run() {
                    if (trail >= 6) {
                        this.cancel();
                    }

                    for (Entity entity : particleSpawn.getWorld().getNearbyEntities(particleSpawn, 1, 1, 1)) {
                        if (entity instanceof Player) {
                            Vector particleMinVector = new Vector(
                                    particleSpawn.getX() - 1,
                                    particleSpawn.getY() - 1,
                                    particleSpawn.getZ() - 1);
                            Vector particleMaxVector = new Vector(
                                    particleSpawn.getX() + 1,
                                    particleSpawn.getY() + 1,
                                    particleSpawn.getZ() + 1);
                            if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                                //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                                //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                                ((Damageable) entity).damage(10, ent);



                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 1));
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 1));
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 5));
                                //particleSpawn.getWorld().playSound(particleSpawn, Sound.ENTITY_WOLF_HOWL, 1, 0);

                                this.cancel();
                            }
                        }
                    }

                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 10);
                    particleSpawn.getWorld().spawnParticle(Particle.REDSTONE, particleSpawn.getX(), particleSpawn.getY(), particleSpawn.getZ(), 0, 0, 0, 0, dust);

                    particleSpawn.add(0, .2, 0);

                    trail += .2;
                }
            }.runTaskTimer(Rpgcore.getInstance(), 0, 1);
        }
    }
}

