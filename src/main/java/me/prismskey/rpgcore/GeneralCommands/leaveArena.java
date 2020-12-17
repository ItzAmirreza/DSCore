package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class leaveArena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (shortTermStorages.playersInMatch.containsKey(player.getName())) {

                shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName())).players.remove(player);
                Location teleporting = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName())).playerPhaseLocation.get(player.getName());
                player.teleport(teleporting);
                shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName())).checkIfStillArenaHasPlayer();
                player.sendMessage(Utils.color("&eYou have left the arena!"));



            } else {
                player.sendMessage(Utils.color("&cYou are not in an arena."));
            }

        }

        return false;
    }
}
