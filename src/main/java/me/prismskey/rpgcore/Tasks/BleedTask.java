package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.DataManager.Bleed;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class BleedTask extends BukkitRunnable {

    @Override
    public void run() {
        Iterator it = Bleed.bleeds.iterator();
        while(it.hasNext()) {
            Bleed bleed = (Bleed) it.next();
            bleed.bleedTarget.damage(bleed.bleedDamage, bleed.bleedSource);
            bleed.bleedTimeRemaining--;
            if(bleed.bleedTimeRemaining <= 0) {
                it.remove();
            }
        }
    }
}
