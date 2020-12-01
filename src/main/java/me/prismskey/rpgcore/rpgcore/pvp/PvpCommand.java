package me.prismskey.rpgcore.rpgcore.pvp;

import me.prismskey.rpgcore.rpgcore.RPGPlayerData;
import me.prismskey.rpgcore.rpgcore.Rpgcore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PvpCommand extends BukkitCommand {

    public PvpCommand(String name) {
        super(name);
        this.description = "A command for toggling pvp";
        this.usageMessage = "implement usage";
        this.setPermission("rpgcore.pvp");
        this.setAliases(new ArrayList<String>());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        Player player = (Player) sender;
        RPGPlayerData data = Rpgcore.instance.getDataByUUID(player.getUniqueId());
        if(args.length < 1) {
            if(data.getPvpState()) {
                player.sendMessage("PVP IS ENABLED. USE /pvp off TO DISABLE");
            } else {
                player.sendMessage("PVP IS DISABLED. USE /pvp on TO ENABLE");
            }
            return true;
        }
        String option = args[0];

        if(data == null) {
            sender.sendMessage("Error: null data. Please report this to staff.");
        }
        if(option.equalsIgnoreCase("on")) {
            data.setPvpState(true);
            Rpgcore.instance.updatePlayerData(data);
            sender.sendMessage(ChatColor.GREEN + "PVP TOGGLED ON.");
        } else if(option.equalsIgnoreCase("off")) {
            data.setPvpState(false);
            Rpgcore.instance.updatePlayerData(data);
            sender.sendMessage(ChatColor.GREEN + "PVP TOGGLED OFF.");
        } else {
            sender.sendMessage("please specify a valid option. Valid options are \"on\" or \"off.\"");
        }
        return true;
    }
}
