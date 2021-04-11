package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.DataManager.ItemStackConfigLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SaveCustomItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack held = player.getInventory().getItemInMainHand();
            if(held == null) {
                sender.sendMessage(ChatColor.RED + "Could not save item.");
                return true;
            }
            ItemStackConfigLoader.items.add(held);
            if(args.length != 1) {
                sender.sendMessage("bad usage");
                return true;
            }
            ItemStackConfigLoader.save(args[0], held);
            player.sendMessage(ChatColor.GREEN + "Saved Item");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Could not save item.");
        return true;

    }
}
