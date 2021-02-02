package me.prismskey.rpgcore;


import com.gmail.nossr50.mcMMO;


import me.prismskey.rpgcore.ArenaManager.*;
import me.prismskey.rpgcore.DataManager.ConfigLoader;
import me.prismskey.rpgcore.DataManager.MobsLevelsConfigManager;
import me.prismskey.rpgcore.GeneralCommands.*;
import me.prismskey.rpgcore.Events.*;
import me.prismskey.rpgcore.Events.OnTriggerSpecialAbilities;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Mobs.EnemySpecialsManager;
import me.prismskey.rpgcore.Recipes.*;
import me.prismskey.rpgcore.Tasks.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

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
        ConfigLoader configloader = new ConfigLoader();
        configloader.parseDefaultConfig();
        configloader.loadPvpStatesConfig();
        configloader.loadArenaConfig();
        ArenaLoader arenaloader = new ArenaLoader();
        arenaloader.loadArenas();
        MobsLevelsConfigManager mlcm = new MobsLevelsConfigManager();
        try {
            mlcm.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        ComboResetTimer comboTimerTask = new ComboResetTimer(this);
        comboTimerTask.runTaskTimer(this, 0, 1);

        CoralArmorFeedTimer coralFeedTimer = new CoralArmorFeedTimer(this);
        coralFeedTimer.runTaskTimer(this, 0, 20 * 30);

        LostvayneParticleTask lostvayneParticleTask = new LostvayneParticleTask();
        lostvayneParticleTask.runTaskTimer(this, 0, 5);

        MagiGuardTask magiGuardTask = new MagiGuardTask();
        magiGuardTask.runTaskTimer(this, 0, 1);
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
        getServer().getPluginCommand("dscore").setExecutor(new DSCoreCommands());
        getServer().getPluginCommand("join").setExecutor(new joinArena());
        getServer().getPluginCommand("leave").setExecutor(new leaveArena());
        getServer().getPluginCommand("givepermissionreward").setExecutor(new GivePermissionReward());
        getServer().getPluginCommand("rejoin").setExecutor(new rejoinArena());
        getServer().getPluginCommand("fillblocks").setExecutor(new FillBlocksCommand());
        getServer().getPluginCommand("pvp").setExecutor(new PvpCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new OnChunkPopulate(), this);
        getServer().getPluginManager().registerEvents(new RegionEnterEvent(), this);
        getServer().getPluginManager().registerEvents(new RegionQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new onCustomItemDegradeOrMend(), this);
        getServer().getPluginManager().registerEvents(new ExtraAnvilRecipes(), this);
        getServer().getPluginManager().registerEvents(new DisableCraftGridRepair(), this);
        getServer().getPluginManager().registerEvents(new OnTriggerSpecialAbilities(), this);
        getServer().getPluginManager().registerEvents(new OnPvp(), this);
        getServer().getPluginManager().registerEvents(new DisableFireworkDamage(), this);
        //getServer().getPluginManager().registerEvents(new FinishedWaveEvent(), this);
        getServer().getPluginManager().registerEvents(new MobDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new EntityRecievingDmg(), this);
        getServer().getPluginManager().registerEvents(new onArenaFinishListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        //test event here will remove it after development
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new DisableFireworkDamage(), this);
        getServer().getPluginManager().registerEvents(new OnMagiGuardEntityAttacked(), this);
    }

    public void registerRecipes() {


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



    public static Economy getEconomy() {
        return econ;
    }




}
