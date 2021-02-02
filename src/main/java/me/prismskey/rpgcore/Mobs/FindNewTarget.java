package me.prismskey.rpgcore.Mobs;

import org.bukkit.GameMode;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.ArrayList;
import java.util.Random;

public class FindNewTarget {

    Random r = new Random();

    public void start(Creature e) {
        ArrayList<Player> potentialTargets = new ArrayList<>();
        for(Entity ent: e.getNearbyEntities(16, 16, 16)) {
            if(ent instanceof Player && ((Player) ent).getGameMode() == GameMode.SURVIVAL) {
                potentialTargets.add((Player) ent);
            }
        }
        if(potentialTargets.size() == 0) {
            return;
        }
        Player target = potentialTargets.get(r.nextInt(potentialTargets.size()));
        e.setTarget(target);
    }
}
