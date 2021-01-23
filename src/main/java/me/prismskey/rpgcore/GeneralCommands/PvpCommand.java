package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.DataManager.ConfigLoader;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCommand implements CommandExecutor {
    private ConfigLoader loader = new ConfigLoader();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            RPGPlayerData data = loader.getDataByUUID(player.getUniqueId());
            if(data.getPvpToggleCoolDown() > 0) {
                player.sendMessage("You must wait " + data.getPvpToggleCoolDown() / 20 + " seconds before you can use that command again.");
                return true;
            }
            if(args[0].equalsIgnoreCase("on")) {
                data.setPvpState(true);
                data.resetPvpToggleCooldown();
            } else if(args[0].equalsIgnoreCase("off")) {
                data.setPvpState(false);
                data.resetPvpToggleCooldown();
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /pvp <ON | OFF>");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /pvp <ON | OFF>");
        }

        return true;
    }
}
