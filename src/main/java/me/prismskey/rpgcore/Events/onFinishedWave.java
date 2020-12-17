package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.Phase;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class onFinishedWave extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Arena arena;
    private final int passedWave;
    private final Phase phase;

    public onFinishedWave(Arena arena, int passedWave, Phase phase) {
        this.arena = arena;
        this.passedWave = passedWave;
        this.phase = phase;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Arena getArena() {
        return arena;
    }

    public int getPassedWave() {
        return passedWave;

    }
    public Phase getPhase() {
        return phase;
    }


}
