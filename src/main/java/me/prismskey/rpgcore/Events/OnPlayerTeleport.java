package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleport implements Listener {
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(Utils.checkIfInDungeon(event.getFrom()) && !Utils.checkIfInDungeon(event.getTo())) {
            event.getPlayer().resetPlayerTime();
        }
        if(!Utils.checkIfInDungeon(event.getFrom()) && !Utils.checkIfInDungeon(event.getTo())) {
            event.getPlayer().resetPlayerTime();
        }
        if(Utils.isWithinRegion(event.getTo(), "spawn")) {
            event.getPlayer().setPlayerTime(6000, false);
        }
    }
}
