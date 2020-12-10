package me.prismskey.rpgcore.ArenaManager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaLoader {


    public ArenaLoader() {

    }

    private Rpgcore rpgcore = Rpgcore.getInstance();

    public void loadArenas() {
        ConfigurationSection config = shortTermStorages.arenasConfiguration.getConfigurationSection("Arenas");
        config.getKeys(false).forEach(key -> {
            String arenaname = key;
            int min = config.getInt(key + ".min");
            int max = config.getInt(key + ".max");
            int maxTime = config.getInt(key + ".maxtime"); //minutes
            Arena newArena = new Arena(arenaname.toLowerCase(), min, max, maxTime);
            String spawnLocation = config.getString(key + ".spawnlocation", "null");
            if (!spawnLocation.equalsIgnoreCase("null")) {
                newArena.setSpawnLocation(spawnLocation);
            }
            HashMap<String, Phase> phases = new HashMap<>();
            boolean ifConfigurationS = config.isConfigurationSection(key + ".phases");
            if (ifConfigurationS) {
                ConfigurationSection phaseSection = config.getConfigurationSection(key + ".phases");
                phaseSection.getKeys(false).forEach(phase -> {

                    String phaseName = phase;
                    String regionName = config.getString(key + ".phases." + phase + ".region");
                    int mobSpawnRange = config.getInt(key + ".phases." + phase + ".spawnrange");
                    Phase newPhase = new Phase(phaseName, regionName, mobSpawnRange);
                    boolean mobsList = config.isList(key + ".phases." + phase + ".mobs");
                    if (mobsList) {
                        List<String> mobs = config.getStringList(key + ".phases." + phase + ".mobs");
                        for (String thatmob : mobs) {
                            String[] devide = thatmob.split(":");
                            newPhase.mobs.add(EntityType.valueOf(devide[0].toUpperCase()));
                        }

                    }

                    phases.put(newPhase.name, newPhase);

                });

            }

            newArena.setPhasesMap(phases);
            newArena.checkIfArenaIsReady();
            shortTermStorages.arenas.add(newArena);

        });
    }

    public void loadNewArena() {

    }


}
