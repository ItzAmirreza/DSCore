package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.DataManager.ConfigLoader;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnPvp implements Listener {

    private ConfigLoader configloader = new ConfigLoader();

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        RPGPlayerData damagerData = configloader.getDataByUUID(damager.getUniqueId());
        RPGPlayerData victimData = configloader.getDataByUUID(victim.getUniqueId());
        if(damagerData.getPvpState() == false || victimData.getPvpState() == false ||
            Utils.checkIfInDungeon(damager) || Utils.checkIfInDungeon(victim)) {
            event.setCancelled(true);
            if(!(Utils.checkIfInDungeon(damager) || Utils.checkIfInDungeon(victim))) {
                if(damagerData.getPvpState() == false) {
                    damager.sendMessage(ChatColor.RED + "Your PVP is toggled off so you cannot attack other players.");
                }
                else if(victimData.getPvpState() == false) {
                    damager.sendMessage(ChatColor.RED + "Your target has their pvp toggled off.");
                }
            }
        }
    }
}
