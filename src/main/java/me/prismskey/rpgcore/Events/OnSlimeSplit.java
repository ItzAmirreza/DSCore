package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.ArenaManager.arenaLoader;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Material;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class OnSlimeSplit implements Listener {
    private Random rand;

    public OnSlimeSplit() {
        rand = new Random();
    }

    private arenaLoader arenaloader = new arenaLoader();

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        Slime slime = event.getEntity();
        if(!arenaloader.isWithinDungeon(slime.getLocation())) {

            ItemStack slimeballs;
            if(slime instanceof MagmaCube) {
                slimeballs = new ItemStack(Material.MAGMA_CREAM, rand.nextInt(2) + 1);
            } else {
                slimeballs = new ItemStack(Material.SLIME_BALL, rand.nextInt(3) + 3);
            }

            slime.getLocation().getWorld().dropItem(slime.getLocation(), slimeballs);
        }
        event.setCancelled(true);

    }
}
