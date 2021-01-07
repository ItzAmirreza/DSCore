package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class onArenaFinish extends Event {

    private String arena;
    private List<Player> playerList;
    private static final HandlerList HANDLERS_LIST = new HandlerList();



    public onArenaFinish(String arena, List<Player> players){
        this.arena = arena;
        this.playerList = players;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public String getArenaName() {
        return arena;
    }

    public Arena getArena() {
        return shortTermStorages.arenaHashMap.get(this.arena);
    }


    public List<Player> getPlayerList() {
        return playerList;
    }


}
