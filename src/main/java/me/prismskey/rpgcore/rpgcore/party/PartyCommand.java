package me.prismskey.rpgcore.rpgcore.party;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class PartyCommand {

}

//public class PartyCommand extends BukkitCommand {
    /*
    public PartyCommand(String name) {
        super(name);
        this.description = "A party command";
        this.usageMessage = "implement usage";
        this.setPermission("rpgcore.party");
        this.setAliases(new ArrayList<String>());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(args.length < 1) {
            sender.sendMessage("Incorrect usage");
            return true;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        String subcommand = args[0];
        if(subcommand.equalsIgnoreCase("create")) {
            return create(player);
        } else if(subcommand.equalsIgnoreCase("invite")) {
            return invite(player, args);
        } else if(subcommand.equalsIgnoreCase("join")) {
            return join(player);
        } else if(subcommand.equalsIgnoreCase("leave")) {
            return leave(player);
        } else if(subcommand.equalsIgnoreCase("disband")) {
            return disband(player);
        }

        return true;
    }

    public boolean create(Player player) {
        if(Party.playerInParty(player.getUniqueId())) {
            player.sendMessage("You are already in a party");
            return true;
        }
        DungeonParty party = new Party(player.getUniqueId());
        Party.parties.add(party);
        player.sendMessage("You have created a party");
        return true;
    }

    public boolean invite(Player player, String args[]) {
        if(args.length < 2) {
            player.sendMessage("Incorrect usage");
            return true;
        }
        String invitedName = args[1];
        Player invited = Bukkit.getPlayer(invitedName);

        if(!Party.isHosting(player.getUniqueId())) {
            player.sendMessage("You can only invite players if you are hosting a party.");
            return true;
        }

        if(invited == null) {
            player.sendMessage("That player is not online.");
            return true;
        }
        if(Party.playerInParty(invited.getUniqueId())) {
            player.sendMessage("That player is already in a party");
            return true;
        }
        if(PartyInvitation.invitationExistsForPlayer(invited.getUniqueId())) {
            player.sendMessage("That player already has a pending invitation.");
            return true;
        }
        PartyInvitation invitation = new PartyInvitation(player.getUniqueId(), invited.getUniqueId());
        //invitation.setInvited(invited.getUniqueId());
        //invitation.setHost(player.getUniqueId());
        PartyInvitation.invitations.add(invitation);
        invited.sendMessage(player.getName() + " has invited you to their party.");
        player.sendMessage("Invitation sent.");
        return true;
    }*/

    /*public boolean join(Player player){
        Iterator it = PartyInvitation.invitations.iterator();
        while(it.hasNext()) {
            PartyInvitation invitation = (PartyInvitation) it.next();
            if(invitation.getInvited().equals(player.getUniqueId())) {
                UUID owner = invitation.getHost();
                Player hostPlayer = Bukkit.getPlayer(owner);

                if(hostPlayer == null) {
                    player.sendMessage("The player of the party you are trying to join is offline.");
                    return true;
                }

                Party party = Party.getPartyByPlayer(owner);
                if(party == null) {
                    player.sendMessage("That party no longer exists.");
                    return true;
                }

                for(Player pl: party.getAllOnlinePlayers()) {
                    pl.sendMessage(player.getName() + " has joined the party.");
                }

                Party.addPartyMember(owner, player.getUniqueId());
                player.sendMessage("You have joined " + hostPlayer.getName() + "'s party.");

                it.remove();
                break;
            }
        }*/
        //remove the rest of the party invitations
        /*it = PartyInvitation.invitations.iterator();
        while(it.hasNext()) {
            PartyInvitation invitation = (PartyInvitation) it.next();
            if(invitation.getInvited().equals(player.getUniqueId())) {
                it.remove();
            }
        }*/
        /*return true;
    }*/

    /*public boolean leave(Player player) {
        Party party = Party.getPartyByPlayer(player.getUniqueId());
        if(party == null) {
            player.sendMessage("You are not in a party");
        }
        if(party.getOwnerUUID().equals(player.getUniqueId())) {
            Party.disbandParty(party);
            player.sendMessage("You have disbanded the party.");
        } else {
            party.removeMember(player.getUniqueId());
            player.sendMessage("You have left the party.");
            party.messageAllPlayers(player.getName() + " has left the party.");
        }
        return true;
    }

    public boolean disband(Player player) {
        Party party = Party.getPartyByPlayer(player.getUniqueId());
        if(party == null) {
            player.sendMessage("You are not in a party");
            return true;
        }
        if(party.getOwnerUUID().equals(player.getUniqueId())) {
            Party.disbandParty(party);
            player.sendMessage("You have disbanded the party.");
        } else {
            player.sendMessage("You are not the owner of this party.");
            return true;
        }
        return true;
    }*/

//}
