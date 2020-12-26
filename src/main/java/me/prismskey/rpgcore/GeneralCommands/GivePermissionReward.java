package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//syntax givetagpermission <player> <permission> <alternate currency reward>
public class GivePermissionReward implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && !sender.hasPermission("dungeonslayer.givepermissionreward")) {
            sender.sendMessage(ChatColor.RED + "You lack the required permission node to execute this command.");
            return true;
        }
        if(args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: givetagpermission <player> <permission> <alternate currency reward>");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            sender.sendMessage(ChatColor.RED + "Unable to find player with name " + args[0] + ".");
        }
        if(player.hasPermission(args[1])) {
            Rpgcore.getEconomy().depositPlayer(player, Double.parseDouble(args[2]));
            player.sendMessage(ChatColor.GREEN + "Since you already unlocked that reward, $" + args[2] + " was added to your balance instead.");
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + args[1] + " true");
        }
        return true;
    }
}
