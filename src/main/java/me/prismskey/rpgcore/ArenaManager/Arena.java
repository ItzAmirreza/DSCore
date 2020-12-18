package me.prismskey.rpgcore.ArenaManager;

import me.prismskey.rpgcore.Enums.ArenaState;
import me.prismskey.rpgcore.Enums.PhaseState;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Arena {

    public String name;
    public int min;
    public int max;
    public int maxTime;
    public ArenaState arenaState;
    public LinkedHashMap<String, Phase> phases = new LinkedHashMap<>();
    public List<Player> players = new ArrayList<>();
    public Location spawnLocation;
    public int passedTime = 1;
    private HashMap<String, Location> previousLocations = new HashMap<>();
    public List<Entity> allMobsInArena = new ArrayList<>();
    public HashMap<String, Location> playerPhaseLocation = new HashMap<>();
    public List<String> listOfFinishedPhases = new ArrayList<>();
    public Phase currentPhase;
    public Phase firstPhase;
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
        this.phases.put(phase.name, phase);
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
        this.previousLocations.clear();
        this.playerPhaseLocation.clear();
        this.listOfFinishedPhases.clear();
        this.passedTime = 0;
        for (Entity mob : allMobsInArena) { //removing remaining mobs
            if (!mob.isDead()) {
                mob.remove();

            }

        }
        allMobsInArena.clear();

        for (Phase phase : phases.values()) { //removing players from phases
            phase.resetPhase();
        }

        this.arenaState = ArenaState.AVAILABLE;
    }

    public void setPassedTime(int passedTime) {
        this.passedTime = passedTime;
    }

    public boolean checkIfArenaIsReady() {
        boolean ready = true;

        if (spawnLocation == null || phases.size() == 0) {
            ready = false;
        }


        if (ready) {
            this.arenaState = ArenaState.AVAILABLE;
        } else {
            this.arenaState = ArenaState.NOTREADY;
        }

        return ready;
    }

    public void setPhasesMap(LinkedHashMap<String, Phase> phasesMap) {

        this.phases = phasesMap;

    }

    private int taskid;

    public void startMatch() {
        this.arenaState = ArenaState.INGAME;

        for (Player player : players) { // getting previous locations to teleport them after they finished/left the dungeon
            previousLocations.put(player.getName(), player.getLocation());
            shortTermStorages.playersInMatch.put(player.getName(), name);
            player.setGameMode(GameMode.SURVIVAL);

        }

        for (Phase phase: phases.values()) {
            this.firstPhase = phase;
            this.currentPhase = phase;
            break;
        }

        this.firstPhase.state = PhaseState.INGAME;


        this.taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Rpgcore.getInstance(), new Runnable() {
            int countdown = 10;
            HashMap<String, Float> levels = new HashMap<>();
            @Override
            public void run() {

                if (countdown == 0) {

                    for (Player player : players) {
                        player.teleport(spawnLocation);
                        player.setExp(levels.get(player.getName()));
                        player.sendTitle(Utils.color("&6&l" + name), Utils.color("&7You have &a " + maxTime + " &7Minutes to finish this dungeon."), 3 * 20, 5 * 20, 20);
                    }


                    startTimer();

                } else {
                    if (countdown == 10) {
                        for (Player player : players) {
                            levels.put(player.getName(), player.getExp());
                            player.setLevel(countdown);
                            playerPhaseLocation.put(player.getName(), player.getLocation());

                        }
                        countdown = countdown - 1;
                    } else {
                        for (Player player : players) {

                            player.sendMessage(Utils.color("&7You will be teleported to dungeon in &6" + countdown + " &7second(s)."));
                            player.setLevel(countdown);


                        }
                        countdown = countdown - 1;
                    }

                }


            }
        }, 0, 20);
    }


    public void startTimer() {
        Bukkit.getScheduler().cancelTask(this.taskid);
        this.taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Rpgcore.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (arenaState == ArenaState.INGAME){
                    setPassedTime(passedTime + 1);
                    if (passedTime/60 >= maxTime) {
                        finishArena();

                    } else {
                        //if (passedTime/60 == 30) {

                         //   announceToAllPlayers("&7Remaining time: &e" + (maxTime - (passedTime/60)) + " minutes");

                        //} else if (passedTime/60 == 15) {
                        //    announceToAllPlayers("&7Remaining time: &e" + (maxTime - (passedTime/60)) + " minutes");
                       // } else if (passedTime/60 == 1) {
                        //    announceToAllPlayers("&7Remaining time: &e" + (maxTime - (passedTime/60)) + " minutes");
                        //}
                    }
                }

            }
        }, 0, 20);
    }


    public void finishArena() {
        Bukkit.getScheduler().cancelTask(taskid);
        for (Player player : players) {
            player.teleport(previousLocations.get(player.getName()));
            shortTermStorages.playersInMatch.remove(player.getName());
        }

        announceToAllPlayers("&eDungeon ended.");
        //will finish next
        resetArenaStats();


    }


    public void announceToAllPlayers(String str) {
        for (Player player : players) {
            player.sendMessage(Utils.color(str));
        }
    }

    public void checkIfStillArenaHasPlayer() {
        if (players.size() == 0) {
            finishArena();
        }
    }





}
