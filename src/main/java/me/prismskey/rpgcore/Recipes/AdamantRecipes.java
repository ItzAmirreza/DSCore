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

public class AdamantRecipes {

    public AdamantRecipes() {
        this.registerAdamantBoots();
        this.registerAdamantChestplate();
        this.registerAdamantHelmet();
        this.registerAdamantLeggings();
    }

    private void registerAdamantHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 507);
        nbti.setInteger("maxDurability", 507);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "adamant_helmet");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("AAA", "A A", "   ");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerAdamantChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 692);
        nbti.setInteger("maxDurability", 692);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "adamant_chestplate");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("A A", "AAA", "AAA");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerAdamantLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 655);
        nbti.setInteger("maxDurability", 655);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "adamant_leggings");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("AAA", "A A", "A A");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerAdamantBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 581);
        nbti.setInteger("maxDurability", 581);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Rpgcore.getInstance(), "adamant_boots");
        shortTermStorages.recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "A A", "A A");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }


}
