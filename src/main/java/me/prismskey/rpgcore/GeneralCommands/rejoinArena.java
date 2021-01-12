package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class rejoinArena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (shortTermStorages.playersInMatch.containsKey(player.getName())) {

                Arena playerArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));
                player.teleport(playerArena.spawnLocation);
                playerArena.players.add(player);
                player.sendMessage(Utils.color("&aYou have returned to dungeon."));


            } else {

                player.sendMessage(Utils.color("&e You are not in any arena to rejoin!"));

            }

        }

        return false;
    }
}
