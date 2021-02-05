package me.prismskey.rpgcore.DataManager;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PartyInvitation {
    public String invited;
    public Party party;
    public int timeTillExpiry;
    public final int EXPIRATION_TIME = 20 * 60;

    public static ArrayList<PartyInvitation> partyInvitations = new ArrayList<>();


    public PartyInvitation(String invited, Party party) {
        this.invited = invited;
        this.party = party;
        timeTillExpiry = EXPIRATION_TIME;
    }
}
