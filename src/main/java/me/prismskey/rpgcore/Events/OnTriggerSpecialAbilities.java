package me.prismskey.rpgcore.Events;

import de.tr7zw.nbtapi.NBTItem;
import me.prismskey.rpgcore.Enums.CooldownTimes;
import me.prismskey.rpgcore.DataManager.RPGPlayerData;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.VFireworks.InstantFirework;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class OnTriggerSpecialAbilities implements Listener {

    @EventHandler
    public void onTriggerSpecialAttackInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        RPGPlayerData data = Rpgcore.instance.getDataByUUID(player.getUniqueId());
        //Rpgcore.instance.getLogger().info("onTriggerSpecial");

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) {
            return;
        }
        NBTItem nbti = new NBTItem(item);
        if (nbti.hasKey("Excalibur")) {
            handleExcaliburSpecials(event, data);
        } else if(nbti.hasKey("lich")) {
            handleLichStaffSpecials(event, data);
        } else if(nbti.hasKey("Hollowscythe")) {
            handleHollowScytheSpecials(event, data);
        }
    }

    private void handleHollowScytheSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            //Rpgcore.instance.getLogger().info("onTriggerSpecial2");
            if (data.getDeathsCallCoolDown() <= 0) {
                //Rpgcore.instance.getLogger().info("onTriggerSpecial3");
                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " run function excalibur:ability/heal");
                triggerDeathsCall(player);
                data.setDeathsCallCooldown(CooldownTimes.DEATHS_CALL.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getDeathsCallCoolDown() / 20 + " seconds until you can use that ability again.");
            }
        }
    }

    private void triggerDeathsCall(Player player) {
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
            for (Entity entity : world.getNearbyEntities(particleLoc, 5, 5, 5)) {
                if (entity instanceof LivingEntity) {
                    if (entity == player) {
                        continue;
                    }

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
                        return;
                    }
                }
            }

            particleLoc.add(vecOffset);

            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);
            world.spawnParticle(Particle.REDSTONE, particleLoc.getX(), particleLoc.getY(), particleLoc.getZ(), 0, 0, 0, 0, dust);
        }
    }

    private void handleLichStaffSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            //Rpgcore.instance.getLogger().info("onTriggerSpecial2");
            if (data.getLifeDrainCooldown() <= 0) {
                //Rpgcore.instance.getLogger().info("onTriggerSpecial3");
                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " run function excalibur:ability/heal");
                triggerLifeDrain(player);
                data.setLifeDrainCooldown(CooldownTimes.LIFE_DRAIN.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getLifeDrainCooldown() / 20 + " seconds until you can use that ability again.");
            }
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
            for (Entity entity : world.getNearbyEntities(particleLoc, 5, 5, 5)) {
                if (entity instanceof LivingEntity) {
                    if (entity == player) {
                        continue;
                    }

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
                        ((Damageable) entity).damage(2, player);
                        player.setHealth(Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth() + 1));
                        return;
                    }
                }
            }

            particleLoc.add(vecOffset);
            world.spawnParticle(Particle.SOUL, particleLoc, 0);
        }
    }

    private void handleExcaliburSpecials(PlayerInteractEvent event, RPGPlayerData data) {
        Player player = event.getPlayer();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.isSneaking()) {
            //Rpgcore.instance.getLogger().info("onTriggerSpecial2");
            if (data.getCamelotsMightCooldown() <= 0) {
                //Rpgcore.instance.getLogger().info("onTriggerSpecial3");
                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " run function excalibur:ability/heal");
                triggerCamelotsMight(player);
                data.setCamelotsMightCooldown(CooldownTimes.CAMELOTS_MIGHT.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getCamelotsMightCooldown() / 20 + " seconds until you can use that ability again.");
            }
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && !player.isSneaking()) {
            //Rpgcore.instance.getLogger().info("onTriggerSpecial4");
            if (data.getStrikeBoltCooldown() <= 0) {
                //Rpgcore.instance.getLogger().info("onTriggerSpecial5");
                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + player.getName() + " run function excalibur:ability/blast");
                triggerStrikeBolt(player);
                data.setStrikeBoltCooldownTime(CooldownTimes.STRIKE_BOLT.cooldown);
            } else {
                player.sendMessage(ChatColor.RED + "You must wait " + data.getStrikeBoltCooldown() / 20 + " seconds until you can use that ability again.");
            }
        }
    }

    private void triggerCamelotsMight(Player player) {

        FireworkEffect fe = FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.YELLOW).build();
        new InstantFirework(fe, player.getLocation());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:excalibur.ability.camelot master @a " + player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ() + " 1 1");

        for (Entity e : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity) {
                if (e instanceof Player) {
                    ((Player) e).sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "For Camelot!");
                    continue;
                }

                if (e instanceof Villager || e instanceof WanderingTrader || e instanceof Cow ||
                        e instanceof Tameable || e instanceof Wolf || e instanceof Ocelot || e instanceof Horse
                        || e instanceof Pig || e instanceof Chicken || e instanceof IronGolem || e instanceof Snowman
                        || e instanceof Llama || e instanceof Parrot || e instanceof Bat || e instanceof Squid || e instanceof Dolphin ||
                        e instanceof Salmon || e instanceof TropicalFish || e instanceof Strider || e instanceof PufferFish || e instanceof Mule ||
                        e instanceof ZombieHorse || e instanceof Horse) {

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
            for (Entity entity : world.getNearbyEntities(particleLoc, 5, 5, 5)) {
                if (entity instanceof LivingEntity) {
                    if (entity == player) {
                        continue;
                    }

                    Vector particleMinVector = new Vector(
                            particleLoc.getX() - 0.25,
                            particleLoc.getY() - 0.25,
                            particleLoc.getZ() - 0.25);
                    Vector particleMaxVector = new Vector(
                            particleLoc.getX() + 0.25,
                            particleLoc.getY() + 0.25,
                            particleLoc.getZ() + 0.25);

                    if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                        world.spawnParticle(Particle.FLASH, particleLoc, 0);
                        world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                        ((Damageable) entity).damage(8, player);
                        FireworkEffect fe = FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.AQUA).build();
                        new InstantFirework(fe, entity.getLocation());
                        return;
                    }
                }
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
        }
    }

    private void handleGhostSaberPassive(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        Location attackerLoc = player.getLocation();
        Location victimLoc = event.getEntity().getLocation();
        if (attackerLoc.distance(victimLoc) <= 1.5) {
            event.setDamage(event.getDamage() * 3);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:entity.ghast.scream hostile @a " + attackerLoc.getX() + " " + attackerLoc.getY() + " " + attackerLoc.getZ() + " 1 0");
        }
    }
}
