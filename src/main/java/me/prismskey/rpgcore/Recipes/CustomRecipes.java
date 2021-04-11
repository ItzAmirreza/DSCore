package me.prismskey.rpgcore.Recipes;

import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomRecipes implements Listener {
    @EventHandler
    public void onCraftPreprocess(PrepareItemCraftEvent event) {
        ItemStack[] matrix = event.getInventory().getMatrix();
        checkForHarpyClawDaggerRecipe(matrix, event);
    }

    private void checkForHarpyClawDaggerRecipe(ItemStack[] matrix, PrepareItemCraftEvent event) {



        if(matrix.length == 9) {
            if(matrix[4] != null && matrix[4].getItemMeta().getCustomModelData() == 8049) {
                if(matrix[7] != null && matrix[7].getType() == Material.STICK) {
                    ItemStack result = Utils.customItemConstructor(Material.IRON_SWORD, ChatColor.WHITE + "Harpy Claw Dagger", 7701);
                    event.getInventory().setResult(result);
                }
            }
            //small grid
        } else {
            if(matrix[0] != null && matrix[0].getItemMeta().getCustomModelData() == 8049) {
                if(matrix[1] != null && matrix[1].getType() == Material.STICK) {
                    ItemStack result = Utils.customItemConstructor(Material.IRON_SWORD, ChatColor.WHITE + "Harpy Claw Dagger", 7701);
                    event.getInventory().setResult(result);
                }
            }
        }
    }
}
