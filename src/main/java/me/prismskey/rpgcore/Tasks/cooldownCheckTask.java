package me.prismskey.rpgcore.Tasks;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class cooldownCheckTask extends BukkitRunnable {

    //initialization
    private final Rpgcore plugin;
    public cooldownCheckTask(Rpgcore plugin) {
        this.plugin = plugin;
    }



    //main task
    //will edit later

    @Override
    public void run() {
        ArrayList<RPGPlayerData> playerData = shortTermStorages.playerData;
        for(int i = 0; i < playerData.size(); i++) {
            RPGPlayerData data = playerData.get(i);
            data.decrementCooldowns();
            if(!data.isPlayerOnline() && data.allCooldownsOff()) {
                playerData.remove(i);
                i--;
            }

        }

    }


}
