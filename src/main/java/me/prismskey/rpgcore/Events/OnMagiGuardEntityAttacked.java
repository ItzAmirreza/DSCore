package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.Mobs.MagiGuard;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnMagiGuardEntityAttacked implements Listener {

    @EventHandler
    public void onMagiguardEntityAttacked(EntityDamageByEntityEvent event) {
        if(MagiGuard.MagiguardingEntitiesAndTimers.keySet().contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
            if(event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                damager.sendMessage(ChatColor.RED + "A magical barrier shields your target from damage. Wait for the barrier to dissipate.");
            }
        }
    }
}
