package me.prismskey.rpgcore.GeneralCommands;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.ArenaManager.ArenaLoader;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
                    //create arena
                    if (mainarg.equalsIgnoreCase("createarena") && argscount == 5) {
                        //createArena
                        int min = Integer.parseInt(args[1]);
                        int max = Integer.parseInt(args[2]);
                        int maxtime = Integer.parseInt(args[3]);



                    } else if (mainarg.equalsIgnoreCase("removearena") && argscount == 2) {


                    } else if (mainarg.equalsIgnoreCase("addphase") && argscount == 5) {


                    } else if (mainarg.equalsIgnoreCase("removephase") && argscount == 3) {

                    } else if (mainarg.equalsIgnoreCase("addmob") && argscount == 4) {

                    } else if (mainarg.equalsIgnoreCase("removemob") && argscount == 4) {

                    } else if (mainarg.equalsIgnoreCase("setspawn") && argscount == 2) {

                    }

                } else {
                    //not the right argument
                    sendHelp(player);

                }


            } else {
                //no arguemnt provided
                sendHelp(player);

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
                "&7&m-------------------------"));

    }


    private void notEnoughArgs(Player player) {
        player.sendMessage(Utils.color("&cNot enough argument provided."));
    }

}
