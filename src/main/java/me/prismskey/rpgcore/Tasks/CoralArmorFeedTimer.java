package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class CoralArmorFeedTimer extends BukkitRunnable {

    private Rpgcore plugin;
    public CoralArmorFeedTimer(Rpgcore plugin) {
        this.plugin = plugin;
    }


    @Override
    public void run() {
        for(Player player: Bukkit.getOnlinePlayers()) {
            if(player.getLocation().getBlock().getType() == Material.WATER) {
                NBTItem helmet = new NBTItem(player.getInventory().getHelmet());
                NBTItem chestplate = new NBTItem(player.getInventory().getChestplate());
                NBTItem leggings = new NBTItem(player.getInventory().getLeggings());
                NBTItem boots = new NBTItem(player.getInventory().getBoots());

                if(helmet.hasKey("coral") && chestplate.hasKey("coral") && leggings.hasKey("coral") && boots.hasKey("coral")) {
                    player.setFoodLevel(Math.min(20, player.getFoodLevel() + 1));
                }
            }
        }
    }
}
