package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.PrizeObject;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Random;

public class onArenaFinishListener implements Listener {

    Random rand = new Random();
    private final String GIVE_KEY_COMMAND = "crates givekey %player% ";

    @EventHandler
    public void onArenaWon(onArenaFinish event) {

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        List<PrizeObject> prizeObjectList = event.getArena().prizeCommands;

        for (Player player : event.getPlayerList()) {

            //the crate key
            double keyRoll = Math.random();
            double totalKilledMobs = event.getArena().totalKilledMobs;
            double totalMobs = event.getArena().totalMobs;
            double keyDropChanceFactor = event.getArena().keyDropChanceFactor;
            double result = totalKilledMobs/totalMobs * keyDropChanceFactor;
            if(result >= keyRoll) {
                Bukkit.dispatchCommand(console, GIVE_KEY_COMMAND.replace("%player%", player.getName()) + event.getArena().prizeKeyName);
            }


            double winCash = event.getArena().winCash;
            result = winCash * (totalKilledMobs/totalMobs);
            Rpgcore.getEconomy().depositPlayer(player, result);
            player.sendMessage(ChatColor.GREEN + "You were awarded $" + result + " for clearing the dungeon.");
            //all other prizes such as money or other items
            /*for (PrizeObject prize : prizeObjectList) {

                int  n = rand.nextInt(100) + 1;
                if (n<=prize.percentage) {

                    Bukkit.dispatchCommand(console, prize.command.replace("%player%", player.getName()));

                }

            }*/

        }


        String command = "/command";
        Bukkit.dispatchCommand(console, command);

    }


}
