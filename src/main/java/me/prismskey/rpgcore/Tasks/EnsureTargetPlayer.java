package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.Utils.APIUsages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.ArrayList;
import java.util.Random;

public class EnsureTargetPlayer extends BukkitRunnable {

    Random r = new Random();

    @Override
    public void run() {
        for(Player player: Bukkit.getOnlinePlayers()) {
            for(Entity e: player.getNearbyEntities(16, 16, 16)) {
                if(e instanceof Creature) {
                    Creature creature = (Creature) e;
                    if(APIUsages.hasMobNBT(e, "findNewTarget") || Utils.entityIsHostile(e)) {
                        if(!(creature.getTarget() instanceof Player)) {
                            creature.setTarget(null);
                            ArrayList<Player> possibleTargets = new ArrayList<>();
                            for(Entity ent: creature.getNearbyEntities(16, 16, 16)) {
                                if(ent instanceof Player && ((Player) ent).getGameMode() == GameMode.SURVIVAL) {
                                    possibleTargets.add((Player) ent);
                                }
                            }
                            if(possibleTargets.size() > 0) {
                                Player target = possibleTargets.get(r.nextInt(possibleTargets.size()));
                                creature.setTarget(target);
                            }
                        }
                    }
                }
            }
        }
    }
}
