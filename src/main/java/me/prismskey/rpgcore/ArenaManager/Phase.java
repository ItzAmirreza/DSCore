package me.prismskey.rpgcore.ArenaManager;

import me.prismskey.rpgcore.Enums.PhaseState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class Phase {

    public String name;
    public String region;
    public String arena; //not really reliable
    public List<EntityType> mobs = new ArrayList<>();
    public List<Player> playersInPhase = new ArrayList<>();
    public int mobSpawnRange;
    public PhaseState state = PhaseState.LOCKED;
    public List<Entity> spawnedEntities = new ArrayList<>();



    public Phase(String name, String arena, String region, int mobSpawnRange) {

        this.name = name;
        this.arena = arena.toLowerCase();
        this.region = region;
        this.mobSpawnRange = mobSpawnRange;

    }



    public void addMob(EntityType entity) {
        mobs.add(entity);
    }

    public void removeMob(EntityType entity) {
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

    }






}
