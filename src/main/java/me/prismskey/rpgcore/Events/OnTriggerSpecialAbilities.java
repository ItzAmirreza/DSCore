package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.DataManager.*;
import me.prismskey.rpgcore.Enums.CooldownTimes;
import me.prismskey.rpgcore.Maps.shortTermStorages;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.NBTItem;
import me.prismskey.rpgcore.VFireworks.InstantFirework;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.Explosion;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnTriggerSpecialAbilities implements Listener {

    private Random r = new Random();
    private final ConfigLoader configloader = new ConfigLoader();

    @EventHandler
    public void onPlayerInteractWhileHoldingSpecialAbilityItem(PlayerInteractEvent event) {
        ClickCombo combo = shortTermStorages.clickComboHashMap.get(event.getPlayer().getUniqueId());

        if (event.getPlayer().getInventory().getItemInMainHand() != null && isActiveSpecialAbilityItem(event.getPlayer().getInventory().getItemInMainHand())) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (!combo.getCombo().equalsIgnoreCase("[ - - - ]")) {
                    combo.addClick(CLICKTYPE.LEFT, event.getPlayer());
                    event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + combo.getCombo()));
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                combo.addClick(CLICKTYPE.RIGHT, event.getPlayer());
                event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + combo.getCombo()));
            }
        }

        Player player = event.getPlayer();
        RPGPlayerData data = configloader.getDataByUUID(player.getUniqueId());
        //Rpgcore.instance.getLogger().info("onTriggerSpecial");

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            return;
        }
        NBTItem nbti = new NBTItem(item);
        if (nbti.hasKey("Excalibur")) {
            handleExcaliburSpecials(event, data);
        } else if (nbti.hasKey("lich")) {
            handleLichStaffSpecials(event, data);
        } else if (nbti.hasKey("Hollowscythe")) {
            handleHollowScytheSpecials(event, data);
        } else if (nbti.hasKey("galaxy")) {
            handleGalaxySwordSpecials(event, data);
        } else if(nbti.hasKey("master_sword")) {
            handleMasterSwordSpecials(event, data);
        } else if(nbti.hasKey("keyblade")) {
            handleKeyBladeSpecials(event, data);
        }
    }

    public boolean isActiveSpecialAbilityItem(ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        if (nbti.hasKey("Excalibur") || nbti.hasKey("lich") || nbti.hasKey("Hollowscythe")
                || nbti.hasKey("galaxy") || nbti.hasKey("master_sword") || nbti.hasKey("keyblade")) {
            return true;
        }
        return false;
    }

    private void handleKeyBladeSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();
        ClickCombo combo = shortTermStorages.clickComboHashMap.get(player.getUniqueId());
        if(combo.getCombo().equalsIgnoreCase("[ R L L ]")) {
            data.switchKeyBladeMode();
            player.sendMessage(ChatColor.GREEN + "Mode switched to " + data.getKeybladeMode().toString() + ".");
            combo.resetCombo();
        }
    }

    private void handleMasterSwordSpecials(PlayerInteractEvent event, RPGPlayerData data) {


        Player player = event.getPlayer();
        ClickCombo combo = shortTermStorages.clickComboHashMap.get(player.getUniqueId());
        if (combo.getCombo().equalsIgnoreCase("[ R L L ]")) {
            if(event.getPlayer().getHealth() == event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                if(data.getMasterSwordBeamCooldown() <= 0) {
                    triggerMasterSwordBeam(player);
                    data.setMasterSwordBeamCooldown(CooldownTimes.MASTER_SWORD_BEAM.cooldown);
                } else {
                    player.sendMessage(ChatColor.RED + "You must wait " + data.getMasterSwordBeamCooldown() / 20 + " seconds until you can use that ability again.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Your health must be full to use the Master Sword's beam attack");
            }

            combo.resetCombo();
        }
    }

    private void triggerMasterSwordBeam(Player player) {
        Location startLoc = player.getEyeLocation();
        // We need to clone() this location, because we will add() to it later.
        Location particleLoc = startLoc.clone();
        World world = startLoc.getWorld();
        // dir is the Vector direction (offset from 0,0,0) the player is facing in 3D space
        Vector dir = startLoc.getDirection();
        Vector vecOffset = dir.clone().multiply(.5);

        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:excalibur.ability.blast master @a " + startLoc.getX() + " " + startLoc.getY() + " " + startLoc.getZ() + " 1 1");

        int maxBeamLength = 30;

        for (int i = 0; i < maxBeamLength; i++) {
            if (!( particleLoc.getBlock().getType() == Material.AIR || particleLoc.getBlock().getType() == Material.WATER)) {
                break;
            }
            int get = 0;
            List<Entity> entities = new ArrayList<>(world.getNearbyEntities(particleLoc, 5, 5, 5));
            while (get < entities.size()) {
                if (get >= entities.size()) {
                    break;
                }
                Entity entity = entities.get(get);
                if (entity instanceof LivingEntity && entity != player) {
                    Vector particleMinVector = new Vector(
                            particleLoc.getX() - 0.25,
                            particleLoc.getY() - 0.25,
                            particleLoc.getZ() - 0.25);
                    Vector particleMaxVector = new Vector(
                            particleLoc.getX() + 0.25,
                            particleLoc.getY() + 0.25,
                            particleLoc.getZ() + 0.25);

                    if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                        //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                        //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                        ((Damageable) entity).damage(6, player);
                        maxBeamLength = 0;
                        break;
                    }
                }
                get++;
            }

            particleLoc.add(vecOffset);

            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(79, 217, 255), 5);
            world.spawnParticle(Particle.REDSTONE, particleLoc.getX(), particleLoc.getY(), particleLoc.getZ(), 0, 0, 0, 0, dust);
        }
    }

    private void handleGalaxySwordSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();
        ClickCombo combo = shortTermStorages.clickComboHashMap.get(player.getUniqueId());
        if (combo.getCombo().equalsIgnoreCase("[ R L L ]")) {
            if (data.getStarRainCooldown() <= 0) {
                triggerStarRain(player);
                data.setStarRainCooldown(CooldownTimes.STAR_RAIN.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getStarRainCooldown() / 20 + " seconds until you can use that ability again.");
            }
            combo.resetCombo();
        }
    }

    private void handleHollowScytheSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();
        ClickCombo combo = shortTermStorages.clickComboHashMap.get(player.getUniqueId());
        if (combo.getCombo().equalsIgnoreCase("[ R L L ]")) {
            if (data.getDeathsCallCoolDown() <= 0) {
                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " run function excalibur:ability/heal");
                triggerDeathsCall(player);
                data.setDeathsCallCooldown(CooldownTimes.DEATHS_CALL.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getDeathsCallCoolDown() / 20 + " seconds until you can use that ability again.");
            }
            combo.resetCombo();
        }
    }

    private void triggerStarRain(Player player) {
        Location abovePlayer = player.getLocation().clone().add(0, 6, 0);
        int stars = r.nextInt(4) + 50;
        for (int i = 0; i < stars; i++) {
            int xrand = r.nextInt(11) - 5;
            int zrand = r.nextInt(11) - 5;
            Location particleSpawn = abovePlayer.clone();
            particleSpawn.add(xrand, 0, zrand);
            new BukkitRunnable() {
                float trail = 0;

                @Override
                public void run() {
                    if (trail >= 8) {
                        if (particleSpawn.getBlock().getType() != Material.AIR) {
                            particleSpawn.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, particleSpawn, 1);
                            particleSpawn.getWorld().playSound(particleSpawn, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                        }
                            this.cancel();
                    }

                    for (Entity entity : particleSpawn.getWorld().getNearbyEntities(particleSpawn, 1, 1, 1)) {
                        if (entity instanceof LivingEntity && entity != player) {
                            Vector particleMinVector = new Vector(
                                    particleSpawn.getX() - 1,
                                    particleSpawn.getY() - 1,
                                    particleSpawn.getZ() - 1);
                            Vector particleMaxVector = new Vector(
                                    particleSpawn.getX() + 1,
                                    particleSpawn.getY() + 1,
                                    particleSpawn.getZ() + 1);
                            if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                                //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                                //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                                ((Damageable) entity).damage(19, player);
                                particleSpawn.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, particleSpawn, 1);
                                particleSpawn.getWorld().playSound(particleSpawn, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                                this.cancel();
                            }
                        }
                    }

                    particleSpawn.getWorld().spawnParticle(Particle.SMOKE_NORMAL, particleSpawn.clone().add(0, -.25, 0), 0, 0, 0, 0, 0);
                    particleSpawn.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, particleSpawn, 0, 0, 0, 0, 0);

                    particleSpawn.add(0, -.2, 0);

                    trail+=.2;
                }
            }.runTaskTimer(Rpgcore.getInstance(), 0, 1);
        }
    }

    private void triggerDeathsCall(Player player) {

        Location abovePlayer = player.getLocation().clone().add(0, -1, 0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 0);
        int deathRays = r.nextInt(4) + 50;
        for (int i = 0; i < deathRays; i++) {
            int xrand = r.nextInt(11) - 5;
            int zrand = r.nextInt(11) - 5;
            Location particleSpawn = abovePlayer.clone();
            particleSpawn.add(xrand, 0, zrand);
            new BukkitRunnable() {
                float trail = 0;

                @Override
                public void run() {
                    if (trail >= 6) {
                        this.cancel();
                    }

                    for (Entity entity : particleSpawn.getWorld().getNearbyEntities(particleSpawn, 1, 1, 1)) {
                        if (entity instanceof LivingEntity && entity != player) {
                            Vector particleMinVector = new Vector(
                                    particleSpawn.getX() - 1,
                                    particleSpawn.getY() - 1,
                                    particleSpawn.getZ() - 1);
                            Vector particleMaxVector = new Vector(
                                    particleSpawn.getX() + 1,
                                    particleSpawn.getY() + 1,
                                    particleSpawn.getZ() + 1);
                            if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                                //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                                //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                                ((Damageable) entity).damage(19, player);
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 1));
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 1));
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 5));
                                //particleSpawn.getWorld().playSound(particleSpawn, Sound.ENTITY_WOLF_HOWL, 1, 0);

                                this.cancel();
                            }
                        }
                    }

                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 10);
                    particleSpawn.getWorld().spawnParticle(Particle.REDSTONE, particleSpawn.getX(), particleSpawn.getY(), particleSpawn.getZ(), 0, 0, 0, 0, dust);

                    particleSpawn.add(0, .2, 0);

                    trail+=.2;
                }
            }.runTaskTimer(Rpgcore.getInstance(), 0, 1);
        }

        /*Location startLoc = player.getEyeLocation();
        // We need to clone() this location, because we will add() to it later.
        Location particleLoc = startLoc.clone();
        World world = startLoc.getWorld();
        // dir is the Vector direction (offset from 0,0,0) the player is facing in 3D space
        Vector dir = startLoc.getDirection();
        Vector vecOffset = dir.clone().multiply(.5);

        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:excalibur.ability.blast master @a " + startLoc.getX() + " " + startLoc.getY() + " " + startLoc.getZ() + " 1 1");

        int maxBeamLength = 30;

        for (int i = 0; i < maxBeamLength; i++) {
            if (particleLoc.getBlock().getType() != Material.AIR) {
                break;
            }
            int get = 0;
            List<Entity> entities = new ArrayList<>(world.getNearbyEntities(particleLoc, 5, 5, 5));
            while (get < entities.size()) {
                if (get >= entities.size()) {
                    break;
                }
                Entity entity = entities.get(get);
                if (entity instanceof LivingEntity && entity != player) {
                    Vector particleMinVector = new Vector(
                            particleLoc.getX() - 0.25,
                            particleLoc.getY() - 0.25,
                            particleLoc.getZ() - 0.25);
                    Vector particleMaxVector = new Vector(
                            particleLoc.getX() + 0.25,
                            particleLoc.getY() + 0.25,
                            particleLoc.getZ() + 0.25);

                    if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                        //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                        //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                        ((Damageable) entity).damage(5, player);
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 1));
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 1));
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 5));
                        maxBeamLength = 0;
                        break;
                    }
                }
                get++;
            }

            particleLoc.add(vecOffset);

            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);
            world.spawnParticle(Particle.REDSTONE, particleLoc.getX(), particleLoc.getY(), particleLoc.getZ(), 0, 0, 0, 0, dust);
        }*/

    }

    private void handleLichStaffSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();
        ClickCombo combo = shortTermStorages.clickComboHashMap.get(player.getUniqueId());

        if (combo.getCombo().equalsIgnoreCase("[ R L L ]")) {
            if (data.getLifeDrainCooldown() <= 0) {
                triggerLifeDrain(player);
                data.setLifeDrainCooldown(CooldownTimes.LIFE_DRAIN.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getLifeDrainCooldown() / 20 + " seconds until you can use that ability again.");
            }
            combo.resetCombo();
        }
    }

    private void triggerLifeDrain(Player player) {
        Location startLoc = player.getEyeLocation();
        // We need to clone() this location, because we will add() to it later.
        Location particleLoc = startLoc.clone();
        World world = startLoc.getWorld();
        // dir is the Vector direction (offset from 0,0,0) the player is facing in 3D space
        Vector dir = startLoc.getDirection();
        Vector vecOffset = dir.clone().multiply(.5);

        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:excalibur.ability.blast master @a " + startLoc.getX() + " " + startLoc.getY() + " " + startLoc.getZ() + " 1 1");

        int maxBeamLength = 30;

        for (int i = 0; i < maxBeamLength; i++) {

            if (particleLoc.getBlock().getType() != Material.AIR) {
                break;
            }

            int get = 0;
            List<Entity> entities = new ArrayList<>(world.getNearbyEntities(particleLoc, 5, 5, 5));
            while (get < entities.size()) {
                if (get >= entities.size()) {
                    break;
                }
                Entity entity = entities.get(get);
                if (entity instanceof LivingEntity && entity != player) {
                    Vector particleMinVector = new Vector(
                            particleLoc.getX() - 0.25,
                            particleLoc.getY() - 0.25,
                            particleLoc.getZ() - 0.25);
                    Vector particleMaxVector = new Vector(
                            particleLoc.getX() + 0.25,
                            particleLoc.getY() + 0.25,
                            particleLoc.getZ() + 0.25);

                    if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                        //world.spawnParticle(Particle.FLASH, particleLoc, 0);
                        //world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                        ((Damageable) entity).damage(4, player);
                        player.setHealth(Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth() + 2));
                        maxBeamLength = 0;
                        break;
                    }
                }
                get++;
            }

            particleLoc.add(vecOffset);
            world.spawnParticle(Particle.VILLAGER_HAPPY, particleLoc, 0);
            maxBeamLength--;
        }
    }


    private void handleExcaliburSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();
        ClickCombo combo = shortTermStorages.clickComboHashMap.get(player.getUniqueId());
        if (combo.getCombo().equalsIgnoreCase("[ R L L ]")) {
            if (data.getCamelotsMightCooldown() <= 0) {
                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " run function excalibur:ability/heal");
                triggerCamelotsMight(player);
                data.setCamelotsMightCooldown(CooldownTimes.CAMELOTS_MIGHT.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getCamelotsMightCooldown() / 20 + " seconds until you can use that ability again.");
            }
            combo.resetCombo();
        }
        if (combo.getCombo().equalsIgnoreCase("[ R L R ]")) {
            if (data.getStrikeBoltCooldown() <= 0) {
                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " run function excalibur:ability/blast");
                triggerStrikeBolt(player);
                data.setStrikeBoltCooldownTime(CooldownTimes.STRIKE_BOLT.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getStrikeBoltCooldown() / 20 + " seconds until you can use that ability again.");
            }
            combo.resetCombo();
        }
    }

    private void triggerCamelotsMight(Player player) {
        FireworkEffect fe = FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.YELLOW).build();
        new InstantFirework(fe, player.getLocation());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:excalibur.ability.camelot master @a " + player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ() + " 1 1");

        for (Entity e : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity) {
                if (e instanceof Player) {
                    e.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "For Camelot!");
                    continue;
                }

                if (e instanceof Villager || e instanceof WanderingTrader || e instanceof Cow ||
                        e instanceof Tameable || e instanceof Wolf || e instanceof Ocelot || e instanceof Horse ||
                        e instanceof Pig || e instanceof Chicken || e instanceof IronGolem || e instanceof Snowman ||
                        e instanceof Llama || e instanceof Parrot || e instanceof Bat || e instanceof Squid || e instanceof Dolphin ||
                        e instanceof Salmon || e instanceof TropicalFish || e instanceof Strider || e instanceof PufferFish || e instanceof Mule ||
                        e instanceof ZombieHorse) {
                    continue;
                }

                e.getLocation().getWorld().strikeLightningEffect(e.getLocation());

                ((Damageable) e).damage(30, player);
            }
        }
    }

    private void triggerStrikeBolt(Player player) {
        Location startLoc = player.getEyeLocation();
        // We need to clone() this location, because we will add() to it later.
        Location particleLoc = startLoc.clone();
        World world = startLoc.getWorld();
        // dir is the Vector direction (offset from 0,0,0) the player is facing in 3D space
        Vector dir = startLoc.getDirection();
        Vector vecOffset = dir.clone().multiply(.5);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:excalibur.ability.blast master @a " + startLoc.getX() + " " + startLoc.getY() + " " + startLoc.getZ() + " 1 1");

        int maxBeamLength = 30;


        for (int i = 0; i < maxBeamLength; i++) {
            if (particleLoc.getBlock().getType() != Material.AIR) {
                break;
            }

            int get = 0;
            List<Entity> entities = new ArrayList<>(world.getNearbyEntities(particleLoc, 5, 5, 5));
            while (get < entities.size()) {
                if (get >= entities.size()) {
                    break;
                }
                Entity entity = entities.get(get);
                if (entity instanceof LivingEntity && entity != player) {
                    Vector particleMinVector = new Vector(particleLoc.getX() - 0.25, particleLoc.getY() - 0.25, particleLoc.getZ() - 0.25);
                    Vector particleMaxVector = new Vector(particleLoc.getX() + 0.25, particleLoc.getY() + 0.25, particleLoc.getZ() + 0.25);

                    if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                        world.spawnParticle(Particle.FLASH, particleLoc, 0);
                        world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                        ((Damageable) entity).damage(8, player);
                        FireworkEffect fe = FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.AQUA).build();
                        new InstantFirework(fe, entity.getLocation());
                        maxBeamLength = 0;
                        break;
                    }
                }
                get++;
            }

            particleLoc.add(vecOffset);
            world.spawnParticle(Particle.SOUL_FIRE_FLAME, particleLoc, 0);
        }

    }


    @EventHandler
    public void onTriggerSpecialAttackDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) {
            return;
        }
        NBTItem nbti = new NBTItem(item);
        if (nbti.hasKey("ghost")) {
            handleGhostSaberPassive(event);
        } else if (nbti.hasKey("coral") && nbti.getItem().getType() == Material.DIAMOND_SWORD) {
            handleCoralSwordPassives(event);
        } else if (nbti.hasKey("coral") && nbti.getItem().getType() == Material.DIAMOND_AXE) {
            handleCoralAxePassives(event);
        } else if(nbti.hasKey("swordfish")) {
            handleSwordFishSwordPassives(event);
        } else if(nbti.hasKey("keyblade")) {
            handleKeyBladePassives(event);
        }
    }

    private void handleKeyBladePassives(EntityDamageByEntityEvent event) {

        RPGPlayerData data = configloader.getDataByUUID(event.getDamager().getUniqueId());
        KEYBLADE_MODE mode = data.getKeybladeMode();
        if(mode == KEYBLADE_MODE.FIRE) {
            event.getEntity().setFireTicks(300);
        } else if(mode == KEYBLADE_MODE.ICE) {
            if(event.getEntity() instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) event.getEntity();
                living.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 2));
            }
        } else if(mode == KEYBLADE_MODE.LIGHTNING) {
            event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
            event.setDamage(event.getDamage() * 2);
        }
    }

    private void handleSwordFishSwordPassives(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if(attacker.getLocation().getBlock().getType() == Material.WATER) {
            event.setDamage(event.getDamage() * 2.2);
        }
    }

    private void handleCoralSwordPassives(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if (attacker.getLocation().getBlock().getType() == Material.WATER) {
            event.setDamage(event.getDamage() * 2);
        }
    }

    private void handleCoralAxePassives(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if (attacker.getLocation().getBlock().getType() == Material.WATER) {
            event.setDamage(event.getDamage() * 2);
        }
    }

    private void handleGhostSaberPassive(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        Location attackerLoc = player.getLocation();
        Location victimLoc = event.getEntity().getLocation();
        if (attackerLoc.distance(victimLoc) <= 1.5) {
            event.setDamage(event.getDamage() * 3);
            player.getWorld().playSound(attackerLoc, Sound.ENTITY_GHAST_SCREAM, 1f, 0f);
        }
    }

    @EventHandler
    public void onPlayerConsumeFood(FoodLevelChangeEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        handleGrimArmorPassive(event);
    }

    private void handleGrimArmorPassive(FoodLevelChangeEvent event) {
        ItemStack item = event.getItem();
        if (item.getType() == Material.ROTTEN_FLESH) {
            NBTItem helmet = new NBTItem(event.getEntity().getInventory().getHelmet());
            NBTItem chestplate = new NBTItem(event.getEntity().getInventory().getChestplate());
            NBTItem leggings = new NBTItem(event.getEntity().getInventory().getLeggings());
            NBTItem boots = new NBTItem(event.getEntity().getInventory().getBoots());

            if (helmet.hasKey("grim") && chestplate.hasKey("grim") && leggings.hasKey("grim") && boots.hasKey("grim")) {

                event.setFoodLevel(Math.min(event.getFoodLevel() + 4, 20));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getEntity().removePotionEffect(PotionEffectType.HUNGER);
                    }
                }.runTaskLater(Rpgcore.getInstance(), 1);
            }

        }
    }
}
