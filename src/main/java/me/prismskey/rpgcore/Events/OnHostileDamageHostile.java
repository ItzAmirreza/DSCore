package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import scala.concurrent.impl.FutureConvertersImpl;

public class OnHostileDamageHostile implements Listener {
    @EventHandler
    public void onHostileDamageHostile(EntityDamageByEntityEvent event) {
        if(Utils.entityIsHostile(event.getDamager()) && Utils.entityIsHostile(event.getEntity())) {
            event.setCancelled(true);
        }
        if(event.getDamager() instanceof Arrow && Utils.entityIsHostile(event.getEntity())) {
            Arrow arrow = (Arrow) event.getDamager();
            if(arrow.getShooter() instanceof Entity && Utils.entityIsHostile((Entity) arrow.getShooter())) {
                event.setCancelled(true);
            }
        }
    }
}
