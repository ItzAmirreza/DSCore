package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class iron_zombie {
    public iron_zombie() {

    }

    public void spawn(Location spawnLocation) {
        Zombie ironZombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
        ironZombie.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
        ironZombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        ironZombie.setCustomName("Armored Zombie");
        ironZombie.setCustomNameVisible(true);

        PersistentDataContainer persistentData = ironZombie.getPersistentDataContainer();
        persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "ironZombie");
    }

}
