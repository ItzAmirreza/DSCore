package me.prismskey.rpgcore.PartySystem;
import com.gmail.nossr50.datatypes.party.Party;
import org.bukkit.entity.Player;

public class DParty {

    private static Party partyAPI;

    public DParty() {

    }


    public boolean inParty(Player player) {

        if (partyAPI.getMembers().containsKey(player.getUniqueId())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLeader(Player player) {
        if (partyAPI.getLeader().getPlayerName().equalsIgnoreCase(player.getName())) {
            return true;
        } else {
            return false;
        }
    }
}
