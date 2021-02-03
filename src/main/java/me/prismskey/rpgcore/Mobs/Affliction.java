package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Affliction {
    public static HashMap<Location, Integer> healingSpots = new HashMap<>();
    public static HashMap<UUID, Integer> afflictedPlayers = new HashMap<>();
    Random r = new Random();
    public void start(LivingEntity ent) {
        healingSpots.put(ent.getLocation(), 20 * 10);

        boolean foundSpot = false;
        for(int i = 0; i < 10; i++) {
            int x = r.nextInt(10) - 5;
            int z = r.nextInt(10) - 5;
            Location loc = ent.getLocation().clone();
            loc.setX(loc.getX() + x);
            loc.setZ(loc.getZ() + z);
            if(Utils.isClearSpace(loc) && Utils.isWithinRegion(loc, "pharaohArena")) {
                foundSpot = true;
                healingSpots.put(loc, 20 * 10);
                break;
            }
        }

        if(!foundSpot) {
            healingSpots.put(ent.getLocation(), 20 * 10);
        }

        for(Entity e: ent.getNearbyEntities(20, 20, 20)) {
            if(e instanceof Player && ((Player) e).getGameMode() == GameMode.SURVIVAL) {
                ((Player) e).sendMessage(ent.getCustomName() + ": " + ChatColor.YELLOW + "Die to the curse!");
                ((Player) e).sendMessage(ChatColor.RED + "You have been cursed! Get in the cleansing circle quickly or you will die!");
                afflictedPlayers.putIfAbsent(e.getUniqueId(), 20 * 10);
            }
        }
    }
}
