package me.prismskey.rpgcore.ArenaManager;

import com.gmail.nossr50.api.PartyAPI;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.PartySystem.DungeonParty;
import me.prismskey.rpgcore.Utils.APIUsages;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class Arena {
    private Location spawn;
    public ArrayList<ArenaPhase> phases;
    private int currentPhaseIndex;
    private DungeonParty party;
    private String regionName;
    private String name;
    BukkitRunnable completionCheckRunnable;
    BukkitRunnable endDungeonRunnable;
    BukkitRunnable dungeonTimeoutRunnable;
    Random random;

    private boolean running;

    public arenaLoader arenaloader = new arenaLoader();

    public Arena(Location spawn, String name, String regionName) {
        party = null;
        currentPhaseIndex = 0;
        phases = new ArrayList<>();
        this.spawn = spawn;
        this.name = name;
        this.regionName = regionName;
        completionCheckRunnable = null;
        endDungeonRunnable = null;
        dungeonTimeoutRunnable = null;
        running = false;
        random = new Random();
    }

    public void reset() {

        currentPhaseIndex = 0;
        running = false;
        for (ArenaPhase phase : phases) {
            if (phase instanceof KillAllPhase) {
                KillAllPhase kap = (KillAllPhase) phase;
                kap.setTotalKillsForPhase(0);
                kap.setKillsRemaining(0);
            }
        }
        if (completionCheckRunnable != null) {
            completionCheckRunnable.cancel();
        }
        if (endDungeonRunnable != null) {
            endDungeonRunnable.cancel();
        }
        if(dungeonTimeoutRunnable != null) {
            dungeonTimeoutRunnable.cancel();
        }
        endDungeonRunnable = null;
        dungeonTimeoutRunnable = null;

        for (Player player : party.mcmmoParty.getOnlineMembers()) {
            if(arenaloader.isWithinDungeon(player.getLocation())) {
                player.sendMessage("Dungeon Resetting.");
                player.teleport(shortTermStorages.spawn);
            }
        }
        party.setArena(null);
        party = null;
    }

    public String getName() {
        return name;
    }

    public void start() {
        ArrayList<Player> players = new ArrayList<Player>(PartyAPI.getOnlineMembers(party.mcmmoParty.getName()));
        running = true;
        for (Player player : players) {
            player.sendMessage("You have 1 hour to clear the dungeon. Goodluck.");
            player.teleport(spawn);
        }

        //delayed so players can teleport
        new BukkitRunnable() {
            @Override
            public void run() {
                purgeMobs();
            }
        }.runTaskLater(Rpgcore.instance, 20);

        //delayed so new mobs are spawned after old ones are purged.
        new BukkitRunnable() {
            @Override
            public void run() {
                phases.get(currentPhaseIndex).runCommands();
                completionCheckRunnable = initializeCompletionCheckRunnable();
                completionCheckRunnable.runTaskTimer(Rpgcore.instance, 20 * 5, 20 * 2);
            }
        }.runTaskLater(Rpgcore.instance, 20 * 3);
        dungeonTimeoutRunnable = initializeDungeonTimeoutRunnable();
        dungeonTimeoutRunnable.runTaskTimer(Rpgcore.instance, 0, 20);
    }

    private void purgeMobs() {
        if (party == null) {
            return;
        }
        for (Player player : PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
            for (Entity e : player.getNearbyEntities(100, 100, 100)) {
                if (arenaloader.isWithinDungeon(e.getLocation())) {
                    if (e instanceof LivingEntity && !(e instanceof Player)) {
                        if (e instanceof Tameable) {
                            Tameable tameable = (Tameable) e;
                            if (!tameable.isTamed()) {
                                e.remove();
                            }
                        } else {
                            e.remove();
                        }
                    }
                }
            }
        }
    }

    public BukkitRunnable initializeCompletionCheckRunnable() {
        Arena instance = this;
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.isCurrentPhaseComplete()) {
                    instance.runNextPhase();
                }
            }
        };
    }

    public void runNextPhase() {
        if (currentPhaseIndex < phases.size() - 1) {
            if (completionCheckRunnable != null) {
                completionCheckRunnable.cancel();
            }
            currentPhaseIndex++;
            purgeMobs();
            phases.get(currentPhaseIndex).runCommands();
            completionCheckRunnable = initializeCompletionCheckRunnable();
            completionCheckRunnable.runTaskTimer(Rpgcore.instance, 20 * 5, 20 * 2);
        } else if (endDungeonRunnable == null) {

            for (Player player : PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                player.sendMessage("Congrats! Dungeon cleared! You will be teleported out in 30 seconds.");
            }
            endDungeonRunnable = initializeEndDungeonRunnable();
            endDungeonRunnable.runTaskLater(Rpgcore.instance, 20 * 30);

        }
    }

    private BukkitRunnable initializeEndDungeonRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                    if(arenaloader.isWithinDungeon(player.getLocation())) {
                        player.sendMessage("Dungeon cleared! Congrats!");
                        player.teleport(shortTermStorages.spawn);
                    }
                    party.setArena(null);
                    reset();
                }
            }
        };
    }

    public BukkitRunnable initializeDungeonTimeoutRunnable() {
        return new BukkitRunnable() {
            int timeInSecondsRemaining = 60 * 60;
            @Override
            public void run() {

                timeInSecondsRemaining--;

                if(timeInSecondsRemaining % (60 * 5) == 0) {
                    for(Player player: PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                        player.sendMessage("You have " + timeInSecondsRemaining / 60 + " minutes remaining");
                    }
                }

                if(timeInSecondsRemaining == 60 * 2) {
                    for(Player player: PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                        player.sendMessage("You have 2 minutes remaining");
                    }
                }

                if(timeInSecondsRemaining == 60) {
                    for(Player player: PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                        player.sendMessage("You have 1 minute remaining");
                    }
                }

                if(timeInSecondsRemaining == 0) {
                    for(Player player: PartyAPI.getOnlineMembers(party.mcmmoParty.getName())) {
                        player.sendMessage("You did not clear the dungeon in time.");
                        if(Rpgcore.instance.isWithinDungeon(player.getLocation())) {
                            player.teleport(Rpgcore.instance.getSpawn());
                        }

                    }
                    party.setArena(null);
                    reset();
                }

            }
        };
    }

    public void handleMobSpawn(LivingEntity entity) {
        if (entity instanceof Player) {
            return;
        }
        if (entity instanceof Tameable) {
            Tameable tameable = (Tameable) entity;
            if (tameable.isTamed()) {
                return;
            }
        }

        if (!running) {
            entity.remove();
            return;
        }
        entity.setRemoveWhenFarAway(false);

        /*if(Rpgcore.hasMobNBT(entity, "fire")) {
            Rpgcore.instance.getLogger().info("firrrrreeee");
        }*/

        ArenaPhase currentPhase = phases.get(currentPhaseIndex);

        if (currentPhase instanceof KillAllPhase) {
            KillAllPhase killAllPhase = (KillAllPhase) currentPhase;
            killAllPhase.setTotalKillsForPhase(killAllPhase.getTotalKillsForPhase() + 1);
            killAllPhase.setKillsRemaining(killAllPhase.getKillsRemaining() + 1);
        }
    }

    public void handleMobDeath(LivingEntity entity, Player killer) {
        if (entity instanceof Player) {
            return;
        }
        if (entity instanceof Tameable) {
            Tameable tameable = (Tameable) entity;
            if (tameable.isTamed()) {
                return;
            }
        }

        ArenaPhase currentPhase = phases.get(currentPhaseIndex);

        handleCustomDrops(entity, killer);

        if (currentPhase instanceof KillAllPhase) {
            KillAllPhase killAllPhase = (KillAllPhase) currentPhase;
            killAllPhase.setKillsRemaining(killAllPhase.getKillsRemaining() - 1);
        }
    }

    public boolean isCurrentPhaseComplete() {
        return phases.get(currentPhaseIndex).isComplete();
    }

    public void teleportPlayerToSpawn(Player player) {
        player.teleport(spawn);
    }

    public DungeonParty getParty() {
        return party;
    }

    public void setParty(DungeonParty party) {
        this.party = party;
    }

    public String getRegionName() {
        return regionName;
    }

    public void addPhase(ArenaPhase phase) {
        phases.add(phase);
    }

    public static void handleCustomMobDrops(Entity e) {
        if (APIUsages.hasMobNBT(e, "fire")) {
            ItemStack item = new ItemStack(Material.FIRE_CHARGE);
            e.getWorld().dropItem(e.getLocation(), item);
            return;
        }
    }

    public void handleCustomDrops(Entity e, Player killer) {
        /*if (Rpgcore.hasMobNBT(e, "fire")) {
            ItemStack item = new ItemStack(Material.FIRE_CHARGE);
            e.getWorld().dropItem(e.getLocation(), item);
            return;
        }*/
        //Even though the mob was killed inside the arena the funtion is stored in this class.
        handleCustomMobDrops(e);
        PersistentDataContainer container = e.getPersistentDataContainer();
        String type = container.get(new NamespacedKey(Rpgcore.instance, "mobType"), PersistentDataType.STRING);
        ArrayList<ItemStack> items = new ArrayList<>();

        if(type == null) {
            return;
        }
        if(type.equalsIgnoreCase("ironZombie")) {
            items = getIronZombieDrops();
        } else if(type.equalsIgnoreCase("undeadKnight")){
            items = getUndeadKnightDrops();
        } else if(type.equalsIgnoreCase("illusioner")) {
            items = getIllusionerDrops();
        } else if(type.equalsIgnoreCase("axeman")) {
            items = getAxemanDrops();
        } else if(type.equalsIgnoreCase("ravager")) {
            items = getRavagerDrops();
        } else if(type.equalsIgnoreCase("zombie")) {
            items = getZombieDrops();
        } else if(type.equalsIgnoreCase("husk")) {
            items = getHuskDrops();
        } else if(type.equalsIgnoreCase("drowned")) {
            items = getDrownedDrops();
        } else if(type.equalsIgnoreCase("slime")) {
            items = getSlimeDrops();
        } else if(type.equalsIgnoreCase("king_slime")) {
            if(killer != null) {
                Rpgcore.getEconomy().depositPlayer(killer, 1000);
            }
            items = getKingSlimeDrops();
            rollForDungeonCrateKeyPartyDrop(killer, 15);
        } else if(type.equalsIgnoreCase("skeleton")) {
            items = getSkeletonDrops();
        } else if(type.equalsIgnoreCase("sword_skeleton")) {
            items = getSkeletonDrops();
        } else if(type.equalsIgnoreCase("spider")) {
            items = getSpiderDrops();
        } else if(type.equalsIgnoreCase("cave_spider")) {
            items = getSpiderDrops();
        } else if(type.equalsIgnoreCase("silverfish")) {
            items = getSilverfishDrops();
        }

        for(ItemStack item: items) {
            if(item.getType() != Material.AIR && item.getAmount() > 0) {
                e.getWorld().dropItem(e.getLocation(), item);
            }
        }
    }

    private ArrayList<ItemStack> getIronZombieDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        int rand = random.nextInt(10);
        if(rand < 7) {
            items.add(new ItemStack(Material.ROTTEN_FLESH));
        } else if(rand == 7) {
            items.add(new ItemStack(Material.IRON_NUGGET, 1));
        } else if(rand == 8) {
            items.add(new ItemStack(Material.IRON_INGOT, 1));
        } else if(rand == 9) {
            items.add(new ItemStack(Material.GOLD_NUGGET));
        }
        return items;
    }
    private ArrayList<ItemStack> getUndeadKnightDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        int rand = random.nextInt(30);
        if(rand < 27) {
            items.add(new ItemStack(Material.IRON_NUGGET));
        } else if(rand == 27) {
            items.add(new ItemStack(Material.DIAMOND));
        } else if(rand == 28) {
            items.add(new ItemStack(Material.IRON_CHESTPLATE));
        } else if(rand == 29) {
            items.add(new ItemStack(Material.IRON_SWORD));
        }
        return items;
    }
    private ArrayList<ItemStack> getIllusionerDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        int rand = random.nextInt(10);
        if(rand < 7) {
            items.add(new ItemStack(Material.EMERALD));
        } else {
            items.add(new ItemStack(Material.APPLE));
        }
        return items;
    }
    private ArrayList<ItemStack> getAxemanDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        int rand = random.nextInt(10);
        if(rand < 7) {
            items.add(new ItemStack(Material.EMERALD));
        }else if(rand == 8) {
            items.add(new ItemStack(Material.DIAMOND));
        } else if(rand == 9) {
            items.add(new ItemStack(Material.COOKED_BEEF));
        }
        return items;
    }
    private ArrayList<ItemStack> getRavagerDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        int rand = random.nextInt(50);
        if(rand < 25) {
            items.add(new ItemStack(Material.LEATHER));
        } else if(rand < 48) {
            items.add(new ItemStack(Material.DIAMOND_BOOTS));
        } else if(rand == 48) {
            items.add(new ItemStack(Material.TOTEM_OF_UNDYING));
        } else if(rand == 49) {
            items.add(new ItemStack(Material.EMERALD_BLOCK,  2));
        }
        return items;
    }
    private ArrayList<ItemStack> getZombieDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        int rand = random.nextInt(10);
        if(rand < 8) {
            items.add(new ItemStack(Material.ROTTEN_FLESH));
        } else if(rand == 8) {
            items.add(new ItemStack(Material.IRON_NUGGET, 1));
        } else if(rand == 9) {
            items.add(new ItemStack(Material.IRON_INGOT, 1));
        }
        return items;
    }
    private ArrayList<ItemStack> getHuskDrops() {
        return getZombieDrops();
    }
    private ArrayList<ItemStack> getDrownedDrops() {
        return getZombieDrops();
    }
    private ArrayList<ItemStack> getSlimeDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.SLIME_BALL, random.nextInt(3) + 3));
        return items;
    }
    private ArrayList<ItemStack> getKingSlimeDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        int rand = random.nextInt(10);
        if(rand < 7) {
            items.add(new ItemStack(Material.SLIME_BALL));
        }else if(rand == 8) {
            items.add(new ItemStack(Material.DIAMOND, 10));
        } else if(rand == 9) {
            items.add(new ItemStack(Material.EMERALD_BLOCK, 2));
        }
        return items;
    }
    private ArrayList<ItemStack> getSkeletonDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.BONE, random.nextInt(3) + 1));
        return items;
    }
    private ArrayList<ItemStack> getSpiderDrops() {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.STRING, random.nextInt(2)));
        items.add(new ItemStack(Material.SPIDER_EYE, random.nextInt(2)));
        return items;
    }
    private ArrayList<ItemStack> getSilverfishDrops() {
        return new ArrayList<ItemStack>();
    }

    public void rollForDungeonCrateKeyPartyDrop(Player player, int chance) {
        if(random.nextInt(chance) == 0) {
            ArrayList<Player> playerNames = new ArrayList<>();
            for(int i = 0; i < PartyAPI.getOnlineMembers(player).size(); i++) {
                Player pl = PartyAPI.getOnlineMembers(player).get(i);

                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                        "crate key " + pl.getName() + " Dungeon_Crate 1");

                playerNames.add(pl);
            }
            if(playerNames.size() == 1) {
                Bukkit.broadcastMessage(formatAnnouceDropNames(playerNames) + " has received a Dungeon Crate Key Drop!");
            } else {
                Bukkit.broadcastMessage(formatAnnouceDropNames(playerNames) + " have received a Dungeon Crate Key Drop!");
            }

        }
    }

    public String getStatus() {
        return party == null ? "free" : "in use";
    }

    public String formatAnnouceDropNames(ArrayList<Player> players) {
        String names = "";
        if(players.size() == 1) {
            return players.get(0).getName();
        }

        if(players.size() == 2) {
            return players.get(0).getName() + " and " + players.get(1).getName();
        }

        for(int i = 0; i < players.size(); i++) {
            if(i != players.size() - 1) {
                names += players.get(i).getName() + ", ";
            } else {
                names += "and " + players.get(i).getName();
            }
        }

        return names;
    }
}
