package me.prismskey.rpgcore.Recipes;

import de.tr7zw.nbtapi.NBTItem;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.ArrayList;

public class ExtraAnvilRecipes implements Listener {
    @EventHandler
    public void onPlaceItemInAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        if(inv.getItem(0) == null || inv.getItem(1) == null) {
            return;
        }

        NBTItem item1 = new NBTItem(inv.getItem(0));
        NBTItem item2 = new NBTItem(inv.getItem(1));

        if (checkForForbiddenRecipes(inv)) {
            event.setResult(null);
            return;
        }

        ItemStack newResult = getItemResult(item1, item2);
        if (newResult != null) {
            event.setResult(newResult);
            inv.setRepairCost(3);
        }

    }

    private boolean checkForForbiddenRecipes(AnvilInventory inv) {
        ItemStack toRepair = inv.getItem(0);
        ItemStack repairCatalyst = inv.getItem(1);

        if (toRepair == null || repairCatalyst == null) {
            return false;
        }

        if (itemMaterialsMatch(toRepair, repairCatalyst, Material.WOODEN_PICKAXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.WOODEN_SWORD) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.WOODEN_AXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.WOODEN_SHOVEL) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.WOODEN_HOE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.LEATHER_HELMET) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.LEATHER_LEGGINGS) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.LEATHER_CHESTPLATE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.LEATHER_BOOTS) ||

                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_PICKAXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_SWORD) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_AXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_SHOVEL) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_HOE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_HELMET) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_LEGGINGS) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_CHESTPLATE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.IRON_BOOTS) ||

                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_PICKAXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_SWORD) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_AXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_SHOVEL) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_HOE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_HELMET) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_LEGGINGS) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_CHESTPLATE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.GOLDEN_BOOTS) ||

                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_PICKAXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_SWORD) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_AXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_SHOVEL) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_HOE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_HELMET) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_LEGGINGS) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_CHESTPLATE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.DIAMOND_BOOTS) ||

                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_PICKAXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_SWORD) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_AXE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_SHOVEL) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_HOE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_HELMET) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_LEGGINGS) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_CHESTPLATE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.NETHERITE_BOOTS) ||

                itemMaterialsMatch(toRepair, repairCatalyst, Material.WARPED_FUNGUS_ON_A_STICK) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.CARROT_ON_A_STICK) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.FLINT_AND_STEEL) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.TRIDENT) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.BOW) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.TURTLE_HELMET) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.CHAINMAIL_HELMET) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.CHAINMAIL_LEGGINGS) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.CHAINMAIL_CHESTPLATE) ||
                itemMaterialsMatch(toRepair, repairCatalyst, Material.CHAINMAIL_BOOTS)

        ) {
            return true;
        }

        return false;
    }

    private boolean itemMaterialsMatch(ItemStack item1, ItemStack item2, Material mat) {
        if (item1 == null || item2 == null) {
            return false;
        }
        return item1.getType() == item2.getType() && item1.getType() == mat;
    }

    private ItemStack getItemResult(NBTItem toRepair, NBTItem material) {
        ArrayList<String> lore = (ArrayList<String>) toRepair.getItem().getItemMeta().getLore();
        //item was not used
        if (lore == null) {
            return null;
        }
        String durabilityString = "";
        int loreDurabilityIndex = -1;
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if (s.contains("Durability: ")) {
                loreDurabilityIndex = i;
                durabilityString = s;
                break;
            }
        }
        if (loreDurabilityIndex == -1) {
            return null;
        }
        int currentDurability = Integer.parseInt(durabilityString.split(" ")[1].split("/")[0]);
        int maxDurability = Integer.parseInt(durabilityString.split(" ")[1].split("/")[1]);
        if (itemAndRepairMaterialMatch(toRepair, material)) {
            currentDurability += maxDurability / 4;
            currentDurability = Math.min(currentDurability, maxDurability);
            lore.set(loreDurabilityIndex, ChatColor.RESET + "Durability: " + currentDurability + "/" + maxDurability);


            Damageable damageable = (Damageable) toRepair.getItem().getItemMeta();
            double vanillaMaxDurability = toRepair.getItem().getType().getMaxDurability();
            double customDurability = currentDurability;
            double customMaxDurability = maxDurability;
            double newDamage = vanillaMaxDurability - (vanillaMaxDurability * (customDurability / customMaxDurability));

            damageable.setDamage((int) newDamage);

            //damageable.setDamage((int) newDamage);
            if (damageable.getDamage() == 0 && currentDurability < customMaxDurability) {
                damageable.setDamage(1);
            }

            ItemMeta meta = (ItemMeta) damageable;
            meta.setLore(lore);
            ItemStack result = toRepair.getItem();
            result.setItemMeta(meta);
            return result;
        }
        return null;
    }

    private boolean itemAndRepairMaterialMatch(NBTItem toRepair, NBTItem material) {
        boolean matches = false;
        if (toRepair.hasKey("dragonstone") && material.getItem().getType() == Material.STRAY_SPAWN_EGG ||
                toRepair.hasKey("orichalcum") && material.getItem().getType() == Material.PARROT_SPAWN_EGG ||
                toRepair.hasKey("adamant") && material.getItem().getType() == Material.DROWNED_SPAWN_EGG ||
                toRepair.hasKey("mithril") && material.getItem().getType() == Material.GHAST_SPAWN_EGG
        ) {
            matches = true;
        }
        if (!(toRepair.hasKey("dragonstone") || toRepair.hasKey("orichalcum") ||
                toRepair.hasKey("adamant") || toRepair.hasKey("mithril"))) {
            if (isIronRepairableItem(toRepair.getItem()) && material.getItem().getType() == Material.IRON_INGOT ||
                    isDiamondRepairableItem(toRepair.getItem()) && material.getItem().getType() == Material.DIAMOND ||
                    isLeatherRepairableItem(toRepair.getItem()) && material.getItem().getType() == Material.LEATHER ||
                    isGoldRepairableItem(toRepair.getItem()) && material.getItem().getType() == Material.GOLD_INGOT ||
                    isNetheriteRepairableItem(toRepair.getItem()) && material.getItem().getType() == Material.NETHERITE_INGOT ||
                    toRepair.getItem().getType() == Material.BOW && material.getItem().getType() == Material.STRING ||
                    toRepair.getItem().getType() == Material.TRIDENT && material.getItem().getType() == Material.IRON_INGOT
            ) {
                matches = true;
            }

        }

        return matches;
    }

    private boolean isIronRepairableItem(ItemStack item) {
        return item.getType() == Material.IRON_SWORD ||
                item.getType() == Material.IRON_AXE ||
                item.getType() == Material.IRON_SHOVEL ||
                item.getType() == Material.IRON_PICKAXE ||
                item.getType() == Material.IRON_HOE ||
                item.getType() == Material.IRON_HELMET ||
                item.getType() == Material.IRON_CHESTPLATE ||
                item.getType() == Material.IRON_LEGGINGS ||
                item.getType() == Material.IRON_BOOTS;
    }

    private boolean isDiamondRepairableItem(ItemStack item) {
        return item.getType() == Material.DIAMOND_SWORD ||
                item.getType() == Material.DIAMOND_AXE ||
                item.getType() == Material.DIAMOND_SHOVEL ||
                item.getType() == Material.DIAMOND_PICKAXE ||
                item.getType() == Material.DIAMOND_HOE ||
                item.getType() == Material.DIAMOND_HELMET ||
                item.getType() == Material.DIAMOND_CHESTPLATE ||
                item.getType() == Material.DIAMOND_LEGGINGS ||
                item.getType() == Material.DIAMOND_BOOTS;

    }

    private boolean isLeatherRepairableItem(ItemStack item) {
        return item.getType() == Material.LEATHER_HELMET ||
                item.getType() == Material.LEATHER_CHESTPLATE ||
                item.getType() == Material.LEATHER_LEGGINGS ||
                item.getType() == Material.LEATHER_BOOTS;
    }

    private boolean isGoldRepairableItem(ItemStack item) {
        return item.getType() == Material.GOLDEN_SWORD ||
                item.getType() == Material.GOLDEN_AXE ||
                item.getType() == Material.GOLDEN_SHOVEL ||
                item.getType() == Material.GOLDEN_PICKAXE ||
                item.getType() == Material.GOLDEN_HOE ||
                item.getType() == Material.GOLDEN_HELMET ||
                item.getType() == Material.GOLDEN_CHESTPLATE ||
                item.getType() == Material.GOLDEN_LEGGINGS ||
                item.getType() == Material.GOLDEN_BOOTS;
    }

    private boolean isNetheriteRepairableItem(ItemStack item) {

        return item.getType() == Material.NETHERITE_SWORD ||
                item.getType() == Material.NETHERITE_AXE ||
                item.getType() == Material.NETHERITE_SHOVEL ||
                item.getType() == Material.NETHERITE_PICKAXE ||
                item.getType() == Material.NETHERITE_HOE ||
                item.getType() == Material.NETHERITE_HELMET ||
                item.getType() == Material.NETHERITE_CHESTPLATE ||
                item.getType() == Material.NETHERITE_LEGGINGS ||
                item.getType() == Material.NETHERITE_BOOTS;
    }
}


