package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.DataManager.Party;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.ChatColor;
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
                Arena playerArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));
                playerArena.players.remove(player);
                shortTermStorages.playersInMatch.remove(player);
                shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName())).checkIfStillArenaHasPlayer(player);
                playerArena.absentPlayers.remove(player.getUniqueId());


                player.sendMessage(Utils.color("&eYou have left the arena!"));

                Party party = Party.getPartyByPlayer(player);
                if(party != null) {
                    party.removeMember(player.getUniqueId());
                    player.sendMessage(ChatColor.YELLOW + "Because you have left the arena you have been automatically kicked form your party.");
                }


            } else {
                player.sendMessage(Utils.color("&cYou are not in an arena."));
            }

        }

        return false;
    }
}
