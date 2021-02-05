package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.DataManager.PartyInvitation;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class PartyInvitationTask extends BukkitRunnable {

    @Override
    public void run() {
        Iterator it = PartyInvitation.partyInvitations.iterator();
        while(it.hasNext()) {
            PartyInvitation invitation = (PartyInvitation) it.next();
            invitation.timeTillExpiry--;
            if(invitation.timeTillExpiry <= 0) {
                it.remove();
            }
        }
    }
}
