package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (shortTermStorages.playersInMatch.containsKey(event.getEntity().getName())) {
            OfflinePlayer offplayer = Bukkit.getOfflinePlayer(player.getName());
            double playerMoney = Rpgcore.getEconomy().getBalance(offplayer);
            int threepercentpunishment = (int)(playerMoney / 100)*3;

            Rpgcore.getEconomy().withdrawPlayer(offplayer, threepercentpunishment);

            Arena playerArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));
            playerArena.players.remove(player);
            playerArena.checkIfStillArenaHasPlayer(player);
            player.sendMessage(Utils.color("&cYou died in dungeon and lost 3% of your money!"));
            player.sendMessage(Utils.color("&eAttention! &7You have less than a minute to &b/rejoin&7 to your previous dungeon, otherwise you will abandon the dungeon automatically."));
        }

    }

}
