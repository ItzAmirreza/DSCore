package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class OnUndeadMobSpawn implements Listener {

    private ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);

    @EventHandler
    public void onUndeadMobSpawn(EntitySpawnEvent event) {
        if(Utils.checkIfInDungeon(event.getLocation())) {
            if(canMobBurnInSunlight(event.getEntity())) {
                LivingEntity living = (LivingEntity) event.getEntity();
                living.getEquipment().setHelmet(leatherHelmet);
                living.getEquipment().setHelmetDropChance(0);
            }
        }
    }

    private boolean canMobBurnInSunlight(Entity e) {
        return e instanceof Zombie || e instanceof Skeleton;
    }
}
