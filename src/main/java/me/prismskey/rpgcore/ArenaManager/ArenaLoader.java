package me.prismskey.rpgcore.ArenaManager;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ArenaLoader {


    public ArenaLoader() {

    }

    private Rpgcore rpgcore = Rpgcore.getInstance();

    public void loadArenas() {
        ConfigurationSection config = shortTermStorages.arenasConfiguration.getConfigurationSection("arenas");
        if (!(config == null)) {
            config.getKeys(false).forEach(key -> {
                String arenaname = key;
                int min = config.getInt(key + ".min");
                int max = config.getInt(key + ".max");
                double keyDropChance = config.getDouble(key + ".key_drop_chance_factor");
                String prizeKeyName = config.getString(key + ".prize_key_name");
                int maxTime = config.getInt(key + ".maxtime"); //minutes
                Arena newArena = new Arena(arenaname.toLowerCase(), min, max, maxTime, keyDropChance, prizeKeyName);
                String spawnLocation = config.getString(key + ".spawnlocation", "null");
                if (!spawnLocation.equalsIgnoreCase("null")) {
                    newArena.setSpawnLocation(spawnLocation);
                }

                //getting rewards
                List<String> rewardStringList = config.getStringList(key + ".rewards");
                for (String strReward : rewardStringList) {
                    String command = strReward.split(":")[0];
                    int percentage = Integer.parseInt(strReward.split(":")[1]);
                    PrizeObject thaPrize = new PrizeObject(command, percentage);
                    newArena.prizeCommands.add(thaPrize);
                }

                LinkedHashMap<String, Phase> phases = new LinkedHashMap<>();
                boolean ifConfigurationS = config.isConfigurationSection(key + ".phases");
                AtomicInteger totalMobs = new AtomicInteger();
                if (ifConfigurationS) {
                    ConfigurationSection phaseSection = config.getConfigurationSection(key + ".phases");
                    phaseSection.getKeys(false).forEach(phase -> {
                        String phaseName = phase;

                        String regionName = config.getString(key + ".phases." + phase + ".region");
                        int mobSpawnRange = config.getInt(key + ".phases." + phase + ".spawnrange");
                        int wavescount = config.getInt(key + ".phases." + phase + ".wavescount");
                        Location center = Utils.convertStringToLoc(config.getString(key + ".phases." + phase + ".center"));
                        Phase newPhase = new Phase(phaseName, arenaname, regionName, mobSpawnRange, wavescount, center);
                        boolean mobsList = config.isList(key + ".phases." + phase + ".mobs");
                        if (mobsList) {
                            List<String> mobs = config.getStringList(key + ".phases." + phase + ".mobs");
                            for (String thatmob : mobs) {
                                totalMobs.getAndIncrement();
                                String[] devide = thatmob.split(":"); //mob | type | percentage | level
                                String mob = devide[0];
                                String type = devide[1];
                                System.out.println(mob);
                                int percentage = Integer.parseInt(devide[2]);
                                int level = Integer.parseInt(devide[3]);
                                boolean isSpecial = false;
                                if (type.equalsIgnoreCase("special")) {
                                    isSpecial = true;
                                }

                                if (!isSpecial) {
                                    //adding vanilla mobs to arena
                                    newPhase.mobs.add(new DMob(mob, percentage, false,level));


                                } else {

                                    //adding special mobs to arena
                                    newPhase.mobs.add(new DMob(mob, percentage, true, level));

                                }

                            }

                        }

                        phases.put(newPhase.name, newPhase);

                    });

                }
                //HashMap<String, Phase> finalMap = new HashMap<>();
                for (String str : phases.keySet()) {
                    Rpgcore.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&b" + str));
                }

                newArena.totalMobs = totalMobs.get();
                Rpgcore.getInstance().getLogger().info("Total Mobs: " + newArena.totalMobs);
                newArena.setPhasesMap(phases);
                newArena.checkIfArenaIsReady();
                shortTermStorages.arenas.add(newArena);
                shortTermStorages.arenaHashMap.put(newArena.name, newArena);

            });
        }

    }



}
