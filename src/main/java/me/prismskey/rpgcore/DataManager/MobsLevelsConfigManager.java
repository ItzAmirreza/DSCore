package me.prismskey.rpgcore.DataManager;

import me.prismskey.rpgcore.Enums.SpecialMobs;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MobsLevelsConfigManager {

    public MobsLevelsConfigManager() {

    }


    public void loadConfig() throws IOException {


        //not special mobs here
        List<EntityType> allmobs = Arrays.asList(EntityType.values());
        List<SpecialMobs> allSpecialMobs = Arrays.asList(SpecialMobs.values());



        File mobsfolder = new File(Rpgcore.getInstance().getDataFolder().getPath() + File.separator + "Mobs");
        if (!mobsfolder.exists()) {
            mobsfolder.mkdir();
        }
        //For entityTypes

        for (EntityType mob : allmobs) {

            if (mob.isAlive()) {
                File mobConfig = new File(mobsfolder.getPath() + File.separator + mob.name() + ".yml");
                if (!mobConfig.exists()) {
                    mobConfig.createNewFile();
                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(mobConfig);
                    yml.set("values", null);
                    yml.set("default.health", 10);
                    yml.set("default.damage", 3);
                    yml.set("values.1.health", 10);
                    yml.set("values.1.damage", 3);
                    yml.set("values.2.health", 20);
                    yml.set("values.2.damage", 3);
                    yml.set("values.3.health", 25);
                    yml.set("values.3.damage", 4);
                    yml.save(mobConfig);
                    //Rpgcore.getInstance().saveResource(mobsfolder.getPath() + File.separator + mob.name() + ".yml", false);

                }
            }


        }

        //for specialMobs

        for (SpecialMobs mob : allSpecialMobs) {

            File mobConfig = new File(mobsfolder.getPath() + File.separator + mob.name() + ".yml");
                if (!mobConfig.exists()) {
                    mobConfig.createNewFile();
                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(mobConfig);
                    yml.set("values", null);
                    yml.set("default.health", 10);
                    yml.set("default.damage", 3);
                    yml.set("values.1.health", 10);
                    yml.set("values.1.damage", 3);
                    yml.set("values.2.health", 20);
                    yml.set("values.2.damage", 3);
                    yml.set("values.3.health", 25);
                    yml.set("values.3.damage", 4);
                    yml.save(mobConfig);
                    //Rpgcore.getInstance().saveResource(mobsfolder.getPath() + File.separator + mob.name() + ".yml", false);

                }



        }





    }


    public int getMobDamage(String name, String level) {

        if (shortTermStorages.mobsConfig.containsKey(name)) {
            YamlConfiguration yaml = shortTermStorages.mobsConfig.get(name);

            int damage = yaml.getInt("values." + level + ".damage", yaml.getInt("default.damage"));

            return damage;
        } else {
            File mobsfolder = new File(Rpgcore.getInstance().getDataFolder().getPath() + File.separator + "Mobs");
            File mobFile = new File(mobsfolder.getPath() + File.separator + name + ".yml");
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(mobFile);
            shortTermStorages.mobsConfig.put(name, yml);
            int damage = yml.getInt("values." + level + ".damage", yml.getInt("default.damage"));
            return damage;

        }



    }


    public int getMobHealth(String name, String level) {

        if (shortTermStorages.mobsConfig.containsKey(name)) {
            YamlConfiguration yaml = shortTermStorages.mobsConfig.get(name);

            int health = yaml.getInt("values." + level + ".health", yaml.getInt("default.health"));

            return health;
        } else {
            File mobsfolder = new File(Rpgcore.getInstance().getDataFolder().getPath() + File.separator + "Mobs");
            File mobFile = new File(mobsfolder.getPath() + File.separator + name + ".yml");
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(mobFile);
            shortTermStorages.mobsConfig.put(name, yml);
            int health = yml.getInt("values." + level + ".health", yml.getInt("default.health"));
            return health;

        }



    }



}
