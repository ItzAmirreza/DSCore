package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.PrizeObject;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Random;

public class onArenaFinishListener implements Listener {

    Random rand = new Random();


    @EventHandler
    public void onArenaEnd(onArenaFinish event) {

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        List<PrizeObject> prizeObjectList = event.getArena().prizeCommands;

        for (Player player : event.getPlayerList()) {


            for (PrizeObject prize : prizeObjectList) {

                int  n = rand.nextInt(100) + 1;
                if (n<=prize.percentage) {

                    Bukkit.dispatchCommand(console, prize.command.replace("%player%", player.getName()));

                }


            }


        }


        String command = "/command";
        Bukkit.dispatchCommand(console, command);

    }


}
