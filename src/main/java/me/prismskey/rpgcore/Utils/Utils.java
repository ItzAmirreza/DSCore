package me.prismskey.rpgcore.Utils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static final ArrayList<String> DUNGEON_REGIONS = new ArrayList<>(Arrays.asList("creepy_crypt1", "sunken_temple1"));

    //Dead_Light edit

    public static String Prefix = "&8[&eDSCore&8] ";

    //convertion of color codes (spigot api - normal use)
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }



    //convetion of color codes (bungee api - special case)
    public static String colorBungee(String str) {
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', str);
    }

    public static boolean checkIfInDungeon(Player player) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        for (ProtectedRegion region : set) {
            if (DUNGEON_REGIONS.contains(region.getId())) {
                return true;
            }
        }
        return false;
    }

    //Location to string serializer
    public static String convertLocToString(Location location) {

        //int x, int y, int z, String world, float pitch, float yaw
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String world = location.getWorld().getName();
        float pitch = location.getPitch();
        float yaw = location.getYaw();
        return Integer.toString(x) + ":" + Integer.toString(y) + ":" + Integer.toString(z) + ":" + world + ":" + Float.toString(pitch) + ":" + Float.toString(yaw);
    }



    //String to location serializer
    public static Location convertStringToLoc(String stringloc) {

        //4 --> pitch
        //5 --> yaw
        List<String> splited = Arrays.asList(stringloc.split(":"));

        return new Location(Bukkit.getWorld(splited.get(3)), Integer.parseInt(splited.get(0)) + 0.5, Integer.parseInt(splited.get(1)), Integer.parseInt(splited.get(2)) + 0.5, Float.parseFloat(splited.get(5)), Float.parseFloat(splited.get(4)));
    }
















}
