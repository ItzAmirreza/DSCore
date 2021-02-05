package me.prismskey.rpgcore.GeneralCommands;

//import com.gmail.nossr50.api.PartyAPI;
//import com.gmail.nossr50.datatypes.party.Party;
import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.DataManager.Party;
import me.prismskey.rpgcore.Enums.ArenaState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import scala.concurrent.impl.FutureConvertersImpl;

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
                    Party party = Party.getPartyByPlayer(player);

                    if (thatArena.arenaState != ArenaState.AVAILABLE) {
                        if(thatArena.arenaState == ArenaState.INGAME || thatArena.arenaState == ArenaState.RESETTING) {
                            player.sendMessage(Utils.color("&cThat dungeon is already in use. Please try again later."));
                        } else {
                            player.sendMessage(Utils.color("&cThis dungeon is not available right now."));
                        }
                        return false;
                    }
                    if(shortTermStorages.playersInMatch.containsKey(player.getName())) {
                        player.sendMessage(Utils.color("&cYou already joined a dungeon!"));
                        return false;
                    }
                    if (party == null) {
                        thatArena.addPlayer(player);
                        shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                        thatArena.startMatch();
                    } else {
                        //It does have a party
                        List<Player> playersInParty = party.getOnlineMembers();
                        if (playersInParty.size() <= thatArena.max) {

                            if (party.hostUUID.equals(player.getUniqueId()) ) {

                                for (Player player1 : playersInParty) {
                                    thatArena.addPlayer(player1);
                                }

                                shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                                shortTermStorages.arenaHashMap.get(thatArena.name).startMatch();

                            } else {

                                player.sendMessage(Utils.color("&cParty leader must start the party!"));

                            }


                        } else {
                            //Too many members / can not start the dungeon / passed the limit

                            player.sendMessage(Utils.color("&cYou can not enter the dungeon because the amount of your party members is higher than max limit of the dungeon. &eMax: " + thatArena.max));

                        }

                    }

                } else {
                    player.sendMessage(Utils.color("&cThis arena doesn't exist!"));
                }

            } else {
                player.sendMessage(Utils.color("&cPlease write the name of the arena for the second argument."));

            }

        }

        return false;
    }




}
