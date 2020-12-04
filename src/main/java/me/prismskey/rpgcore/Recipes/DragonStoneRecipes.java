package me.prismskey.rpgcore.Recipes;

import de.tr7zw.nbtapi.NBTItem;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.UUID;

public class DragonStoneRecipes {


    public DragonStoneRecipes() {
        this.registerDragonstoneHelmet();
        this.registerDragonstoneBoots();
        this.registerDragonstoneChestplate();
        this.registerDragonstoneLeggings();
    }



    private void registerDragonstoneHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 807);
        nbti.setInteger("maxDurability", 807);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "dragonstone_helmet");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("DDD", "D D", "   ");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }


    private void registerDragonstoneChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 992);
        nbti.setInteger("maxDurability", 992);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 11, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "dragonstone_chestplate");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("D D", "DDD", "DDD");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }


    private void registerDragonstoneLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 955);
        nbti.setInteger("maxDurability", 955);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "dragonstone_leggings");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("DDD", "D D", "D D");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerDragonstoneBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 881);
        nbti.setInteger("maxDurability", 881);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "dragonstone_boots");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "D D", "D D");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }


}
