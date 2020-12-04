package me.prismskey.rpgcore.ArenaManager;

import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class arenaLoader {

    private Rpgcore rpgcore = Rpgcore.getInstance();


    public void parseArenaConfig() {
        for(String arenaName: shortTermStorages.getArenaConfig().getKeys(false)) {
            ConfigurationSection specificArenaSection = shortTermStorages.getArenaConfig().getConfigurationSection(arenaName);
            int arenaSpawnX = specificArenaSection.getInt("x");
            int arenaSpawnY = specificArenaSection.getInt("y");
            int arenaSpawnZ = specificArenaSection.getInt("z");
            String arenaWorldString = specificArenaSection.getString("world");
            Location arenaSpawnLoc = new Location(rpgcore.getServer().getWorld(arenaWorldString), arenaSpawnX, arenaSpawnY, arenaSpawnZ);
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
                    rpgcore.getLogger().info("Error: arenaPhase is null");
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
                        World mobSpawnWorld = rpgcore.getServer().getWorld(mobSpawnWorldString);
                        Location mobSpawnLocation = new Location(mobSpawnWorld, mobSpawnX, mobSpawnY, mobSpawnZ);
                        String mobSpawnType = specificCommandSection.getString("prebuilt_type");
                        arenaCommand = new SpawnPrebuiltCommand(mobSpawnLocation, mobSpawnType);
                    }
                    if(commandType.equalsIgnoreCase("command")) {
                        String cmd = specificCommandSection.getString("command");
                        rpgcore.getLogger().info(cmd);
                        arenaCommand = new ServerCommandExecutor(cmd);
                    }
                    if(arenaCommand == null) {
                        rpgcore.getLogger().info("Error arenaCommand is null");
                        return;
                    }
                    arenaPhase.addCommand(arenaCommand);
                }
                arena.addPhase(arenaPhase);
            }
            shortTermStorages.arenas.add(arena);
        }
    }


}