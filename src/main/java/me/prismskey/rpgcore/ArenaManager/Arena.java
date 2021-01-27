package me.prismskey.rpgcore.ArenaManager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.prismskey.rpgcore.Enums.ArenaState;
import me.prismskey.rpgcore.Enums.PhaseState;
import me.prismskey.rpgcore.Events.onArenaFinish;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.*;

public class Arena {

    public String name;
    public int min;
    public int max;
    public int maxTime;
    public double keyDropChanceFactor;
    public String prizeKeyName;
    public int totalMobs = 0;
    public int totalKilledMobs = 0;
    public String mainRegion = null;
    public String friendlyName = "";

    public ArenaState arenaState;
    public LinkedHashMap<String, Phase> phases = new LinkedHashMap<>();
    public List<Player> players = new ArrayList<>();
    public Location spawnLocation;
    public int passedTime = 1;
    private HashMap<String, Location> previousLocations = new HashMap<>();
    public List<Entity> allMobsInArena = new ArrayList<>();
    public HashMap<String, Location> playerPhaseLocation = new HashMap<>();
    public List<String> listOfFinishedPhases = new ArrayList<>();
    //public Phase currentPhase;
    public Phase firstPhase;
    //private int checkingID = 0;
    public List<PrizeObject> prizeCommands = new ArrayList<>();
    public List<UUID> absentPlayers = new ArrayList<>();

    public Arena(String name, int min, int max, int maxTime, double keyDropChanceFactor, String prizeKeyName) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.maxTime = maxTime;
        this.keyDropChanceFactor = keyDropChanceFactor;
        this.prizeKeyName = prizeKeyName;
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
        this.totalKilledMobs = 0;
        this.totalMobs = 0;

        clearOutMobs();

        for (Phase phase : phases.values()) {
            phase.bossMobsRemaining = 0;
            phase.finalBossMobsRemaining = 0;
        }

        for (Phase phase : phases.values()) { //removing players from phases
            phase.resetPhase();
        }

