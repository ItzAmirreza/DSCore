package me.prismskey.rpgcore.GeneralCommands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.MagmaCube;

public class FillBlocksCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 8) {
            sender.sendMessage("Incorrect usage");
            return true;
        }
        int x1 = Integer.parseInt(args[0]);
        int y1 = Integer.parseInt(args[1]);
        int z1 = Integer.parseInt(args[2]);
        int x2 = Integer.parseInt(args[3]);
        int y2 = Integer.parseInt(args[4]);
        int z2 = Integer.parseInt(args[5]);
        Material material = Material.valueOf(args[6]);
        World world = Bukkit.getWorld(args[7]);

        for(int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for(int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                for(int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
        return true;
    }
}
