package me.prismskey.rpgcore.Utils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.prismskey.rpgcore.ArenaManager.Arena;
import me.prismskey.rpgcore.DataManager.ConfigLoader;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static final ArrayList<String> DUNGEON_REGIONS = new ArrayList<>(Arrays.asList("creepy_crypt1", "sunken_temple1", "sandy_tombs1," +
            "creepy_crypt2", "sunken_temple2", "sandy_tombs2", "creepy_crypt3", "sunken_temple3", "sandy_tombs3"));
    private static final ConfigLoader loader = new ConfigLoader();
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
       return checkIfInDungeon(player.getLocation());
    }

    public static boolean checkIfInDungeon(Location theLocation) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(theLocation);
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

    public static boolean checkIfRegisteredForDungeon(Player player) {
        for(Arena arena: shortTermStorages.arenas) {
            if(arena.players.contains(player)) {
                return true;
            }
        }
        return false;
    }

    public static boolean locationInAnyRegion(Location loca) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(loca);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        if(set.size() > 1) {
            return true;
        }
        return false;
    }

    public static boolean isWithinRegion(Location loca, String name) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(loca);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        for (ProtectedRegion region : set) {
            if (name.equalsIgnoreCase(region.getId())) {
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

    public static boolean pvpCheck(Player p1, Player p2) {
        RPGPlayerData data1 = loader.getDataByUUID(p1.getUniqueId());
        RPGPlayerData data2 = loader.getDataByUUID(p2.getUniqueId());
        if(Utils.checkIfInDungeon(p1) || Utils.checkIfInDungeon(p2)) {
            return false;
        }
        if(data1.getPvpState() && data2.getPvpState()) {
            return true;
        }
        return false;
    }

    public static boolean entityIsHostile(Entity e) {
        if(e instanceof Skeleton || e instanceof Zombie || e instanceof Drowned || e instanceof Stray) {
            return true;
        }
        return false;
    }

    public static boolean isClearSpace(Location loc) {
        if(loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WATER) {
            return true;
        }
        return false;
    }

    public static boolean isCustomMob(Entity e) {
        return APIUsages.hasMobNBT(e, "hollow") || APIUsages.hasMobNBT(e, "ghost") ||
                APIUsages.hasMobNBT(e, "lich") || APIUsages.hasMobNBT(e, "fire") ||
                APIUsages.hasMobNBT(e, "pharaoh") || APIUsages.hasMobNBT(e, "earth") ||
                APIUsages.hasMobNBT(e, "fm.dark_elf") || APIUsages.hasMobNBT(e, "fm.werewolf") ||
                APIUsages.hasMobNBT(e, "fm.phoenix") || APIUsages.hasMobNBT(e, "fm.lizardman") ||
                APIUsages.hasMobNBT(e, "fm.harpy") || APIUsages.hasMobNBT(e, "fm.griffin") ||
                APIUsages.hasMobNBT(e, "fm.basilisk") || APIUsages.hasMobNBT(e, "fm.banshee") ||
                APIUsages.hasMobNBT(e, "fm.dryad") || APIUsages.hasMobNBT(e, "fm.naga") || APIUsages.hasMobNBT(e, "fm.hobgoblin") ||
                APIUsages.hasMobNBT(e, "fm.golden_beetle") || APIUsages.hasMobNBT(e, "fm.cerberus");
    }

    public static ItemStack customItemConstructor(Material mat, String name, int customModelData) {
        ItemStack result = new ItemStack(mat);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(name);
        meta.setCustomModelData(customModelData);
        result.setItemMeta(meta);
        return result;
    }

}
