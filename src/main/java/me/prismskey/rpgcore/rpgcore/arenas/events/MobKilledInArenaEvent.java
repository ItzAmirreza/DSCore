package me.prismskey.rpgcore.rpgcore.arenas.events;

import me.prismskey.rpgcore.rpgcore.Rpgcore;
import me.prismskey.rpgcore.rpgcore.arenas.Arena;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MobKilledInArenaEvent implements Listener {

    @EventHandler
    public void onMobKilledInArena(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity) {
            Arena arena = Rpgcore.instance.getArenaByLocation(entity.getLocation());
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
