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

public class MithrilRecipes {

    public MithrilRecipes() {
        this.registerMithrilBoots();
        this.registerMithrilChestplate();
        this.registerMithrilHelmet();
        this.registerMithrilLeggings();

    }

    private void registerMithrilHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 263);
        nbti.setInteger("maxDurability", 263);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "mithril_helmet");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("MMM", "M M", "   ");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerMithrilChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 370);
        nbti.setInteger("maxDurability", 370);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "mithril_chestplate");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("M M", "MMM", "MMM");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerMithrilLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 360);
        nbti.setInteger("maxDurability", 360);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "mithril_leggings");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("MMM", "M M", "M M");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerMithrilBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 325);
        nbti.setInteger("maxDurability", 325);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "mithril_boots");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "M M", "M M");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }

}
