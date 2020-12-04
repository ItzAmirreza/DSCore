package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.arenaLoader;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.ArenaManager.Arena;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;


public class MobSpawnInArenaEvent implements Listener {

    private arenaLoader arenaloader = new arenaLoader();
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity) {
            Arena arena = arenaloader.getArenaByLocation(event.getLocation());
            if(arena == null) {
                return;
            }
            LivingEntity living = (LivingEntity) entity;
            arena.handleMobSpawn(living);
        }
    }
}
