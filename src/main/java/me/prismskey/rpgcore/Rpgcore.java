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
import me.prismskey.rpgcore.Events.MobKilledInArenaEvent;
import me.prismskey.rpgcore.Events.MobSpawnInArenaEvent;
import me.prismskey.rpgcore.GeneralCommands.PvpCommand;
import me.prismskey.rpgcore.Events.*;
import me.prismskey.rpgcore.Events.OnTriggerSpecialAbilities;
import me.prismskey.rpgcore.Mobs.EnemySpecialsManager;
import me.prismskey.rpgcore.Recipes.ExtraAnvilRecipes;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public final class Rpgcore extends JavaPlugin {

    public static Rpgcore instance;
    private Location spawn;

    private File arenasFile;
    private FileConfiguration arenasConfiguration;

    private File pvpStatesFile;
    private FileConfiguration pvpStatesConfiguration;

    private ArrayList<Arena> arenas;
    private ArrayList<NamespacedKey> recipeKeys;
    private ArrayList<RPGPlayerData> playerData;
    private static Economy econ = null;
    private EnemySpecialsManager enemySpecialsManager;

    private static mcMMO mcMMOPlugin;

    @Override
    public void onEnable() {
        instance = this;
        arenas = new ArrayList<>();
        recipeKeys = new ArrayList<>();
        playerData = new ArrayList<>();
        enemySpecialsManager = new EnemySpecialsManager();

        saveDefaultConfig();
        parseDefaultConfig();
        loadArenaConfig();
        parseArenaConfig();
        loadPvpStatesConfig();



        if(!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        startRegistration();
        startCooldownCheckRunnable();


    }

    public void startRegistration() {
        registerCommands();
        registerEvents();
        registerRecipes();
    }


    public void startCooldownCheckRunnable() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for(int i = 0; i < playerData.size(); i++) {
                    RPGPlayerData data = playerData.get(i);
                    data.decrementCooldowns();
                    if(!data.isPlayerOnline() && data.allCooldownsOff()) {
                        playerData.remove(i);
                        i--;
                    }

                }
            }
        }.runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        unRegisterRecipes();
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

    private void parseArenaConfig() {
        for(String arenaName: getArenaConfig().getKeys(false)) {
            ConfigurationSection specificArenaSection = getArenaConfig().getConfigurationSection(arenaName);
            int arenaSpawnX = specificArenaSection.getInt("x");
            int arenaSpawnY = specificArenaSection.getInt("y");
            int arenaSpawnZ = specificArenaSection.getInt("z");
            String arenaWorldString = specificArenaSection.getString("world");
            Location arenaSpawnLoc = new Location(getServer().getWorld(arenaWorldString), arenaSpawnX, arenaSpawnY, arenaSpawnZ);
            String arenaRegionString = specificArenaSection.getString("region_name");
            Arena arena = new Arena(arenaSpawnLoc, arenaName, arenaRegionString);

            ConfigurationSection phasesSection = specificArenaSection.getConfigurationSection("phases");
            for(String phaseName: phasesSection.getKeys(false)) {
                ConfigurationSection currentPhaseSection = phasesSection.getConfigurationSection(phaseName);

                String goal = currentPhaseSection.getString("goal");
                ArenaPhase arenaPhase = null;

                if(goal.equalsIgnoreCase("killall")) {
                    arenaPhase = new KillAllPhase();
                }
                if(arenaPhase == null) {
                    getLogger().info("Error: arenaPhase is null");
                    return;
                }

                ConfigurationSection commandsSection = currentPhaseSection.getConfigurationSection("commands");
                for(String commandName: commandsSection.getKeys(false)) {
                    ConfigurationSection specificCommandSection = commandsSection.getConfigurationSection(commandName);
                    String commandType = specificCommandSection.getString("type");
                    ArenaCommand arenaCommand = null;
                    if(commandType.equalsIgnoreCase("spawn_prebuilt")) {
                        int mobSpawnX = specificCommandSection.getInt("x");
                        int mobSpawnY = specificCommandSection.getInt("y");
                        int mobSpawnZ = specificCommandSection.getInt("z");
                        String mobSpawnWorldString = specificCommandSection.getString("world");
                        World mobSpawnWorld = getServer().getWorld(mobSpawnWorldString);
                        Location mobSpawnLocation = new Location(mobSpawnWorld, mobSpawnX, mobSpawnY, mobSpawnZ);
                        String mobSpawnType = specificCommandSection.getString("prebuilt_type");
                        arenaCommand = new SpawnPrebuiltCommand(mobSpawnLocation, mobSpawnType);
                    }
                    if(commandType.equalsIgnoreCase("command")) {
                        String cmd = specificCommandSection.getString("command");
                        getLogger().info(cmd);
                        arenaCommand = new ServerCommandExecutor(cmd);
                    }
                    if(arenaCommand == null) {
                        getLogger().info("Error arenaCommand is null");
                        return;
                    }
                    arenaPhase.addCommand(arenaCommand);
                }
                arena.addPhase(arenaPhase);
            }
            arenas.add(arena);
        }
    }

    public FileConfiguration getArenaConfig() {
        return this.arenasConfiguration;
    }

    private void loadArenaConfig() {
        arenasFile = new File(getDataFolder(), "arenas.yml");
        if (!arenasFile.exists()) {
            arenasFile.getParentFile().mkdirs();
            saveResource("arenas.yml", false);
        }

        arenasConfiguration = new YamlConfiguration();
        try {
            arenasConfiguration.load(arenasFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadPvpStatesConfig() {
        pvpStatesFile = new File(getDataFolder(), "pvpStates.yml");
        if(!pvpStatesFile.exists()) {
            pvpStatesFile.getParentFile().mkdirs();
            saveResource("pvpStates.yml", false);
        }

        pvpStatesConfiguration = new YamlConfiguration();
        try {
            pvpStatesConfiguration.load(pvpStatesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void parseDefaultConfig() {
        ConfigurationSection spawnSection = getConfig().getConfigurationSection("spawn");
        int x = spawnSection.getInt("x");
        int y = spawnSection.getInt("y");
        int z = spawnSection.getInt("z");
        String world = spawnSection.getString("world");
        spawn = new Location(getServer().getWorld(world), x, y, z);
    }

    public boolean isWithinDungeon(Location loc)
    {
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(loc);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);
        for(ProtectedRegion rg: set.getRegions()) {

            for(Arena arena: arenas) {
                if(rg.getId().toLowerCase().contains(arena.getName().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Arena getArenaByLocation(Location loc) {
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(loc);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);
        for(ProtectedRegion rg: set.getRegions()) {

            for(Arena arena: arenas) {
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
        registerDragonstoneHelmet();
        registerDragonstoneChestplate();
        registerDragonstoneLeggings();
        registerDragonstoneBoots();

        registerAdamantHelmet();
        registerAdamantChestplate();
        registerAdamantLeggings();
        registerAdamantBoots();

        registerMithrilHelmet();
        registerMithrilChestplate();
        registerMithrilLeggings();
        registerMithrilBoots();

        registerOrichalcumHelmet();
        registerOrichalcumChestplate();
        registerOrichalcumLeggings();
        registerOrichalcumBoots();
    }

    private void unRegisterRecipes() {
        for(NamespacedKey key: recipeKeys) {
            Bukkit.removeRecipe(key);
        }
    }

    private void registerDragonstoneHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 807);
        nbti.setInteger("maxDurability", 807);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "dragonstone_helmet");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("DDD", "D D", "   ");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerDragonstoneChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 992);
        nbti.setInteger("maxDurability", 992);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 11, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "dragonstone_chestplate");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("D D", "DDD", "DDD");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerDragonstoneLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 955);
        nbti.setInteger("maxDurability", 955);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "dragonstone_leggings");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("DDD", "D D", "D D");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerDragonstoneBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("dragonstone", true);
        nbti.setInteger("currentDurability", 881);
        nbti.setInteger("maxDurability", 881);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonstone Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.PURPLE);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "dragonstone_boots");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "D D", "D D");
        recipe.setIngredient('D', Material.STRAY_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }

    private void registerAdamantHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 507);
        nbti.setInteger("maxDurability", 507);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "adamant_helmet");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("AAA", "A A", "   ");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerAdamantChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 692);
        nbti.setInteger("maxDurability", 692);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "adamant_chestplate");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("A A", "AAA", "AAA");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerAdamantLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 655);
        nbti.setInteger("maxDurability", 655);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "adamant_leggings");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("AAA", "A A", "A A");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerAdamantBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("adamant", true);
        nbti.setInteger("currentDurability", 581);
        nbti.setInteger("maxDurability", 581);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Adamant Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.fromRGB(0x2AAD83));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "adamant_boots");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "A A", "A A");
        recipe.setIngredient('A', Material.DROWNED_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }

    private void registerMithrilHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 263);
        nbti.setInteger("maxDurability", 263);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "mithril_helmet");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("MMM", "M M", "   ");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerMithrilChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 370);
        nbti.setInteger("maxDurability", 370);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "mithril_chestplate");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("M M", "MMM", "MMM");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerMithrilLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 360);
        nbti.setInteger("maxDurability", 360);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "mithril_leggings");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("MMM", "M M", "M M");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerMithrilBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("mithril", true);
        nbti.setInteger("currentDurability", 325);
        nbti.setInteger("maxDurability", 325);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Mithril Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.fromRGB(0xFFFFFF));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "mithril_boots");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "M M", "M M");
        recipe.setIngredient('M', Material.GHAST_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }

    private void registerOrichalcumHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 607);
        nbti.setInteger("maxDurability", 607);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Helmet");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1111);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "orichalcum-helmet");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("OOO", "O O", "   ");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerOrichalcumChestplate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 792);
        nbti.setInteger("maxDurability", 792);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Chestplate");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1112);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "orichalcum-chestplate");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("O O", "OOO", "OOO");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerOrichalcumLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 755);
        nbti.setInteger("maxDurability", 755);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Leggings");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1113);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "orichalcum-leggings");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("OOO", "O O", "O O");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
    }
    private void registerOrichalcumBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("orichalcum", true);
        nbti.setInteger("currentDurability", 681);
        nbti.setInteger("maxDurability", 681);
        item = nbti.getItem();


        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orichalcum Boots");
        String[] lore = {ChatColor.BLACK + "."};
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(1114);
        meta.setColor(Color.fromRGB(0xFF0000));


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);


        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "orichalcum-boots");
        recipeKeys.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "O O", "O O");
        recipe.setIngredient('O', Material.PARROT_SPAWN_EGG);
        Bukkit.addRecipe(recipe);
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
