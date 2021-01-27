package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.DataManager.ClickCombo;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ComboResetTimer extends BukkitRunnable {

    private final Rpgcore rpgcore;
    public ComboResetTimer(Rpgcore rpgcore) {
        this.rpgcore = rpgcore;

    }

    @Override
    public void run() {
        for(ClickCombo combo: shortTermStorages.clickComboHashMap.values()) {
            combo.checkTime();
            combo.incrementTimer();
        }
    }
}
