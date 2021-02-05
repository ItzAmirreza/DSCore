package me.prismskey.rpgcore.Mobs;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BansheeScream {
    public void start(LivingEntity entity) {
        for(Entity e: entity.getNearbyEntities(10, 10, 10)) {
            if(e instanceof Player) {
                ((Damageable) e).damage(5, entity);
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 1, false, false, true));
                ((Player) e).sendMessage(ChatColor.RED + "The banshee's scream makes your ears bleed.");
            }
            if(e instanceof Villager || e instanceof WanderingTrader) {
                ((Damageable) e).damage(5, entity);
            }
        }
    }
}
