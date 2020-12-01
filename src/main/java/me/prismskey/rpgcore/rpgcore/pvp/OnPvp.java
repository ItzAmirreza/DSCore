package me.prismskey.rpgcore.rpgcore.pvp;

import me.prismskey.rpgcore.rpgcore.RPGPlayerData;
import me.prismskey.rpgcore.rpgcore.Rpgcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnPvp implements Listener {

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        RPGPlayerData damagerData = Rpgcore.instance.getDataByUUID(damager.getUniqueId());
        RPGPlayerData victimData = Rpgcore.instance.getDataByUUID(victim.getUniqueId());
        if(damagerData.getPvpState() == false || victimData.getPvpState() == false) {
            event.setCancelled(true);
        }
    }
}
