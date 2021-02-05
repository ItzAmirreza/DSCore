package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.DataManager.Party;
import me.prismskey.rpgcore.DataManager.PartyInvitation;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.Iterator;

public class PartyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /party create/join/invite/leave/disband (player_name)");
            return true;
        }
        String subcommand = args[0];
        Player player = (Player) sender;
        if(subcommand.equalsIgnoreCase("create")) {
            if(Party.getPartyByPlayer(player) == null) {
                Party party = new Party(player.getUniqueId());
                Party.parties.add(party);
                player.sendMessage(ChatColor.GREEN + "You created a party. Invite players with /party invite <player_name>");
            } else {
                player.sendMessage(ChatColor.RED + "You are already in a party. You must leave your current party before you can create a new one.");
            }
        } else if(subcommand.equalsIgnoreCase("disband")) {
            Party party = Party.getPartyByPlayer(player);
            if(party == null) {
                player.sendMessage(ChatColor.RED + "You are not in a party.");
            } else {
                Party.disbandParty(party);
            }
        } else if(subcommand.equalsIgnoreCase("invite")) {

            if(Utils.checkIfRegisteredForDungeon(player)) {
                player.sendMessage(ChatColor.RED + "You cannot invite anyone to your party while you are registered for a dungeon. You must leave the dungeon first.");
                return true;
            }

            if(args.length < 2) {
                player.sendMessage(ChatColor.RED + "You must specify a player to invite.");
            } else {
                Player invited = Bukkit.getPlayer(args[1]);
                if(invited == null || !invited.isOnline()) {
                    player.sendMessage(ChatColor.RED + "That player is not online. Did you spell their name correctly?");
                } else {
                    Party party = Party.getPartyByPlayer(player);
                    if(party.hostUUID.equals(player.getUniqueId())) {
                        PartyInvitation invitation = new PartyInvitation(invited.getName(), party);
                        PartyInvitation.partyInvitations.add(invitation);
                        player.sendMessage(ChatColor.GREEN + "Invitation sent!");
                        invited.sendMessage(ChatColor.GREEN + player.getName() + " has invited you to their party. Do /party join " + player.getName() + " to join the party.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Only the host can invite people.");
                    }
                }
            }
        } else if(subcommand.equalsIgnoreCase("join")) {

            if(Utils.checkIfRegisteredForDungeon(player)) {
                player.sendMessage(ChatColor.RED + "You cannot join a party if you are already registered for a dungeon. You must leave the dungeon with /leave to join the party.");
                return true;
            }

            if(args.length < 2) {
                player.sendMessage(ChatColor.RED + "Please specify whose party you would like to join.");
                return true;
            }
            Party party = Party.getPartyByPlayer(player);
            if(party != null) {
                player.sendMessage(ChatColor.RED + "You are already in a party. You must leave your current party before joining a new one.");
                return true;
            } else {
                Player inviter = Bukkit.getPlayer(args[1]);
                if(inviter == null || !inviter.isOnline()) {
                    player.sendMessage(ChatColor.RED + "That player is not online. Did you spell their name correctly?");
                    return true;
                }
                Party partyToJoin = Party.getPartyByPlayer(inviter);
                if(partyToJoin == null) {
                    player.sendMessage(ChatColor.RED + "Could not find a party to join with that host.");
                    return true;
                }
                Iterator it = PartyInvitation.partyInvitations.iterator();
                while(it.hasNext()) {
                    PartyInvitation invitation = (PartyInvitation) it.next();
                    if(invitation.party.equals(partyToJoin) && invitation.invited.equals(player.getName())) {
                        partyToJoin.messageAllPlayers(ChatColor.GREEN + player.getName() + " joined the party.");
                        partyToJoin.addMember(player.getUniqueId());
                        player.sendMessage(ChatColor.GREEN + "You have joined the party.");
                        it.remove();
                        return true;
                    }
                }
                player.sendMessage(ChatColor.RED + "You were not invited to that party.");
            }
        } else if(subcommand.equalsIgnoreCase("leave")) {
            Party party = Party.getPartyByPlayer(player);
            if(party == null) {
                player.sendMessage(ChatColor.RED + "You are not in a party.");
            } else {
                party.removeMember(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "You have left the party.");
            }
        }


        return true;
    }
}
