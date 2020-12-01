package me.prismskey.rpgcore.rpgcore.arenas;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.prismskey.rpgcore.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SpawnPrebuiltCommand extends ArenaCommand {

    private Location spawnLocation;
    private String prebuiltString;

    public SpawnPrebuiltCommand(Location spawnLocation, String prebuiltString) {
        this.spawnLocation = spawnLocation;
        this.prebuiltString = prebuiltString;
    }

    @Override
    public void runCommand() {
        if(prebuiltString.equalsIgnoreCase("iron_zombie")) {
            Zombie ironZombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
            ironZombie.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
            ironZombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
            ironZombie.setCustomName("Armored Zombie");
            ironZombie.setCustomNameVisible(true);

            PersistentDataContainer persistentData = ironZombie.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "ironZombie");

        } else if(prebuiltString.equalsIgnoreCase("undead_knight")) {
            Skeleton knight = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
            knight.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
            knight.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            knight.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            knight.getEquipment().setLeggings(new ItemStack(Material.IRON_BOOTS));
            knight.setCustomName("Undead Knight");
            knight.setCustomNameVisible(true);

            PersistentDataContainer persistentData = knight.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "undeadKnight");
        } else if(prebuiltString.equalsIgnoreCase("illusioner")) {
            Illusioner illusioner = (Illusioner) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ILLUSIONER);
            illusioner.setCustomName("Illusioner");
            illusioner.setCustomNameVisible(true);

            PersistentDataContainer persistentData = illusioner.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "illusioner");
        } else if(prebuiltString.equalsIgnoreCase("axeman")) {
            Vindicator axeman = (Vindicator) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.VINDICATOR);
            axeman.setCustomName("Rogue Axeman");
            axeman.setCustomName("true");

            PersistentDataContainer persistentData = axeman.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "axeman");
        } else if(prebuiltString.equalsIgnoreCase("ravager")) {
            Ravager ravager = (Ravager) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.RAVAGER);
            ravager.setCustomName("Ravager");
            ravager.setCustomNameVisible(true);

            PersistentDataContainer persistentData = ravager.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "ravager");
        } else if(prebuiltString.equalsIgnoreCase("zombie")) {
            Zombie zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
            zombie.setCustomName("Zombie");
            zombie.setCustomNameVisible(true);

            PersistentDataContainer persistentData = zombie.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "zombie");
        } else if(prebuiltString.equalsIgnoreCase("husk")) {
            Husk husk = (Husk) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.HUSK);
            husk.setCustomName("Husk");
            husk.setCustomNameVisible(true);

            PersistentDataContainer persistentData = husk.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "husk");
        } else if(prebuiltString.equalsIgnoreCase("drowned")) {
            Drowned drowned = (Drowned) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.DROWNED);
            drowned.setCustomName("Drowned");
            drowned.setCustomNameVisible(true);

            PersistentDataContainer persistentData = drowned.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "drowned");
        } else if(prebuiltString.equalsIgnoreCase("slime")) {
            Slime slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SLIME);
            slime.setSize(4);
            slime.setCustomName("Slime");
            slime.setCustomNameVisible(true);

            PersistentDataContainer persistentData = slime.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "slime");
        } else if(prebuiltString.equalsIgnoreCase("king_slime")) {
            Slime slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SLIME);
            slime.setSize(7);
            slime.setCustomName("King Slime");
            slime.setCustomNameVisible(true);

            PersistentDataContainer persistentData = slime.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "king_slime");
        } else if(prebuiltString.equalsIgnoreCase("skeleton")) {
            Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);

            PersistentDataContainer persistentData = skeleton.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "skeleton");
        } else if(prebuiltString.equalsIgnoreCase("sword_skeleton")) {
            Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
            skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));

            PersistentDataContainer persistentData = skeleton.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "sword_skeleton");
        } else if(prebuiltString.equalsIgnoreCase("spider")) {
            Spider spider = (Spider) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SPIDER);

            PersistentDataContainer persistentData = spider.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "spider");
        } else if(prebuiltString.equalsIgnoreCase("cave_spider")) {
            CaveSpider spider = (CaveSpider) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.CAVE_SPIDER);

            PersistentDataContainer persistentData = spider.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "cave_spider");
        } else if(prebuiltString.equalsIgnoreCase("silverfish")) {
            Silverfish silverfish = (Silverfish) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SILVERFISH);

            PersistentDataContainer persistentData = silverfish.getPersistentDataContainer();
            persistentData.set(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING, "silverfish");
        }

    }
}
