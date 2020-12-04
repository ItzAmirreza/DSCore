package me.prismskey.rpgcore;


import com.gmail.nossr50.mcMMO;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;

import com.sk89q.worldguard.protection.ApplicableRegionSet;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;


import de.tr7zw.nbtapi.NBTItem;
import me.prismskey.rpgcore.ArenaManager.*;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.DataManager.configLoader;
import me.prismskey.rpgcore.Events.MobKilledInArenaEvent;
import me.prismskey.rpgcore.Events.MobSpawnInArenaEvent;
import me.prismskey.rpgcore.GeneralCommands.PvpCommand;
import me.prismskey.rpgcore.Events.*;
import me.prismskey.rpgcore.Events.OnTriggerSpecialAbilities;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Mobs.EnemySpecialsManager;
import me.prismskey.rpgcore.Recipes.*;
import me.prismskey.rpgcore.Tasks.cooldownCheckTask;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.*;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public final class Rpgcore extends JavaPlugin {

    public static Rpgcore instance;
    public static Rpgcore getInstance() {
        return instance;
    }

    private static Economy econ = null;
    private EnemySpecialsManager enemySpecialsManager;


    private static mcMMO mcMMOPlugin;

    @Override
    public void onEnable() {
        instance = this;

        enemySpecialsManager = new EnemySpecialsManager();

        saveDefaultConfig();
        configLoader configloader = new configLoader();
        arenaLoader arenaloader = new arenaLoader();




        if(!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        startRegistration();
        startTasks();



    }

    //registration section

    public void startRegistration() {
        registerCommands();
        registerEvents();
        registerRecipes();
    }

    public void startTasks() {
        cooldownCheckTask cooldownCheckTask = new cooldownCheckTask(this);
        cooldownCheckTask.runTaskTimer(this, 0, 1);



    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void registerCommands() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            //commandMap.register("party", new PartyCommand("party"));
            commandMap.register("dungeon", new DungeonJoinCommand("dungeon"));
            commandMap.register("forceend", new ForceEndCommand("forceend"));
            commandMap.register("pvp", new PvpCommand("pvp"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new MobSpawnInArenaEvent(), this);
        getServer().getPluginManager().registerEvents(new MobKilledInArenaEvent(), this);
        getServer().getPluginManager().registerEvents(new OnSlimeSplit(), this);
        getServer().getPluginManager().registerEvents(new OnCommandPreProcess(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnChunkPopulate(), this);
        getServer().getPluginManager().registerEvents(new onCustomItemDegradeOrMend(), this);
        getServer().getPluginManager().registerEvents(new ExtraAnvilRecipes(), this);
        getServer().getPluginManager().registerEvents(new DisableCraftGridRepair(), this);
        getServer().getPluginManager().registerEvents(new OnTriggerSpecialAbilities(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new OnPvp(), this);
        getServer().getPluginManager().registerEvents(new DisableFireworkDamage(), this);
    }

    public void registerRecipes() {

        DragonStoneRecipes dragonStoneRecipes = new DragonStoneRecipes();
        AdamantRecipes adamantRecipes = new AdamantRecipes();
        MithrilRecipes mithrilRecipes = new MithrilRecipes();
        OrichalcumRecipes orichalcumRecipes = new OrichalcumRecipes();
    }

    private void unRegisterRecipes() {
        for(NamespacedKey key: shortTermStorages.recipeKeys) {
            Bukkit.removeRecipe(key);
        }
    }



    //end of registrations <--



    @Override
    public void onDisable() {
        unRegisterRecipes();
    }

    // I don't really know what this function is doing and how it is related just know its using worldedit api

    public boolean isWithinDungeon(Location loc)
    {
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(loc);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);
        for(ProtectedRegion rg: set.getRegions()) {

            for(Arena arena: shortTermStorages.arenas) {
                if(rg.getId().toLowerCase().contains(arena.getName().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    //same ^^

    public Arena getArenaByLocation(Location loc) {
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(loc);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);
        for(ProtectedRegion rg: set.getRegions()) {

            for(Arena arena: shortTermStorages.arenas) {
                if(rg.getId().toLowerCase().contains(arena.getName().toLowerCase())) {
                    return arena;
                }
            }
        }
        return null;
    }



    public String getDungeonRegionName(Location loc) {
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(loc);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);
        for(ProtectedRegion rg: set.getRegions()) {
            if(rg.getId().toLowerCase().contains("dungeon")) {
                return rg.getId();
            }
        }
        return "";
    }



    public Arena getArenaByName(String name) {
        for(Arena arena: arenas) {
            if(arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

    public Location getSpawn() {
        return spawn;
    }

    public static boolean hasMobNBT(Entity e, String tag) {
        net.minecraft.server.v1_16_R3.Entity entity = ((CraftEntity)e).getHandle();

        net.minecraft.server.v1_16_R3.NBTTagCompound compound = new net.minecraft.server.v1_16_R3.NBTTagCompound();
        entity.d(compound);
        NBTTagList list = compound.getList("Tags",8);
        return list.toString().contains(tag);
    }

    public RPGPlayerData getDataByUUID(UUID uuid) {
        for(RPGPlayerData data: playerData) {
            if(data.getPlayerUUID().equals(uuid)) {
                return data;
            }
        }
        return null;
    }

    public ArrayList<Arena> getArenas() {
        return arenas;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public ArrayList<RPGPlayerData> getPlayerData() {
        return playerData;
    }

    public FileConfiguration getPvpStatesConfiguration() {
        return pvpStatesConfiguration;
    }

    public void savePvpStatesConfig() {
        try {
            pvpStatesConfiguration.save(pvpStatesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerData(RPGPlayerData data) {
        for(int i = 0; i < playerData.size(); i++) {
            RPGPlayerData d = playerData.get(i);
            if(d.getPlayerUUID().equals(data.getPlayerUUID())) {
                playerData.set(i, data);
            }
        }
    }
}
