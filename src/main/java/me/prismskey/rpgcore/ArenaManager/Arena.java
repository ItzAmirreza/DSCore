package me.prismskey.rpgcore.ArenaManager;

import me.prismskey.rpgcore.Enums.ArenaState;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    public String name;
    public int min;
    public int max;
    public int maxTime;
    public ArenaState arenaState;
    public List<Phase> phases = new ArrayList<>();
    public List<Player> players = new ArrayList<>();
    public Location spawnLocation;
    public int passedTime = 0;


    public Arena(String name, int min, int max, int maxTime) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.maxTime = maxTime;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
    }

    public void setSpawnLocation(String location) {
        Location location1 = Utils.convertStringToLoc(location);
        this.spawnLocation = location1;
    }

    public void addPhase(Phase phase) {
        this.phases.add(phase);
    }
    public void removePhase(Phase phase) {
        this.phases.remove(phase);
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
    }
    public ArenaState getArenaState() {
        return this.arenaState;
    }
    public void setArenaState(ArenaState state) {
        this.arenaState = state;
    }

    public void resetArenaStats() {
        this.arenaState = ArenaState.RESETTING;
        this.players.clear();
        this.passedTime = 0;
    }

    public void setPassedTime(int passedTime) {
        this.passedTime = passedTime;
    }

    public boolean checkIfArenaIsReady() {

        if (spawnLocation != null && phases.size() != 0) {
            return true;
        } else {
            return false;
        }
    }





}
