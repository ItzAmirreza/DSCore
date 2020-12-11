package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.Enums.ArenaState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class joinArena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length > 0) {

                String arenaName = args[0].toLowerCase();
                if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
                    Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);

                    if (thatArena.arenaState == ArenaState.AVAILABLE) {

                        thatArena.addPlayer(player);
                        shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                        thatArena.startMatch();

                    } else{
                        player.sendMessage(Utils.color("&cThis arena is not available right now."));
                    }


                } else {
                    player.sendMessage(Utils.color("&cThis arena doesn't exist!"));
                }

            } else {
                player.sendMessage(Utils.color("&cPlease write the name of the arena for second argument."));

            }

        }

        return false;
    }




}
