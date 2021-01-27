package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.DataManager.ConfigLoader;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private ConfigLoader loader = new ConfigLoader();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        shortTermStorages.clickComboHashMap.remove(player.getUniqueId());

        if (shortTermStorages.playersInMatch.containsKey(player.getName())) {
            Arena playerArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));

            playerArena.players.remove(player);
            playerArena.checkIfStillArenaHasPlayer(player);
            shortTermStorages.playersInMatch.remove(player);
        }

        RPGPlayerData data = loader.getDataByUUID(player.getUniqueId());
        shortTermStorages.pvpStatesConfiguration.set(player.getUniqueId().toString(), data.getPvpState());

    }
}
