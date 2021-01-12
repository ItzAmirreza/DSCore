package me.prismskey.rpgcore.Events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class JustTestingEvent implements Listener {

    @EventHandler
    public void onSlimeSpawn(EntitySpawnEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) e.getEntity();
            if (entity.getType() == EntityType.SLIME) {
                e.setCancelled(true);
            }
        }
    }

}
