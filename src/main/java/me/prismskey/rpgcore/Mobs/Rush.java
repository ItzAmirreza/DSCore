package me.prismskey.rpgcore.Mobs;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Random;

public class Rush {
    Random r = new Random();

    public void start(LivingEntity e) {
        ArrayList<Player> potentialTargets = new ArrayList<>();
        for(Entity ent: e.getNearbyEntities(10, 10, 10)) {
            if(ent instanceof Player && ((Player) ent).getGameMode() == GameMode.SURVIVAL) {
                potentialTargets.add((Player) ent);
            }
        }
        if(potentialTargets.size() > 0) {
            Player randomTarget = potentialTargets.get(r.nextInt(potentialTargets.size()));
            ((Creature) e).setTarget(randomTarget);
            e.teleport(randomTarget.getLocation());
            randomTarget.sendMessage(ChatColor.RED + "The enemy rushes at you.");
            ((Damageable) randomTarget).damage(7,e);
        }

    }
}
