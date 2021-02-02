package me.prismskey.rpgcore.Tasks;

import me.prismskey.rpgcore.DataManager.ConfigLoader;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.Utils.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class LostvayneParticleTask extends BukkitRunnable {
    private ConfigLoader loader = new ConfigLoader();
    private Random rand = new Random();

    @Override
    public void run() {
        for(Player player: Bukkit.getOnlinePlayers()) {
            RPGPlayerData data = loader.getDataByUUID(player.getUniqueId());

            ItemStack mainhand = player.getInventory().getItemInMainHand();
            NBTItem nbti = new NBTItem(mainhand);
            if(!nbti.hasKey("lostvayne")) {
                data.resetLostvayneCharge();
            }

            for(int i = 0; i < data.getLostvayneCharge(); i++) {
                int randx = player.getLocation().getBlockX() + rand.nextInt(7) - 3;
                int randy = player.getLocation().getBlockY() + rand.nextInt(7);
                int randz = player.getLocation().getBlockZ() + rand.nextInt(7) - 3;
                Particle.DustOptions blackDust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 3);
                Particle.DustOptions purpleDust = new Particle.DustOptions(Color.fromRGB(69, 0, 196), 3);
                player.getWorld().spawnParticle(Particle.REDSTONE, randx, randy, randz, 0, 0, 0, 0, blackDust);
                player.getWorld().spawnParticle(Particle.REDSTONE, randx, randy, randz, 0, 0, 0, 0, purpleDust);
            }
        }
    }
}
