package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Enums.SpecialMobs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Random;

public class UndeadSummoner {

    Random r = new Random();

    public void start(LivingEntity e) {
        int summons = 3;
        for(int i = 0; i < summons; i++) {
            Location loc = e.getLocation().clone();
            loc.setX(loc.getX() + r.nextInt(10) - 5);
            loc.setZ(loc.getZ() + r.nextInt(10) - 5);
            if((loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WATER) && (loc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR || loc.clone().add(0, 1, 0).getBlock().getType() == Material.WATER)) {
                int rand = r.nextInt(3);
                if(rand == 0) {
                    loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
                } else if(rand == 1) {
                    loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
                } else if(rand == 2) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " run function _spawn:" + SpecialMobs.GHOST.getName());
                }
            }
        }
    }
}
