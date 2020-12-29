package me.prismskey.rpgcore.Events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import java.util.Random;

public class OnChunkPopulate implements Listener {

    private Random rand;

    public OnChunkPopulate() {
        rand = new Random();
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        /*Chunk chunk = event.getChunk();
        if(chunk.getWorld().getName().equalsIgnoreCase("world")) {
            if(rand.nextInt(13) == 1) {
                int x = chunk.getBlock(0, 1, 0).getX() + rand.nextInt(16);
                int y = rand.nextInt(4);
                int z = chunk.getBlock(0, 1, 0).getZ() + rand.nextInt(16);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + x + " " + y + " " + z + " run function generate:orichalcum");
            }
            if(rand.nextInt(10) == 1) {
                int x = chunk.getBlock(0, 1, 0).getX() + rand.nextInt(16);
                int y = rand.nextInt(4);
                int z = chunk.getBlock(0, 1, 0).getZ() + rand.nextInt(16);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + x + " " + y + " " + z + " run function generate:adamant");
            }

            if(rand.nextInt(2) == 1) {
                int x = chunk.getBlock(0, 1, 0).getX() + rand.nextInt(16);
                int y = rand.nextInt(40);
                int z = chunk.getBlock(0, 1, 0).getZ() + rand.nextInt(16);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute positioned " + x + " " + y + " " + z + " run function generate:mithril");
            }
        }
        if(chunk.getWorld().getName().equalsIgnoreCase("world_the_end")) {
            if(rand.nextInt(10) == 1) {
                int x = chunk.getBlock(0, 1, 0).getX() + rand.nextInt(16);
                int y = rand.nextInt(7);
                int z = chunk.getBlock(0, 1, 0).getZ() + rand.nextInt(16);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute in minecraft:the_end positioned " + x + " " + y + " " + z + " run function generate:dragonstone");
            }
        }
*/
    }
}
