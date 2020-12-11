package me.prismskey.rpgcore.GeneralCommands;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.Enums.ArenaState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class joinArena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length > 0) {

                String arenaName = args[0].toLowerCase();
                if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
                    Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
                    Party mcmmoParty = PartyAPI.getPartyByPlayer(player);
                    if (mcmmoParty == null) {
                        if (thatArena.arenaState == ArenaState.AVAILABLE) {

                            thatArena.addPlayer(player);
                            shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                            thatArena.startMatch();

                        } else{
                            player.sendMessage(Utils.color("&cThis arena is not available right now."));
                        }
                    } else {
                        //It does have a party
                        List<Player> playersInParty = mcmmoParty.getOnlineMembers();
                        if (playersInParty.size() <= thatArena.max) {

                            if (mcmmoParty.getLeader().getUniqueId() == player.getUniqueId()) {

                                for (Player player1 : playersInParty) {
                                    thatArena.addPlayer(player1);
                                }

                                shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                                thatArena.startMatch();

                            } else {

                                player.sendMessage(Utils.color("&cParty leader must start the party!"));

                            }


                        } else {
                            //Too many members / can not start the dungeon / passed the limit

                            player.sendMessage(Utils.color("&cYou can not enter the dungeon because amount of your party members are higher than max limit of dungeon. &eMax: " + thatArena.max));

                        }

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
