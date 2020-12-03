package me.prismskey.rpgcore.PartySystem;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.party.PartyLeader;
import me.prismskey.rpgcore.ArenaManager.Arena;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DungeonParty {
    //private UUID ownerUUID;
    //private ArrayList<UUID> memberIDs;
    public static ArrayList<DungeonParty> parties = new ArrayList<>();
    private Arena arena;
    public Party mcmmoParty;


    public DungeonParty(Party party) {
        mcmmoParty = party;
    }


    /*public Party(UUID ownerUUID) {


        /*this.ownerUUID = ownerUUID;
        memberIDs = new ArrayList<>();
        arena = null;
    }*/


    /*public static void addPartyMember(UUID owner, UUID member) {
        for(int i = 0; i < parties.size(); i++) {
            Party party = parties.get(i);
            if(party.ownerUUID.equals(owner)) {
                ArrayList<UUID> members = party.getPartyMembersIDs();
                members.add(member);
                party.setMemberIDs(members);
                parties.set(i, party);
                return;
            }
        }
    }

    public static boolean playerInParty(UUID uuid) {
        for(Party party: parties) {
            if(uuid.equals(party.getOwnerUUID())) {
                return true;
            }
            for(UUID memberID: party.getPartyMembersIDs()) {
                if(uuid.equals(memberID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Party getPartyByPlayer(UUID uuid) {
        for(Party party: parties) {
            if(uuid.equals(party.getOwnerUUID())) {
                return party;
            }
            for(UUID memberID: party.getPartyMembersIDs()) {
                if(uuid.equals(memberID)) {
                    return party;
                }
            }
        }
        return null;
    }

    public static boolean isHosting(UUID uuid) {
        for(Party party: parties) {
            if(uuid.equals(party.getOwnerUUID())) {
                return true;
            }
        }
        return false;
    }*/

    public boolean allPlayersOnline() {
        return mcmmoParty.getMembers().size() == mcmmoParty.getOnlineMembers().size();
        /*Player owner = Bukkit.getPlayer(ownerUUID);
        if(owner == null) {
            return false;
        }
        if(!owner.isOnline()) {
            return false;
        }

        for(UUID uuid: memberIDs) {
            Player member = Bukkit.getPlayer(uuid);
            if(member == null) {
                return false;
            }
            if(!member.isOnline()) {
                return false;
            }
        }
        return true;*/
    }

    /*public static void disbandParty(Party party) {

        for(Player p: party.getAllOnlinePlayers()) {
            if(!party.getOwnerUUID().equals(p.getUniqueId())) {
                p.sendMessage("The party host has ended the party.");
            }

            if(Rpgcore.instance.isWithinDungeon(p.getLocation())) {
                p.teleport(Rpgcore.instance.getSpawn());
                p.sendMessage("Since the host ended the party you have been kicked from the dungeon.");
            }
        }

        PartyInvitation.removeAllInvitationsForParty(party);
        Arena arena = party.getArena();
        if(arena != null) {
            arena.reset();
        }
        parties.remove(party);
    }*/

    /*public ArrayList<Player> getAllOnlinePlayers() {
        //implement
        ArrayList<Player> players = new ArrayList<>();
        Player owner = Bukkit.getPlayer(ownerUUID);
        if(owner != null && owner.isOnline()) {
            players.add(owner);
        }

        for(UUID memberID: memberIDs) {
            Player member = Bukkit.getPlayer(memberID);
            if(member != null && member.isOnline()) {
                players.add(member);
            }
        }
        return players;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public ArrayList<UUID> getPartyMembersIDs() {
        return memberIDs;
    }

    public void setMemberIDs(ArrayList<UUID> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public void removeMember(UUID uuid) {
        memberIDs.remove(uuid);
    }*/

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void messageAllPlayers(String msg) {
        for(Player player: mcmmoParty.getOnlineMembers()) {
            player.sendMessage(msg);
        }
    }
}
