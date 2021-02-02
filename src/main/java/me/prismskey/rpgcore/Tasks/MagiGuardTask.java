package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.Mobs.MagiGuard;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.UUID;

public class MagiGuardTask extends BukkitRunnable {

    @Override
    public void run() {
        Iterator it = MagiGuard.MagiguardingEntitiesAndTimers.keySet().iterator();
        while (it.hasNext()) {
            UUID uuid = (UUID) it.next();
            Entity e = Bukkit.getEntity(uuid);
            if (e == null || e.isDead()) {
                it.remove();
                continue;
            }
            MagiGuard.MagiguardingEntitiesAndTimers.merge(uuid, -1, Integer::sum);
            if(MagiGuard.MagiguardingEntitiesAndTimers.get(uuid) <= 0) {
                it.remove();
            }

            double radius = .6f;
            for (double angle = 0; angle < 2 * Math.PI; angle += .2) {
                double x = (radius * Math.sin(angle));
                double z = (radius * Math.cos(angle));

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 255), 1);

                for (float y = 0; y < 2; y += .5) {
                    e.getLocation().getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().getX() + x, e.getLocation().getY() + y, e.getLocation().getZ() + z, 0, 0, 0, 0, dust);
                }
            }
        }
    }
}
