package me.prismskey.rpgcore.Mobs;

import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.UUID;

public class MagiGuard {
    public static HashMap<UUID, Integer> MagiguardingEntitiesAndTimers = new HashMap<>();

    public void start(LivingEntity e) {
        if(MagiguardingEntitiesAndTimers.keySet().contains(e.getUniqueId())) {
            return;
        }
        MagiguardingEntitiesAndTimers.put(e.getUniqueId(), 20 * 10);
    }
}
