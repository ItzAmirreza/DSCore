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

public class OrichalcumRecipes {

    public OrichalcumRecipes() {

        this.registerOrichalcumBoots();
        this.registerOrichalcumChestplate();
        this.registerOrichalcumHelmet();
        this.registerOrichalcumLeggings();

    }


    private void registerOrichalcumHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 607);
        nbti.setInteger("maxDurability", 607);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "orichalcum-helmet");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("OOO", "O O", "   ");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerOrichalcumChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 792);
        nbti.setInteger("maxDurability", 792);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "orichalcum-chestplate");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("O O", "OOO", "OOO");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerOrichalcumLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 755);
        nbti.setInteger("maxDurability", 755);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "orichalcum-leggings");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("OOO", "O O", "O O");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerOrichalcumBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 681);
        nbti.setInteger("maxDurability", 681);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "orichalcum-boots");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "O O", "O O");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }

}