        this.arenaState = ArenaState.AVAILABLE;
    }

    public void clearOutMobs() {
        for (Entity mob : allMobsInArena) { //removing remaining mobs
            if (!mob.isDead()) {
                mob.remove();
            }
        }
        allMobsInArena.clear();

        //ensure remaining mobs are removed
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(Bukkit.getWorld("dungeons")));
        if (regions != null) {
            ProtectedRegion region = regions.getRegion(mainRegion);
            Region rg = new CuboidRegion(region.getMaximumPoint(), region.getMinimumPoint());
            Location center = new Location(Bukkit.getWorld("dungeons"), rg.getCenter().getX(), rg.getCenter().getY(), rg.getCenter().getZ());
            Collection<Entity> entities = Bukkit.getWorld("dungeons").getNearbyEntities(center, rg.getWidth() / 2, rg.getHeight() / 2, rg.getLength() / 2);
            for (Entity entity : entities) {
                if (!(entity instanceof LivingEntity)) {
                    continue;
                }
                if (entity instanceof Player) {
                    continue;
                }
                if (entity instanceof Tameable) {
                    Tameable tameable = (Tameable) entity;
                    if (tameable.isTamed()) {
                        continue;
                    }
                }
                entity.remove();
            }
        }
    }

    public void setPassedTime(int passedTime) {
        this.passedTime = passedTime;
    }

    public boolean checkIfArenaIsReady() {
        boolean ready = true;

        Rpgcore.instance.getLogger().info("--------------------------------------------------------");
        Rpgcore.instance.getLogger().info(prizeKeyName + " " + keyDropChanceFactor);
        Rpgcore.instance.getLogger().info("" + phases.size());
        Rpgcore.instance.getLogger().info("" + spawnLocation);

        if (spawnLocation == null || phases.size() == 0 || prizeKeyName == null || keyDropChanceFactor <= 0 || mainRegion == null) {
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

        for (Phase phase : phases.values()) {
            this.firstPhase = phase;
            //this.currentPhase = phase;
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
                        player.sendTitle(Utils.color("&6&l" + friendlyName), Utils.color("&7You have &a " + maxTime + " &7Minutes to finish this dungeon."), 3 * 20, 5 * 20, 20);
                    }


                    startTimer();
                    checkIfPlayersAreInArena();

                } else {
                    if (countdown == 10) {
                        for (Player player : players) {
                            levels.put(player.getName(), player.getExp());
                            playerPhaseLocation.put(player.getName(), player.getLocation());

                        }
                        countdown = countdown - 1;
                    } else {
                        for (Player player : players) {

                            player.sendMessage(Utils.color("&7You will be teleported to dungeon in &6" + countdown + " &7second(s)."));


                        }
                        countdown = countdown - 1;
                    }

                }


            }
        }, 0, 20);
    }


    public void startTimer() {
        Bukkit.getScheduler().cancelTask(this.taskid);
        //checkingTimer();
        this.taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Rpgcore.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (arenaState == ArenaState.INGAME) {
                    setPassedTime(passedTime + 1);
                    if (passedTime / 60 >= maxTime) {
                        finishArena();

                    } else {
                        if (passedTime / 60 == 30) {

                            announceToAllPlayers("&7Remaining time: &e" + (maxTime - (passedTime / 60)) + " minutes");

                        } else if (passedTime / 60 == 15) {
                            announceToAllPlayers("&7Remaining time: &e" + (maxTime - (passedTime / 60)) + " minutes");
                        } else if (passedTime / 60 == 1) {
                            announceToAllPlayers("&7Remaining time: &e" + (maxTime - (passedTime / 60)) + " minutes");
                        }
                    }
                }

            }
        }, 0, 20);
    }


    public void finishArena() {
        //onArenaFinish event = new onArenaFinish(this.name, players);
        //Bukkit.getPluginManager().callEvent(event);
        Bukkit.getScheduler().cancelTask(taskid);
        Bukkit.getScheduler().cancelTask(checkIfPlayersAreInArenaTaskID);
        //Bukkit.getScheduler().cancelTask(checkingID);
        for (Player player : players) {

            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            for (ProtectedRegion region : set) {
                if (region.getId().equalsIgnoreCase(mainRegion)) {
                    player.teleport(previousLocations.get(player.getName()));
                }
            }
            shortTermStorages.playersInMatch.remove(player.getName());
        }

        announceToAllPlayers("&eDungeon ended.");
        //will finish next
        resetArenaStats();


    }

    public void cancelTimerTask() {
        Bukkit.getScheduler().cancelTask(taskid);
    }


    public void announceToAllPlayers(String str) {
        for (Player player : players) {
            player.sendMessage(Utils.color(str));
        }
    }

    private int checkIfPlayersAreInArenaTaskID = -1;

    public void checkIfPlayersAreInArena() {
        if (checkIfPlayersAreInArenaTaskID != -1) {
            Bukkit.getScheduler().cancelTask(checkIfPlayersAreInArenaTaskID);
        }
        checkIfPlayersAreInArenaTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Rpgcore.getInstance(), new Runnable() {
            @Override
            public void run() {

                for (Player player : players) {
                    boolean inDungeon = false;
                    if(!player.isOnline()) {
                        inDungeon = false;
                    } else {
                        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
                        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                        RegionQuery query = container.createQuery();
                        ApplicableRegionSet set = query.getApplicableRegions(loc);

                        for (ProtectedRegion region : set) {
                            if (region.getId().equalsIgnoreCase(mainRegion)) {
                                inDungeon = true;
                                break;
                            }
                        }
                    }
                    if (!inDungeon) {
                        if (absentPlayers.contains(player.getUniqueId()) || !player.isOnline()) {
                            shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName())).players.remove(player);
                            Arena playerArena = shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName()));
                            playerArena.players.remove(player);
                            shortTermStorages.playersInMatch.remove(player);
                            shortTermStorages.arenaHashMap.get(shortTermStorages.playersInMatch.get(player.getName())).checkIfStillArenaHasPlayer(player);
                            playerArena.absentPlayers.remove(player.getUniqueId());
                            if(player.isOnline()) {
                                player.sendMessage(Utils.color("&eYou have been kicked from the dungeon!"));
                            }
                        } else {
                            if(player.isOnline()) {
                                player.sendMessage(Utils.color("&4You have left the dungeon. Do /rejoin to rejoin the dungeon otherwise you will be kicked."));
                            }

                            absentPlayers.add(player.getUniqueId());
                        }
                    }
                }
            }
        }, 20, 20 * 30);
    }

    public void checkIfStillArenaHasPlayer(Player player) {

        Bukkit.getScheduler().scheduleSyncDelayedTask(Rpgcore.getInstance(), new Runnable() {
            @Override
            public void run() {

                if (players.size() == 0) {
                    finishArena();
                }

                if (!players.contains(player)) {
                    shortTermStorages.playersInMatch.remove(player.getName());
                }

            }
        }, 20 * 20);

    }

    SpawningSystem spawningSystem = new SpawningSystem();

    /*private void checkingTimer() {
        checkingID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Rpgcore.getInstance(), new Runnable() {
            @Override
            public void run() {

                if (currentPhase.spawnedEntities.size() == 0 && currentPhase.state == PhaseState.INGAME) {
                    //go for the next phase
                    spawningSystem.spawn(name);

                } else {

                    List<Entity> deletationEntities = new ArrayList<>();

                    for (Entity entity : currentPhase.spawnedEntities) {
                        if (entity.isDead()) {
                            deletationEntities.add(entity);
                        }
                    }

                    for (Entity entity : deletationEntities) {
                        currentPhase.spawnedEntities.remove(entity);
                    }

                    if (currentPhase.spawnedEntities.size() == 0 && currentPhase.state == PhaseState.INGAME) {
                        //go for the next phase
                        spawningSystem.spawn(name);
                    }


                    if (currentPhase.spawnedEntities.size() == 1) {
                        Entity entity = currentPhase.spawnedEntities.get(0);
                        entity.teleport(players.get(0));
                        System.out.println("Spawned");
                    }
                }

                System.out.println(currentPhase.spawnedEntities.size());


            }
        }, 0, 20 * 3);
    }*/

    public void setKeyDropChanceFactor(double factor) {
        keyDropChanceFactor = factor;
    }

    public void setPrizeKeyName(String name) {
        prizeKeyName = name;
    }

    public void setMainRegion(String region) {
        mainRegion = region;
    }

    public void setFriendlyName(String name) {
        friendlyName = name;
    }


}
