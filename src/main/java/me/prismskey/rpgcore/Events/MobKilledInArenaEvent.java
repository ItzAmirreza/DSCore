package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.ArenaLoader;
import me.prismskey.rpgcore.ArenaManager.Arena;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobKilledInArenaEvent implements Listener {

    private ArenaLoader arenaloader = new ArenaLoader();

    @EventHandler
    public void onMobKilledInArena(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity) {
            Arena arena = arenaloader.getArenaByLocation(entity.getLocation());
            if(arena == null) {
                return;
            }
            if (entity instanceof Player) {
                return;
            }
            if (entity instanceof Tameable) {
                Tameable tameable = (Tameable) entity;
                if (tameable.isTamed()) {
                    return;
                }
            }


            //PersistentDataContainer data = entity.getPersistentDataContainer();
            //NamespacedKey key = new NamespacedKey(Rpgcore.instance, "mobType");
            //if(data.has(key, PersistentDataType.STRING)) {
            event.getDrops().clear();
            //}
            LivingEntity living = (LivingEntity) entity;

            Player killer = null;

            if(living.getKiller() instanceof Player) {
                killer = living.getKiller();
            }

            arena.handleMobDeath(living, killer);
        }
    }
}
