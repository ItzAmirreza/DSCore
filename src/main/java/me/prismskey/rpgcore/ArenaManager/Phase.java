package me.prismskey.rpgcore.ArenaManager;

import org.bukkit.entity.Player;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

public class Phase {

    public final String name;
    public final String region;
    public List<Entity> mobs = new ArrayList<>();
    public List<Player> playersInPhase = new ArrayList<>();
    public int mobSpawnRange;



    public Phase(String name, String region, int mobSpawnRange) {

        this.name = name;
        this.region = region;
        this.mobSpawnRange = mobSpawnRange;

    }



    public void addMob(Entity entity) {
        mobs.add(entity);
    }

    public void removeMob(Entity entity) {
        mobs.remove(entity);
    }

    public void addPlayer(Player player) {
        playersInPhase.add(player);
    }

    public void removePlayer(Player player) {
        playersInPhase.remove(player);
    }

    public void resetPlayers() {
        playersInPhase.clear();
    }




}
