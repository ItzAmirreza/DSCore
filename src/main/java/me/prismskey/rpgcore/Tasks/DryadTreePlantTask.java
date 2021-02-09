package me.prismskey.rpgcore.Tasks;

import com.mojang.authlib.GameProfile;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.APIUsages;
import me.prismskey.rpgcore.Utils.Utils;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DryadTreePlantTask extends BukkitRunnable {
    Random r = new Random();

    //Player npcPlayer;
    public DryadTreePlantTask() {
        super();
        /*MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorld("dungeons")).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        npcPlayer = npc.getBukkitEntity().getPlayer();
        npcPlayer.setPlayerListName("");
        npc.setLocation(1594, 7, 1298, 0, 0);*/
    }

    @Override
    public void run() {
        ArrayList<LivingEntity> livingEntities = new ArrayList();
        livingEntities.addAll(Bukkit.getWorld("world").getLivingEntities());
        livingEntities.addAll(Bukkit.getWorld("world_nether").getLivingEntities());
        livingEntities.addAll(Bukkit.getWorld("world_the_end").getLivingEntities());
        for (LivingEntity living : livingEntities) {
            if (APIUsages.hasMobNBT(living, "dryad")) {
                //Rpgcore.getInstance().getLogger().info("check a");
                tryPlantSapling(living.getLocation());
            }
        }
    }

    private void tryPlantSapling(Location loc) {
        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    Location theLoc = loc.clone().add(x, y, z);
                    if (Utils.locationInAnyRegion(theLoc)) {
                        continue;
                    }
                    if (isLogOrSaplingBlock(theLoc.getBlock())) {
                        return;
                    }
                }
            }
        }

        ArrayList<Location> validPlantingSpots = new ArrayList<>();

        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    Location theLoc = loc.clone().add(x, y, z);
                    if (theLoc.getBlock() == null) {
                        continue;
                    }
                    if (isValidPlantingSpot(theLoc)) {
                        validPlantingSpots.add(theLoc);
                    }
                }
            }
        }

        if (validPlantingSpots.size() > 0) {
            Location random = validPlantingSpots.get(r.nextInt(validPlantingSpots.size()));
            random.getBlock().setType(getRandomSaplingTypeForLocation(random));
        }
    }

    private Material getRandomSaplingTypeForLocation(Location loc) {
        ArrayList<Material> saplingTypes = new ArrayList<>();
        Biome biome = loc.getBlock().getBiome();
        if (biomeMatchesOak(biome)) {
            saplingTypes.add(Material.OAK_SAPLING);
        }
        if (biomeMatchesDarkOak(biome)) {
            saplingTypes.add(Material.DARK_OAK_SAPLING);
        }
        if (biomeMatchesBirch(biome)) {
            saplingTypes.add(Material.BIRCH_SAPLING);
        }
        if (biomeMatchesAcacia(biome)) {
            saplingTypes.add(Material.ACACIA_SAPLING);
        }
        if (biomeMatchesJungle(biome)) {
            saplingTypes.add(Material.JUNGLE_SAPLING);
        }
        if (biomeMatchesSpruce(biome)) {
            saplingTypes.add(Material.SPRUCE_SAPLING);
        }
        if (saplingTypes.size() > 0) {
            return saplingTypes.get(r.nextInt(saplingTypes.size()));
        }
        return Material.AIR;
    }

    private boolean biomeMatchesOak(Biome biome) {
        if (biome == Biome.BAMBOO_JUNGLE || biome == Biome.BAMBOO_JUNGLE_HILLS || biome == Biome.DARK_FOREST ||
                biome == Biome.DARK_FOREST_HILLS || biome == Biome.FOREST || biome == Biome.FLOWER_FOREST || biome == Biome.JUNGLE ||
                biome == Biome.JUNGLE_EDGE || biome == Biome.JUNGLE_HILLS || biome == Biome.MODIFIED_JUNGLE || biome == Biome.MODIFIED_JUNGLE_EDGE ||
                biome == Biome.PLAINS || biome == Biome.SUNFLOWER_PLAINS || biome == Biome.RIVER || biome == Biome.SAVANNA || biome == Biome.SAVANNA_PLATEAU ||
                biome == Biome.SWAMP || biome == Biome.WOODED_BADLANDS_PLATEAU || biome == Biome.MODIFIED_WOODED_BADLANDS_PLATEAU || biome == Biome.WOODED_MOUNTAINS || biome == Biome.WOODED_HILLS) {
            return true;
        }
        return false;
    }

    private boolean biomeMatchesDarkOak(Biome biome) {
        if (biome == Biome.DARK_FOREST_HILLS || biome == Biome.DARK_FOREST) {
            return true;
        }
        return false;
    }

    private boolean biomeMatchesBirch(Biome biome) {
        if (biome == Biome.FOREST || biome == Biome.BIRCH_FOREST || biome == Biome.BIRCH_FOREST_HILLS || biome == Biome.DARK_FOREST ||
                biome == Biome.DARK_FOREST_HILLS) {
            return true;
        }
        return false;
    }

    private boolean biomeMatchesJungle(Biome biome) {
        if (biome == Biome.BAMBOO_JUNGLE || biome == Biome.BAMBOO_JUNGLE_HILLS || biome == Biome.JUNGLE ||
                biome == Biome.JUNGLE_EDGE || biome == Biome.JUNGLE_HILLS || biome == Biome.MODIFIED_JUNGLE_EDGE || biome == Biome.MODIFIED_JUNGLE) {
            return true;
        }
        return false;
    }

    private boolean biomeMatchesSpruce(Biome biome) {
        if (biome == Biome.GIANT_SPRUCE_TAIGA || biome == Biome.GIANT_SPRUCE_TAIGA_HILLS || biome == Biome.GIANT_TREE_TAIGA || biome == Biome.GIANT_TREE_TAIGA_HILLS ||
                biome == Biome.SNOWY_MOUNTAINS || biome == Biome.SNOWY_TAIGA || biome == Biome.SNOWY_TAIGA_HILLS || biome == Biome.SNOWY_TAIGA_MOUNTAINS ||
                biome == Biome.SNOWY_TUNDRA) {
            return true;
        }
        return false;
    }

    private boolean biomeMatchesAcacia(Biome biome) {
        if (biome == Biome.SAVANNA_PLATEAU || biome == Biome.SAVANNA) {
            return true;
        }
        return false;
    }

    private boolean isValidPlantingSpot(Location loc) {
        if (Utils.locationInAnyRegion(loc)) {
            return false;
        }
        if (loc.getBlock() == null) {
            return false;
        }
        if (loc.getBlock().getType() == Material.AIR) {
            Block below = loc.clone().add(0, -1, 0).getBlock();
            if (below == null) {
                return false;
            }
            if (below.getType() == Material.DIRT || below.getType() == Material.PODZOL || below.getType() == Material.GRASS_BLOCK) {
                return true;
            }
        }
        return false;
    }

    private boolean isLogOrSaplingBlock(Block block) {
        return block != null && (block.getType() == Material.OAK_LOG || block.getType() == Material.DARK_OAK_LOG ||
                block.getType() == Material.BIRCH_LOG || block.getType() == Material.JUNGLE_LOG || block.getType() == Material.SPRUCE_LOG ||
                block.getType() == Material.ACACIA_LOG || block.getType() == Material.OAK_SAPLING || block.getType() == Material.DARK_OAK_SAPLING ||
                block.getType() == Material.BIRCH_SAPLING || block.getType() == Material.JUNGLE_SAPLING || block.getType() == Material.SPRUCE_SAPLING ||
                block.getType() == Material.ACACIA_SAPLING);
    }

    /*private void tryGrowSapling(Entity entity, Player player) {
        Location loc = entity.getLocation();
        boolean grewSapling = false;
        for(int x = -5; x <= 5; x++) {
            for(int y = -5; y <= 5; y++) {
                for(int z = -5; z <= 5; z++) {
                    Location theLoc = loc.clone().add(x, y, z);
                    if(Utils.locationInAnyRegion(theLoc)) {
                        continue;
                    }
                    BlockData block = theLoc.getBlock().getBlockData();
                    //Rpgcore.getInstance().getLogger().info("check b");
                    if(block instanceof Sapling && !(block instanceof Bamboo)) {
                        //Rpgcore.getInstance().getLogger().info("Sapling found");
                        growStructure(theLoc);
                        grewSapling = true;
                        break;
                    }
                }
                if(grewSapling) {
                    break;
                }
            }
            if(grewSapling) {
                break;
            }
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                if(loc.getBlock().getType() != Material.AIR) {
                    for(int x = -5; x <= 5; x++) {
                        for (int y = -5; y <= 5; y++) {
                            for (int z = -5; z <= 5; z++) {
                                if(loc.clone().add(x, y, z).getBlock().getType() == Material.AIR &&
                                        loc.clone().add(x, y + 1, z).getBlock().getType() == Material.AIR) {
                                    entity.teleport(loc.clone().add(x, y, z));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskLater(Rpgcore.getInstance(), 1);
    }

    private boolean isSapling(Material mat) {
        return mat == Material.OAK_SAPLING || mat == Material.DARK_OAK_SAPLING || mat == Material.BIRCH_SAPLING ||
                mat == Material.SPRUCE_SAPLING || mat == Material.ACACIA_SAPLING || mat == Material.JUNGLE_SAPLING;
    }*/

    /*private void growStructure(Location loc) {
        try {
            Class clazz = Class.forName("org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack");
            Constructor<CraftItemStack> constructor = clazz.getDeclaredConstructor(ItemStack.class);
            constructor.setAccessible(true);
            CraftItemStack stack = constructor.newInstance(new ItemStack(Material.BONE_MEAL));
            net.minecraft.server.v1_16_R3.ItemBoneMeal.applyBonemeal(new ItemActionContext(((CraftPlayer) npcPlayer).getHandle(), EnumHand.MAIN_HAND, new MovingObjectPositionBlock(new Vec3D(loc.getX(), loc.getY(), loc.getZ()), EnumDirection.UP, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), false)));
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            Rpgcore.getInstance().getLogger().info("Class not found when trying reflection.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }*/
}
