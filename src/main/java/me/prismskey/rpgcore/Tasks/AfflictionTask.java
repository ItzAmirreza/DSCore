package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.Mobs.Affliction;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.UUID;

public class AfflictionTask extends BukkitRunnable {
    @Override
    public void run() {
        Iterator it = Affliction.healingSpots.keySet().iterator();
        while(it.hasNext()) {
            Location loc = (Location) it.next();

            //draw circle
            double radius = 1f;
            for (double angle = 0; angle < 2 * Math.PI; angle += .2) {
                double x = (radius * Math.sin(angle));
                double z = (radius * Math.cos(angle));

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 100, 0), 1);


                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY() + .5, loc.getZ() + z, 0, 0, 0, 0, dust);

            }


            for(Entity e: loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
                if(Affliction.afflictedPlayers.containsKey(e.getUniqueId())) {
                    Affliction.afflictedPlayers.remove(e.getUniqueId());
                    e.sendMessage(ChatColor.GREEN + "You have been cleansed from the pharaoh's curse.");
                }
            }
            Affliction.healingSpots.merge(loc, -1, Integer::sum);
            if(Affliction.healingSpots.get(loc) <= 0) {
                it.remove();
            }
        }




        it = Affliction.afflictedPlayers.keySet().iterator();
        while(it.hasNext()) {
            UUID uuid = (UUID) it.next();
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) {
                it.remove();
                continue;
            }
            Affliction.afflictedPlayers.merge(uuid, -1, Integer::sum);
            if(Affliction.afflictedPlayers.get(uuid) <= 0) {
                it.remove();
                player.setHealth(0);
                continue;
            }
        }
    }
}
