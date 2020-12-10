package me.prismskey.rpgcore.DataManager;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ConfigLoader {

    public ConfigLoader() {
        //this.loadArenaConfig();
        ///this.loadPvpStatesConfig();
        //this.parseDefaultConfig();
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


    //I guess it doesn't need to be executed in OnEnable right? so i don't initialize it in constructor
    public void savePvpStatesConfig() {
        try {
            shortTermStorages.pvpStatesConfiguration.save(shortTermStorages.pvpStatesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void updatePlayerData(RPGPlayerData data) {
        for(int i = 0; i < shortTermStorages.playerData.size(); i++) {
            RPGPlayerData d = shortTermStorages.playerData.get(i);
            if(d.getPlayerUUID().equals(data.getPlayerUUID())) {
                shortTermStorages.playerData.set(i, data);
            }
        }
    }

    public RPGPlayerData getDataByUUID(UUID uuid) {
        for(RPGPlayerData data: shortTermStorages.playerData) {
            if(data.getPlayerUUID().equals(uuid)) {
                return data;
            }
        }
        return null;
    }







}
