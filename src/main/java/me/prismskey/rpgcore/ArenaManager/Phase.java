package me.prismskey.rpgcore.ArenaManager;

import me.prismskey.rpgcore.Enums.PhaseState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class Phase {

    public String name;
    public String region;
    public int wavescount;
    public String arena; //not really reliable
    public Location center;
    public List<DMob> mobs = new ArrayList<>();
    public List<String> startCommands = new ArrayList<>();
    public List<String> endCommands = new ArrayList<>();
    public List<Player> playersInPhase = new ArrayList<>();
    public int mobSpawnRange;
    public PhaseState state = PhaseState.LOCKED;
    public List<Entity> spawnedEntities = new ArrayList<>();
    public int current_wave;
    public boolean firstTime = true;
    public int finalBossMobs = 0;
    public int bossMobs = 0; // not including final boss mobs.
    public int bossMobsRemaining = 0;
    public int finalBossMobsRemaining = 0;



    public Phase(String name, String arena, String region, int mobSpawnRange, int wavescount, Location center) {

        this.name = name;
        this.arena = arena.toLowerCase();
        this.region = region;
        this.mobSpawnRange = mobSpawnRange;
        this.wavescount = wavescount;
        this.center = center;

    }



    public void addMob(DMob entity) {
        mobs.add(entity);
    }

    public void removeMob(DMob entity) {
        mobs.remove(entity);
    }

    public void addPlayer(Player player) {
        playersInPhase.add(player);
    }

    public void removePlayer(Player player) {
        playersInPhase.remove(player);
    }

    public void resetPhase() {
        playersInPhase.clear();
        this.spawnedEntities.clear();
        this.state = PhaseState.LOCKED;
        this.current_wave = 0;
        this.firstTime = true;

    }
    public void setWavescount(int num) {
        this.wavescount = num;
    }






}
