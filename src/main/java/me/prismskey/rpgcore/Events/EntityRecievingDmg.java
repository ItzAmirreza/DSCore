package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.DMob;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;

public class EntityRecievingDmg implements Listener {


    @EventHandler
    public void onDMG(EntityDamageEvent event) {

        if (event.getEntity() instanceof LivingEntity) {

            //there is a bug here so don't test this part out (or just ignore it)
            /*LivingEntity entity = (LivingEntity) event.getEntity();

            if (entity.getPersistentDataContainer().has(new NamespacedKey(Rpgcore.getInstance(), "isspecial"), PersistentDataType.INTEGER)) {
                String nametg = entity.getCustomName();
                int maxhealth = (int) entity.getMaxHealth();
                int currenthealth = (int) entity.getHealth();

                entity.setCustomName(Utils.color(nametg + "&a" + currenthealth + "/" + maxhealth));
            }*/


        }

    }

}
