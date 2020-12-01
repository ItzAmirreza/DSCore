package me.prismskey.rpgcore.rpgcore;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class DisableCraftGridRepair implements Listener {
    @EventHandler
    public void onTryCraftRepair(PrepareItemCraftEvent event) {
        ItemStack[] matrix = event.getInventory().getMatrix();
        ItemStack result = event.getInventory().getResult();

        if (testForItemInGrid(matrix, Material.WOODEN_AXE) ||
                testForItemInGrid(matrix, Material.WOODEN_SWORD) ||
                testForItemInGrid(matrix, Material.WOODEN_SHOVEL) ||
                testForItemInGrid(matrix, Material.WOODEN_HOE) ||
                testForItemInGrid(matrix, Material.WOODEN_PICKAXE) ||
                testForItemInGrid(matrix, Material.LEATHER_BOOTS) ||
                testForItemInGrid(matrix, Material.LEATHER_CHESTPLATE) ||
                testForItemInGrid(matrix, Material.LEATHER_LEGGINGS) ||
                testForItemInGrid(matrix, Material.LEATHER_HELMET) ||

                testForItemInGrid(matrix, Material.IRON_AXE) ||
                testForItemInGrid(matrix, Material.IRON_SWORD) ||
                testForItemInGrid(matrix, Material.IRON_SHOVEL) ||
                testForItemInGrid(matrix, Material.IRON_HOE) ||
                testForItemInGrid(matrix, Material.IRON_PICKAXE) ||
                testForItemInGrid(matrix, Material.IRON_BOOTS) ||
                testForItemInGrid(matrix, Material.IRON_CHESTPLATE) ||
                testForItemInGrid(matrix, Material.IRON_LEGGINGS) ||
                testForItemInGrid(matrix, Material.IRON_HELMET) ||

                testForItemInGrid(matrix, Material.GOLDEN_AXE) ||
                testForItemInGrid(matrix, Material.GOLDEN_SWORD) ||
                testForItemInGrid(matrix, Material.GOLDEN_SHOVEL) ||
                testForItemInGrid(matrix, Material.GOLDEN_HOE) ||
                testForItemInGrid(matrix, Material.GOLDEN_PICKAXE) ||
                testForItemInGrid(matrix, Material.GOLDEN_BOOTS) ||
                testForItemInGrid(matrix, Material.GOLDEN_CHESTPLATE) ||
                testForItemInGrid(matrix, Material.GOLDEN_LEGGINGS) ||
                testForItemInGrid(matrix, Material.GOLDEN_HELMET) ||

                testForItemInGrid(matrix, Material.DIAMOND_AXE) ||
                testForItemInGrid(matrix, Material.DIAMOND_SWORD) ||
                testForItemInGrid(matrix, Material.DIAMOND_SHOVEL) ||
                testForItemInGrid(matrix, Material.DIAMOND_HOE) ||
                testForItemInGrid(matrix, Material.DIAMOND_PICKAXE) ||
                testForItemInGrid(matrix, Material.DIAMOND_BOOTS) ||
                testForItemInGrid(matrix, Material.DIAMOND_CHESTPLATE) ||
                testForItemInGrid(matrix, Material.DIAMOND_LEGGINGS) ||
                testForItemInGrid(matrix, Material.DIAMOND_HELMET) ||

                testForItemInGrid(matrix, Material.NETHERITE_AXE) ||
                testForItemInGrid(matrix, Material.NETHERITE_SWORD) ||
                testForItemInGrid(matrix, Material.NETHERITE_SHOVEL) ||
                testForItemInGrid(matrix, Material.NETHERITE_HOE) ||
                testForItemInGrid(matrix, Material.NETHERITE_PICKAXE) ||
                testForItemInGrid(matrix, Material.NETHERITE_BOOTS) ||
                testForItemInGrid(matrix, Material.NETHERITE_CHESTPLATE) ||
                testForItemInGrid(matrix, Material.NETHERITE_LEGGINGS) ||
                testForItemInGrid(matrix, Material.NETHERITE_HELMET) ||

                testForItemInGrid(matrix, Material.TURTLE_HELMET) ||
                testForItemInGrid(matrix, Material.CHAINMAIL_BOOTS) ||
                testForItemInGrid(matrix, Material.CHAINMAIL_CHESTPLATE) ||
                testForItemInGrid(matrix, Material.CHAINMAIL_HELMET) ||
                testForItemInGrid(matrix, Material.CHAINMAIL_LEGGINGS) ||
                testForItemInGrid(matrix, Material.BOW) ||
                testForItemInGrid(matrix, Material.TRIDENT) ||
                testForItemInGrid(matrix, Material.FLINT_AND_STEEL) ||
                testForItemInGrid(matrix, Material.CARROT_ON_A_STICK) ||
                testForItemInGrid(matrix, Material.WARPED_FUNGUS_ON_A_STICK)) {

            event.getInventory().setResult(null);
        }
    }

    private boolean testForItemInGrid(ItemStack[] items, Material mat) {
        for (int i = 0; i < items.length; i++) {
            ItemStack current = items[i];
            if (current != null && current.getType() == mat) {
                return true;
            }
        }
        return false;
    }
}
