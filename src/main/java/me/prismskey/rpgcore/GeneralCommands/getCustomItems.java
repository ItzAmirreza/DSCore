package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.DataManager.ItemStackConfigLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class getCustomItems implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            for(ItemStack item: ItemStackConfigLoader.items) {
                player.getInventory().addItem(item);
            }
            player.updateInventory();
            player.sendMessage(ChatColor.GREEN + "Given custom items");
        }
        return true;
    }
}
