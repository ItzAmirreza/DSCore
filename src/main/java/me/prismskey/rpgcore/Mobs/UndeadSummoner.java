package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Enums.SpecialMobs;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Random;

public class UndeadSummoner {

    Random r = new Random();

    public void start(LivingEntity e) {

        for(Entity ent: e.getNearbyEntities(10, 10, 10)) {
            if(ent instanceof Player) {
                ((Player) ent).sendMessage(ChatColor.DARK_GREEN + e.getCustomName() + ": " + ChatColor.YELLOW + "Arise my minions!");
            }
        }

        int summons = 3;
        for(int i = 0; i < summons; i++) {
            Location loc = e.getLocation().clone();
            loc.setX(loc.getX() + r.nextInt(10) - 5);
            loc.setZ(loc.getZ() + r.nextInt(10) - 5);
            if(Utils.isClearSpace(loc) && Utils.isClearSpace(loc.clone().add(0, 1, 0))) {
                int rand = r.nextInt(3);
                if(rand == 0) {
                    loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
                } else if(rand == 1) {
                    loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
                } else if(rand == 2) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute in " + loc.getWorld().getName() + " positioned " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " run function _spawn:" + SpecialMobs.GHOST.getName());
                }
            }
        }
    }
}
