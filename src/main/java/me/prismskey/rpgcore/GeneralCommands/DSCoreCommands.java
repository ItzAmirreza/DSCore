package me.prismskey.rpgcore.GeneralCommands;

import com.zachsthings.libcomponents.config.ConfigurationFile;
import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.ArenaLoader;
import me.prismskey.rpgcore.ArenaManager.Phase;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
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

    private final List<String> possibleArgs = Arrays.asList("createarena", "removearena", "addphase", "removephase", "addmob", "removemob", "setspawn");
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

                    if (mainarg.equalsIgnoreCase("createarena") && argscount == 5) {
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

                    } else if (mainarg.equalsIgnoreCase("addphase") && argscount == 5) {
                        ///dscore addphase (phase name) (arena name) (region name) (mob spawning range)

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

                    } else if (mainarg.equalsIgnoreCase("addmob") && argscount == 4) {

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

                    } else {

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

        player.sendMessage(Utils.color("&7&m------------------------- " +
                "&7- &c/dscore createarena (arena name) (min players) (max players) (maxTime)" +
                "&7- &c/dscore removearena (arena name)" +
                "&7- &c/dscore addphase (phase name) (arena name) (region name) (mob spawning range)" +
                "&7- &c/dscore removephase (phase name) (arena name)" +
                "&7- &c/dscore addmob (phase name) (arena name) (mob)" +
                "&7- &c/dscore removemob (phase name) (arena name) (mob)" +
                "&7- &c/dscore setspawn (arena name)" +
                "&7- &c/dscore arenalist" +
                "&7- &c/dscore phaselist (arena name)" +
                "&7&m-------------------------"));

    }


    private void createArena(String[] args, Player player) throws IOException, InvalidConfigurationException {
        String arenaName = args[1].toLowerCase();
        if (!(shortTermStorages.arenaHashMap.containsKey(arenaName))) {

            int min = Integer.parseInt(args[2]);
            int max = Integer.parseInt(args[3]);
            int maxtime = Integer.parseInt(args[4]);

            Arena newArena = new Arena(arenaName, min, max, maxtime);

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

    private void setSpawnPoint(String[] args, Player player) throws IOException, InvalidConfigurationException {

        String arenaName = args[1].toLowerCase();
        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {

            shortTermStorages.arenaHashMap.get(arenaName).setSpawnLocation(player.getLocation());


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
        ///dscore addphase (phase name) (arena name) (region name) (mob spawning range)
        String phaseName = args[1].toLowerCase();
        String arenaName = args[2].toLowerCase();
        String regionName = args[3].toLowerCase();
        int mobRange = Integer.parseInt(args[4]);

        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
            if (!checkIfPhaseExists(thatArena.phases, phaseName)) {

                Phase newPhase = new Phase(phaseName, arenaName, regionName, mobRange);
                thatArena.phases.put(phaseName, newPhase);
                shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
                FileConfiguration arenasconfig = new YamlConfiguration();
                arenasconfig.load(arenasfile);
                arenasconfig.createSection("arenas." + arenaName + ".phases");
                arenasconfig.createSection("arenas." + arenaName + ".phases." + phaseName);
                arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".region", regionName);
                arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".spawnrange", mobRange);
                setTheConfigs(arenasfile, arenasconfig);

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
        //dscore addmob (phase name) (arena name) (mob)
        String phaseName = args[1].toLowerCase();
        String arenaName = args[2].toLowerCase();
        String mob = args[3];

        if (shortTermStorages.arenaHashMap.containsKey(arenaName)) {
            Arena thatArena = shortTermStorages.arenaHashMap.get(arenaName);
            if (thatArena.phases.containsKey(phaseName)) {

                Phase thatPhase = thatArena.phases.get(phaseName);
                thatPhase.addMob(EntityType.valueOf(mob));
                thatArena.phases.replace(thatPhase.name, thatPhase);
                shortTermStorages.arenaHashMap.replace(arenaName, thatArena);
                File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
                FileConfiguration arenasconfig = new YamlConfiguration();
                arenasconfig.load(arenasfile);
                List<String> outGoingMobList = Arrays.asList(mob.toUpperCase() + ":vanilla");

                if (arenasconfig.isList("arenas." + arenaName + ".phases." + phaseName + ".mobs")) {
                    //when there is already a list of mobs
                    List<String> mobslist = arenasconfig.getStringList("arenas." + arenaName + ".phases." + phaseName + ".mobs");
                    mobslist.add(mob.toUpperCase() + ":vanilla");
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".mobs", mobslist);
                } else {
                    //when there is no mobs
                    arenasconfig.set("arenas." + arenaName + ".phases." + phaseName + ".mobs", outGoingMobList);
                }

                setTheConfigs(arenasfile, arenasconfig);

                player.sendMessage(Utils.color("&aMob &e" + mob + " &aadded to &d" + phaseName + "&a."));

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
                if (thatPhase.mobs.contains(EntityType.valueOf(mob.toUpperCase()))) {

                    thatPhase.removeMob(EntityType.valueOf(mob.toUpperCase()));
                    thatArena.phases.replace(thatPhase.name, thatPhase);
                    shortTermStorages.arenaHashMap.replace(thatArena.name, thatArena);
                    File arenasfile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
                    FileConfiguration arenasconfig = new YamlConfiguration();
                    arenasconfig.load(arenasfile);
                    List<String> mobs = arenasconfig.getStringList("arenas." + arenaName + ".phases." + phaseName + ".mobs");
                    mobs.remove(mob.toUpperCase() + ":vanilla");
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

    private boolean checkIfVanillaMob(String mob) {
        return true;
    }


}
