package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.DataManager.ClickCombo;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

public class onPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        //checkPlayerInDungeon(event.getPlayer());
        tryCreatePlayerProfile(event.getPlayer());
        ClickCombo combo = new ClickCombo();
        shortTermStorages.clickComboHashMap.put(event.getPlayer().getUniqueId(), combo);

        if(Utils.checkIfInDungeon(event.getPlayer())) {
            event.getPlayer().resetPlayerTime();
            event.getPlayer().teleport(shortTermStorages.spawn);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot log back into a dungeon so you were teleported to spawn.");
        }
        if(Utils.isWithinRegion(event.getPlayer().getLocation(), "spawn")) {
            event.getPlayer().setPlayerTime(6000, false);
        }
    }

    private void tryCreatePlayerProfile(Player player) {
        RPGPlayerData data = new RPGPlayerData(player.getUniqueId());
        if(shortTermStorages.playerData.contains(data)) {
            return;
        }
        boolean pvp = shortTermStorages.pvpStatesConfiguration.getBoolean(player.getUniqueId().toString());
        data.setPvpState(pvp);
        shortTermStorages.playerData.add(data);
        if(data.getPvpState()) {
            player.sendMessage(ChatColor.GREEN + "PVP IS ENABLED FOR YOU.");
        } else {
            player.sendMessage(ChatColor.GREEN + "PVP IS DISABLED FOR YOU.");
        }
    }

    /*private void checkPlayerInDungeon(Player player) {
        if(Rpgcore.instance.isWithinDungeon(player.getLocation())) {
            player.teleport(Rpgcore.instance.getSpawn());
            player.sendMessage("Because you logged in inside a dungeon you have been teleported to spawn.");
        }
    }*/

}
