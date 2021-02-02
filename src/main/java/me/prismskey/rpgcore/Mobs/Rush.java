package me.prismskey.rpgcore.Mobs;

import org.bukkit.GameMode;
import org.bukkit.entity.*;

import java.util.ArrayList;

public class Rush {
    public void start(LivingEntity e) {
        ArrayList<Player> potentialTargets = new ArrayList<>();
        for(Entity ent: e.getNearbyEntities(10, 10, 10)) {
            if(ent instanceof Player && ((Player) ent).getGameMode() == GameMode.SURVIVAL) {
                ((Creature) e).setTarget((Player) ent);
                e.teleport(ent.getLocation());
                ((Damageable) ent).damage(7,e);
            }
        }
    }
}
