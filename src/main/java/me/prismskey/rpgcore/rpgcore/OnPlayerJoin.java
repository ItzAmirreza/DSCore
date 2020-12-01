package me.prismskey.rpgcore.rpgcore;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.List;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        checkPlayerInDungeon(event.getPlayer());
        tryCreatePlayerProfile(event.getPlayer());
    }

    private void tryCreatePlayerProfile(Player player) {
        RPGPlayerData data = new RPGPlayerData(player.getUniqueId());
        if(Rpgcore.instance.getPlayerData().contains(data)) {
            return;
        }

        Rpgcore.instance.getPlayerData().add(data);
        if(data.getPvpState()) {
            player.sendMessage(ChatColor.GREEN + "PVP IS ENABLED FOR YOU.");
        } else {
            player.sendMessage(ChatColor.GREEN + "PVP IS DISABLED FOR YOU.");
        }
    }

    private void checkPlayerInDungeon(Player player) {
        if(Rpgcore.instance.isWithinDungeon(player.getLocation())) {
            player.teleport(Rpgcore.instance.getSpawn());
            player.sendMessage("Because you logged in inside a dungeon you have been teleported to spawn.");
        }
    }
}
