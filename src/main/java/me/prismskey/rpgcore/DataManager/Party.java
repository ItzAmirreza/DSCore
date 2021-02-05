package me.prismskey.rpgcore.DataManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Party {
    public static ArrayList<Party> parties = new ArrayList<>();
    public ArrayList<UUID> members = new ArrayList<>();
    public UUID hostUUID = null;

    public Party(UUID host) {
        this.hostUUID = host;
        members.add(host);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        if(uuid.equals(hostUUID)) {
            hostUUID = null;
        }
        members.remove(uuid);
        Player left = Bukkit.getPlayer(uuid);
        if(left != null) {
            for(Player player: getOnlineMembers()) {
                player.sendMessage(ChatColor.YELLOW + left.getName() + " has left the party.");
            }
        }

        if(hostUUID == null) {
            for(UUID potentialHost: members) {
                for(Player player: Bukkit.getOnlinePlayers()) {
                    if(player.getUniqueId().equals(potentialHost)) {
                        hostUUID = potentialHost;
                        break;
                    }
                }
                if(hostUUID != null) {
                    Player newHost = Bukkit.getPlayer(hostUUID);
                    for(UUID uuid1: members) {
                        for(Player player: Bukkit.getOnlinePlayers()) {
                            if(uuid1.equals(player.getUniqueId())) {
                                player.sendMessage(ChatColor.GREEN + newHost.getDisplayName() + " is the new party leader.");
                            }
                        }
                    }
                    break;
                }
            }
        }
        if(hostUUID == null) {
            parties.remove(this);
        }
    }

    public ArrayList<Player> getOnlineMembers() {
        ArrayList onlinePartyMembers = new ArrayList();
        for(UUID uuid: members) {
            for(Player player: Bukkit.getOnlinePlayers()) {
                if(player.getUniqueId().equals(uuid)) {
                    onlinePartyMembers.add(player);
                }
            }
        }
        return onlinePartyMembers;
    }

    public void messageAllPlayers(String message) {
        for(Player player: getOnlineMembers()) {
            player.sendMessage(message);
        }
    }

    public static boolean isInParty(UUID search) {
        for(Party party: parties) {
            for(UUID uuid: party.members) {
                if(uuid.equals(search)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Party getPartyByPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        for(Party party: parties) {
            if(party.members.contains(uuid)) {
                return party;
            }
        }
        return null;
    }

    public static void disbandParty(Party party) {
        for(Player player: party.getOnlineMembers()) {
            player.sendMessage(ChatColor.RED + "Your party was disbanded. You are no longer in a party.");
        }
        parties.remove(party);
    }
}
