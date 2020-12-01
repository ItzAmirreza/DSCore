package me.prismskey.rpgcore.rpgcore.arenas.events;

import me.prismskey.rpgcore.rpgcore.Rpgcore;
import me.prismskey.rpgcore.rpgcore.arenas.Arena;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;


public class MobSpawnInArenaEvent implements Listener {
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity) {
            Arena arena = Rpgcore.instance.getArenaByLocation(event.getLocation());
            if(arena == null) {
                return;
            }
            LivingEntity living = (LivingEntity) entity;
            arena.handleMobSpawn(living);
        }
    }
}
