package me.prismskey.rpgcore.DataManager;

import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ItemStackConfigLoader {
    public static ArrayList<ItemStack> items = new ArrayList<>();

    public static void load() {
        File file = new File(Rpgcore.getInstance().getDataFolder().getPath() + File.separator + "customItems.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        items = new ArrayList<>();
        Rpgcore.getInstance().getLogger().info(ChatColor.YELLOW + "DEBUG DEBUG:");
        if (yml.getConfigurationSection("items") == null) {
            return;
        }
        Rpgcore.getInstance().getLogger().info(ChatColor.GREEN + "DEBUG DEBUG:");
        for (String key : yml.getConfigurationSection("items").getKeys(false)) {
            Rpgcore.getInstance().getLogger().info(ChatColor.GREEN + key);
            ItemStack item = yml.getItemStack("items." + key);
            items.add(item);
        }


    }

    public static void save(String name, ItemStack item) {
        File file = new File(Rpgcore.getInstance().getDataFolder().getPath() + File.separator + "customItems.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.set("items." + name, item);
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
