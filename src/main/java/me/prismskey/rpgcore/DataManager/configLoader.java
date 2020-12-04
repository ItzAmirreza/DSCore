package me.prismskey.rpgcore.DataManager;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class configLoader {

    public configLoader() {
        this.loadArenaConfig();
        this.loadPvpStatesConfig();
        this.parseDefaultConfig();
    }


    //Arena Configuration Loader / saves the configurations in shortTermStorages

    public void loadArenaConfig() {
        shortTermStorages.arenasFile = new File(Rpgcore.getInstance().getDataFolder(), "arenas.yml");
        if (!shortTermStorages.arenasFile.exists()) {
            shortTermStorages.arenasFile.getParentFile().mkdirs();
            Rpgcore.getInstance().saveResource("arenas.yml", false);
        }

        shortTermStorages.arenasConfiguration = new YamlConfiguration();
        try {
            shortTermStorages.arenasConfiguration.load(shortTermStorages.arenasFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    //PVP State loader / saves the configurations in shortTertStorages

    public void loadPvpStatesConfig() {
        shortTermStorages.pvpStatesFile = new File(Rpgcore.getInstance().getDataFolder(), "pvpStates.yml");
        if(!shortTermStorages.pvpStatesFile.exists()) {
            shortTermStorages.pvpStatesFile.getParentFile().mkdirs();
            Rpgcore.getInstance().saveResource("pvpStates.yml", false);
        }

        shortTermStorages.pvpStatesConfiguration = new YamlConfiguration();
        try {
            shortTermStorages.pvpStatesConfiguration.load(shortTermStorages.pvpStatesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    //Sets the spawn point / saves in STS

    public void parseDefaultConfig() {
        ConfigurationSection spawnSection = Rpgcore.getInstance().getConfig().getConfigurationSection("spawn");
        int x = spawnSection.getInt("x");
        int y = spawnSection.getInt("y");
        int z = spawnSection.getInt("z");
        String world = spawnSection.getString("world");
        shortTermStorages.spawn = new Location(Rpgcore.getInstance().getServer().getWorld(world), x, y, z);
    }





}
