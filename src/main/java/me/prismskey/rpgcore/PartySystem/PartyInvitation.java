package me.prismskey.rpgcore.PartySystem;

/*public class PartyInvitation {
    private UUID host = null;
    private UUID invited;
    public static ArrayList<PartyInvitation> invitations = new ArrayList<>();

    public PartyInvitation(UUID host, UUID invited) {
        PartyInvitation instance = this;
        this.host = host;
        this.invited = invited;
        new BukkitRunnable() {

            @Override
            public void run() {
                boolean removed = invitations.remove(instance);
                Rpgcore.instance.getLogger().info("Was removed: " + removed);

                if(removed) {
                    Player hostPlayer = Bukkit.getPlayer(host);
                    Player invitedPlayer = Bukkit.getPlayer(invited);
                    hostPlayer.sendMessage("Your invitation for " + invitedPlayer.getName() + " has expired.");
                    invitedPlayer.sendMessage(hostPlayer.getName() + "'s invitation has expired.");
                }

            }
        }.runTaskLater(Rpgcore.instance, 20 * 5);
    }

    public static boolean invitationExistsForPlayer(UUID uuid) {
        for(PartyInvitation invitation: invitations) {
            if(invitation.getInvited().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static void removeAllInvitationsForParty(Party party) {
        Iterator it = invitations.iterator();
        while(it.hasNext()) {
            PartyInvitation invite = (PartyInvitation) it.next();
            if(party.getOwnerUUID().equals(invite.getHost())) {

                Player invited = Bukkit.getPlayer(invite.getInvited());
                if(invited != null) {
                    invited.sendMessage("Your party invitation has expired prematurely since the party was disbanded.");
                }

                it.remove();
            }
        }
    }

    public UUID getHost() {
        return host;
    }

    public void setHost(UUID host) {
        this.host = host;
    }

    public UUID getInvited() {
        return invited;
    }

    public void setInvited(UUID invited) {
        this.invited = invited;
    }
}*/
