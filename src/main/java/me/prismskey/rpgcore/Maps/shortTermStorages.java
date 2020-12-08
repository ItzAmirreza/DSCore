package me.prismskey.rpgcore.Maps;

import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class shortTermStorages {

    public static ArrayList<RPGPlayerData> playerData = new ArrayList<>();

    public static ArrayList<Arena> arenas = new ArrayList<>(); //loaded arenas + arena can be loaded but not ready

    public static File arenasFile;

    public static FileConfiguration arenasConfiguration;

    public static File pvpStatesFile;

    public static FileConfiguration pvpStatesConfiguration;

    public static Location spawn;

    public static ArrayList<NamespacedKey> recipeKeys = new ArrayList<>();

    public static FileConfiguration getArenaConfig() {
        return arenasConfiguration;
    }

    public static List<String> playersInMatch = new ArrayList<>();



}
