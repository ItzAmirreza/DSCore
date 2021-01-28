package me.prismskey.rpgcore.Events;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DisableFireworkDamage implements Listener {
    @EventHandler
    public void onFireworkDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Firework) {
            event.setCancelled(true);
        }
    }
}
