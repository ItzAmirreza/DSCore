package me.prismskey.rpgcore.GeneralCommands;

import com.zachsthings.libcomponents.config.ConfigurationFile;
import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.ArenaLoader;
import me.prismskey.rpgcore.ArenaManager.DMob;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.Enums.SpecialMobs;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DSCoreCommands implements CommandExecutor {

    private final List<String> possibleArgs = Arrays.asList("createarena", "removearena", "addphase", "removephase", "addmob", "removemob", "setspawn", "setkeyname", "setkeydropchancefactor");
    private ArenaLoader arenaLoader = new ArenaLoader();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {



        if (sender instanceof Player) {
            //IN-GAME
            Player player = (Player) sender;

            int argscount = args.length;


            if (!(argscount == 0)) {
                String mainarg = args[0].toLowerCase();
                //dscore createarena name limit max
                if (possibleArgs.contains(mainarg)) {

                    if (mainarg.equalsIgnoreCase("createarena") && argscount == 7) {
                        //createArena
                        try {
                            createArena(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                    } else if (mainarg.equalsIgnoreCase("removearena") && argscount == 2) {
                        //removeArena
                        try {
                            removeArena(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                    } else if (mainarg.equalsIgnoreCase("addphase") && argscount == 6) {
                        ///dscore addphase (phase name) (arena name) (region name) (mob spawning range) (wavescount)

                        try {
                            createPhase(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }


                    } else if (mainarg.equalsIgnoreCase("removephase") && argscount == 3) {

                        try {
                            removePhase(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                    } else if (mainarg.equalsIgnoreCase("addmob") && argscount == 6) {
                        //dscore addmob (phase name) (arena name) (mob) (percentage) [optional level]
                        try {
                            addMob(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                    } else if (mainarg.equalsIgnoreCase("removemob") && argscount == 4) {
                        //dscore removemob (phase name) (arena name) (mob)
                        try {
                            removeMob(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                    } else if (mainarg.equalsIgnoreCase("setspawn") && argscount == 2) {

                        try {
                            setSpawnPoint(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }
                    } else if(mainarg.equalsIgnoreCase("setKeyName") && argscount == 3) {
                        try {
                            setKeyName(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                    } else if(mainarg.equalsIgnoreCase("setkeydropchancefactor") && argscount == 3) {
                        try {
                            setKeyDropChanceFactor(args, player);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                    }
                    else {

                        sendHelp(player);
                    }

                } else {
                    //not the right argument
                    sendHelp(player);

                }


            } else {
                //no arguemnt provided
                notEnoughArgs(player);

            }

        } else {
            //CONSOLE
            sender.sendMessage(Utils.color("&cNot yet for console ..."));


        }



        return false;
    }



    private void sendHelp(Player player) {

        //default help message

        player.sendMessage(Utils.color("&7&m------------------------- \n" +
                "&7- &c/dscore createarena (arena name) (min players) (max players) (maxTime) (key drop chance factor) (key name)\n" +
                "&7- &c/dscore removearena (arena name)\n" +
                "&7- &c/dscore addphase (phase name) (arena name) (region name) (mob spawning range) (waves count) | Note: Where you are staying when you are creating this sets the center of phase \n" +
                "&7- &c/dscore removephase (phase name) (arena name) \n" +
                "&7- &c/dscore addmob (phase name) (arena name) (mob) (percentage) (level) | level for vanilla mobs is 1\n" +
                "&7- &c/dscore removemob (phase name) (arena name) (mob) \n" +
                "&7- &c/dscore setspawn (arena name) \n" +
                "&7- &c/dscore arenalist \n" +
                "&7- &c/dscore phaselist (arena name) \n" +
                "&7- &c/dscore setkeyname (arena name) (key name) \n" +
                "&7- &c/dscore setkeydropchancefactor (arena name) (factor value) \n" +
                "&7&m-------------------------"));

    }


    private void createArena(String[] args, Player player) throws IOException, InvalidConfigurationException {
        String arenaName = args[1].toLowerCase();
        if (!(shortTermStorages.arenaHashMap.containsKey(arenaName))) {

            int min = Integer.parseInt(args[2]);
            int max = Integer.parseInt(args[3]);
            int maxtime = Integer.parseInt(args[4]);
            double keyDropChanceFactor = Double.parseDouble(args[5]);
            String keyName = args[6];

            Arena newArena = new Arena(arenaName, min, max, maxtime, keyDropChanceFactor, keyName);

            shortTermStorages.arenas.add(newArena);
            shortTermStorages.arenaHashMap.put(newArena.name, newArena);


            //Adding to arenas.yml section
            File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
            FileConfiguration arenasconfig = new YamlConfiguration();
            arenasconfig.load(arenasfile);
            arenasconfig.createSection("arenas." + arenaName);
            arenasconfig.set("arenas." + arenaName + ".min", min);
            arenasconfig.set("arenas." + arenaName + ".max", max);
            arenasconfig.set("arenas." + arenaName + ".maxtime", maxtime);
            arenasconfig.set("arenas." + arenaName + ".keydropchancefactor", keyDropChanceFactor);
            arenasconfig.set("arenas." + arenaName + ".prizekeyname", keyName);
            arenasconfig.set("arenas." + arenaName + ".rewards", Arrays.asList("eco give %player% 100:100"));
            Rpgcore.getInstance().saveResource("arenas.yml", true);
            setTheConfigs(arenasfile, arenasconfig);

            player.sendMessage(Utils.color("&aArena" + " &e" + arenaName + " &aSuccessfully created! Now you can set it up."));


        } else {

            alreadyExists(player);

        }

    }



    private void removeArena(String[] args, Player player) throws IOException, InvalidConfigurationException {
        String arenaName = args[1].toLowerCase();
        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
            shortTermStorages.arenas.remove(thatArena);
            shortTermStorages.arenaHashMap.remove(arenaName);
            File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
            FileConfiguration arenasconfig = new YamlConfiguration();
            arenasconfig.load(arenasfile);
            arenasconfig.set("arenas." + arenaName, null);
            setTheConfigs(arenasfile, arenasconfig);

            player.sendMessage(Utils.color("&aArena &e" + arenaName + " &asuccessfully removed."));

        } else {
            doesntExist(player);
        }

    }

    private void setKeyDropChanceFactor(String[] args, Player player) throws IOException, InvalidConfigurationException {
        String arenaName = args[1].toLowerCase();
        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {

            shortTermStorages.arenaHashMap.get(arenaName).setKeyDropChanceFactor(Double.parseDouble(args[2]));
            shortTermStorages.arenaHashMap.get(arenaName).checkIfArenaIsReady();

            File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
            FileConfiguration arenasconfig = new YamlConfiguration();
            arenasconfig.load(arenasfile);
            arenasconfig.set("arenas." + arenaName + ".keydropchancefactor", Double.parseDouble(args[2]));
            setTheConfigs(arenasfile, arenasconfig);


            player.sendMessage(Utils.color("&aYou have set the key drop chance factor to " + args[2] + "."));

        } else {
            doesntExist(player);
        }
    }

    private void setKeyName(String[] args, Player player) throws IOException, InvalidConfigurationException {
        String arenaName = args[1].toLowerCase();
        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {

            shortTermStorages.arenaHashMap.get(arenaName).setPrizeKeyName(args[2]);
            shortTermStorages.arenaHashMap.get(arenaName).checkIfArenaIsReady();

            File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
            FileConfiguration arenasconfig = new YamlConfiguration();
            arenasconfig.load(arenasfile);
            arenasconfig.set("arenas." + arenaName + ".prizekeyname", args[2]);
            setTheConfigs(arenasfile, arenasconfig);


            player.sendMessage(Utils.color("&aYou have set the prize key name to " + args[2] + "."));

        } else {
            doesntExist(player);
        }
    }

    private void setSpawnPoint(String[] args, Player player) throws IOException, InvalidConfigurationException {

        String arenaName = args[1].toLowerCase();
        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {

            shortTermStorages.arenaHashMap.get(arenaName).setSpawnLocation(player.getLocation());
            shortTermStorages.arenaHashMap.get(arenaName).checkIfArenaIsReady();

            File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
            FileConfiguration arenasconfig = new YamlConfiguration();
            arenasconfig.load(arenasfile);
            arenasconfig.set("arenas." + arenaName + ".spawnlocation", Utils.convertLocToString(player.getLocation()));
            setTheConfigs(arenasfile, arenasconfig);


            player.sendMessage(Utils.color("&aYou have set the spawn point."));

        } else {
            doesntExist(player);
        }

    }


    private void createPhase(String[] args, Player player) throws IOException, InvalidConfigurationException {
        ///dscore addphase (phase name) (arena name) (region name) (mob spawning range) (wavescount)
        String phaseName = args[1].toLowerCase();
        String arenaName = args[2].toLowerCase();
        String regionName = args[3].toLowerCase();
        int mobRange = Integer.parseInt(args[4]);
        int wavescount = Integer.parseInt(args[5]);
        Location center = player.getLocation();

        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
            if (!checkIfPhaseExists(thatArena.phases, phaseName)) {

                Phase newPhase = new Phase(phaseName, arenaName, regionName, mobRange, wavescount, center);
                newPhase.wavescount = wavescount;
                newPhase.center = center;
                thatArena.phases.put(phaseName, newPhase);
                thatArena.checkIfArenaIsReady();
                shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
                FileConfiguration arenasconfig = new YamlConfiguration();
                arenasconfig.load(arenasfile);
                if (arenasconfig.isConfigurationSection("arenas." + arenaName + ".phases")) {

                    arenasconfig.createSection("arenas." + arenaName + ".phases." + phaseName);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".region", regionName);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".spawnrange", mobRange);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".wavescount", wavescount);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".center", Utils.convertLocToString(center));
                    setTheConfigs(arenasfile, arenasconfig);

                } else {
                    arenasconfig.createSection("arenas." + arenaName + ".phases");
                    arenasconfig.createSection("arenas." + arenaName + ".phases." + phaseName);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".region", regionName);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".spawnrange", mobRange);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".wavescount", wavescount);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".center", Utils.convertLocToString(center));
                    setTheConfigs(arenasfile, arenasconfig);
                }

                player.sendMessage(Utils.color("&aPhase &e" + phaseName + " &asuccessfully created."));

            } else {

                player.sendMessage(Utils.color("&cThis phase already exists in this arena. Choose another name..."));

            }

        } else {

            doesntExist(player);
        }


    }


    private void removePhase(String[] args, Player player) throws IOException, InvalidConfigurationException {
        //dscore removephase (phase name) (arena name)
        String phaseName = args[1].toLowerCase();
        String arenaName = args[2].toLowerCase();

        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
            if (thatArena.phases.containsKey(phaseName)) {

                thatArena.phases.remove(phaseName);
                thatArena.checkIfArenaIsReady();
                shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
                FileConfiguration arenasconfig = new YamlConfiguration();
                arenasconfig.load(arenasfile);
                arenasconfig.set("arenas." + arenaName + ".phases." + phaseName, null);
                setTheConfigs(arenasfile, arenasconfig);

                player.sendMessage(Utils.color("&aThis phase successfully removed."));


            } else {

                player.sendMessage(Utils.color("&cThis phase doesn't exist in this arena."));

            }
        } else {

            player.sendMessage(Utils.color("&cThis arena doesn't exist."));

        }

    }



    public void addMob(String[] args, Player player) throws IOException, InvalidConfigurationException {
        //dscore addmob (phase name) (arena name) (mob) (percentage) [optional level]
        String phaseName = args[1].toLowerCase();
        String arenaName = args[2].toLowerCase();
        String mob = args[3];
        int percentage = Integer.parseInt(args[4]);
        int level = 1;
        boolean special;
        try {
            EntityType type = EntityType.valueOf(mob);
            level = Integer.parseInt(args[5]);
            special = false;
        } catch (IllegalArgumentException ex) {
            level = Integer.parseInt(args[5]);
            special = true;
        }

        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);

            if (thatArena.phases.containsKey(phaseName)) {


                Phase thatPhase = thatArena.phases.get(phaseName);
                File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
                FileConfiguration arenasconfig = new YamlConfiguration();

                if (special) {

                    //add the special mob
                    try {
                        SpecialMobs specialMob = SpecialMobs.valueOf(mob.toUpperCase());
                        thatPhase.addMob(new DMob(mob.toUpperCase(), percentage, true, level));
                        thatArena.phases.replace(thatPhase.name, thatPhase);
                        shortTermStorages.arenaHashMap.replace(arenaName, thatArena);

                        arenasconfig.load(arenasfile);
                        List<String> outGoingMobList = Arrays.asList(mob.toUpperCase() + ":special:" + percentage + ":" + level);

                        if (arenasconfig.isList("arenas." + arenaName + ".phases." + phaseName + ".mobs")) {
                            //when there is already a list of mobs
                            List<String> mobslist = arenasconfig.getStringList("arenas." + arenaName + ".phases." + phaseName + ".mobs");
                            mobslist.add(mob.toUpperCase() + ":special:" + percentage + ":" + level); //mob | type | percentage | level
                            arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".mobs", mobslist);
                        } else {
                            //when there is no mobs
                            arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".mobs", outGoingMobList);
                        }

                        player.sendMessage(Utils.color("&aMob &e" + mob + " &aadded to &d" + phaseName + "&a."));


                    } catch (IllegalArgumentException ex) {
                        player.sendMessage(Utils.color("&cThis special mob doesn't exist."));
                    }

                } else {
                    //not special
                    thatPhase.addMob(new DMob(mob, percentage, false, level));
                    thatArena.phases.replace(thatPhase.name, thatPhase);
                    shortTermStorages.arenaHashMap.replace(arenaName, thatArena);

                    arenasconfig.load(arenasfile);
                    List<String> outGoingMobList = Arrays.asList(mob.toUpperCase() + ":vanilla:" + percentage + ":" + level);

                    if (arenasconfig.isList("arenas." + arenaName + ".phases." + phaseName + ".mobs")) {
                        //when there is already a list of mobs
                        List<String> mobslist = arenasconfig.getStringList("arenas." + arenaName + ".phases." + phaseName + ".mobs");
                        mobslist.add(mob.toUpperCase() + ":vanilla:" + percentage + ":" + level); //mob | type | percentage | level
                        arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".mobs", mobslist);
                    } else {
                        //when there is no mobs
                        arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".mobs", outGoingMobList);
                    }

                    player.sendMessage(Utils.color("&aMob &e" + mob + " &aadded to &d" + phaseName + "&a."));
                }



                setTheConfigs(arenasfile, arenasconfig);


            } else {
                player.sendMessage(Utils.color("&cThis phase doesn't exist in this arena."));
            }
        } else {
            player.sendMessage(Utils.color("&cThis arena doesn't exist."));
        }

    }


    private void removeMob(String[] args, Player player) throws IOException, InvalidConfigurationException {
        //dscore removemob (phase name) (arena name) (mob)
        String phaseName = args[1].toLowerCase();
        String arenaName = args[2].toLowerCase();
        String mob = args[3];

        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {

            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
            if (thatArena.phases.containsKey(phaseName)) {

                Phase thatPhase = thatArena.phases.get(phaseName);
                boolean found = false;
                DMob thatmob = null;
                for (DMob dmob : thatPhase.mobs) {
                    if (dmob.mob.equalsIgnoreCase(mob)) {
                        found = true;
                        thatmob = dmob;
                        break;
                    }
                }
                if (found) {

                    thatPhase.removeMob(thatmob);
                    thatArena.phases.replace(thatPhase.name, thatPhase);
                    File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
                    FileConfiguration arenasconfig = new YamlConfiguration();
                    arenasconfig.load(arenasfile);
                    List<String> mobs = arenasconfig.getStringList("arenas." + arenaName + ".phases." + phaseName + ".mobs");
                    mobs.remove(mob.toUpperCase() + ":" + thatmob.getSpecialitiyString() + ":" + thatmob.percentage + ":" + thatmob.level);
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".mobs", mobs);
                    setTheConfigs(arenasfile, arenasconfig);

                    player.sendMessage(Utils.color("&aThe mob &e" + mob + " &aadded to &d" + phaseName + " &aphase."));



                } else {
                    player.sendMessage(Utils.color("&cThis mob is not in this phase."));
                }
            } else {
                player.sendMessage(Utils.color("&cThis phase doesn't exist in this arena."));
            }
        } else {
            player.sendMessage(Utils.color("&cThis arena doesn't exist."));
        }

    }



    private void notEnoughArgs(Player player) {
        player.sendMessage(Utils.color("&cNot enough argument provided."));
    }

    private void alreadyExists(Player player) {

        player.sendMessage(Utils.color("&cThis object already exists."));

    }
    private void doesntExist(Player player) {
        player.sendMessage(Utils.color("&cThis object doesn't exist."));
    }

    private void setTheConfigs(File file, FileConfiguration fileConfiguration) throws IOException, InvalidConfigurationException {
        fileConfiguration.save(file);
        shortTermStorages.arenasFile = file;
        shortTermStorages.arenasConfiguration.load(file);
    }

    private boolean checkIfPhaseExists(HashMap<String, Phase> phases, String phasename) {
        boolean result = false;

        for (String phase : phases.keySet()) {

            if (phase.toLowerCase().equalsIgnoreCase(phasename)) {
                result = true;
                break;
            }

        }
        return result;

    }




}
