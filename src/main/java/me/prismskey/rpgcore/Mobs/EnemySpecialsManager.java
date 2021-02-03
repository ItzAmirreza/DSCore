package me.prismskey.rpgcore.Mobs;

import me.prismskey.rpgcore.Enums.MobAbilityCoolDownTimes;
import me.prismskey.rpgcore.Enums.MobSpecialAttackCooldownTimes;
import me.prismskey.rpgcore.Rpgcore;
import me.prismskey.rpgcore.Utils.APIUsages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class EnemySpecialsManager implements Listener {

    private final HashMap<String, Integer> enemyMasterAttackCooldowns;
    private final HashMap<String, Integer> enemySpecialCooldowns;
    private final HashSet<UUID> processedEntities;
    private final Random random;

    public EnemySpecialsManager() {
        enemySpecialCooldowns = new HashMap<>();
        enemyMasterAttackCooldowns = new HashMap<>();
        random = new Random();

        processedEntities = new HashSet<>();

        new BukkitRunnable() {
            @Override
            public void run() {


                Iterator it = enemyMasterAttackCooldowns.keySet().iterator();
                while(it.hasNext()) {
                    String key = (String) it.next();
                    enemyMasterAttackCooldowns.merge(key, -1, Integer::sum);

                    try {
                        if( ((LivingEntity) Bukkit.getEntity(UUID.fromString(key))).isDead()) {
                            it.remove();
                        }
                    } catch (NullPointerException e) {
                        it.remove();
                    }

                }

                it = enemySpecialCooldowns.keySet().iterator();
                while(it.hasNext()) {
                    String key = (String) it.next();
                    enemySpecialCooldowns.merge(key, -1, Integer::sum);

                    try {
                        if( ((LivingEntity) Bukkit.getEntity(UUID.fromString(key.split(":")[0]))).isDead()) {
                            it.remove();
                        }
                    } catch (NullPointerException e) {
                        it.remove();
                    }
                }

                processedEntities.clear();
                for(Player player: Bukkit.getOnlinePlayers()) {
                    processEnemiesAroundPlayer(player);
                }
            }
        }.runTaskTimer(Rpgcore.instance, 0, 1);
    }

    private void processEnemiesAroundPlayer(Player player) {
        for(Entity e: player.getNearbyEntities(20, 20 , 20)) {
            if(e instanceof LivingEntity) {
                if(processedEntities.contains(e.getUniqueId())) {
                    continue;
                } else {
                    processedEntities.add(e.getUniqueId());
                }
                chooseAttackForEntity((LivingEntity) e);
            }
        }
    }

    private boolean checkMasterCooldown(LivingEntity e) {
        Integer value = null;
        if(APIUsages.hasMobNBT(e, "hollow")) {
            value = enemyMasterAttackCooldowns.putIfAbsent(e.getUniqueId().toString(), 0);
        } else if(APIUsages.hasMobNBT(e,"ghost")) {
            value = enemyMasterAttackCooldowns.putIfAbsent(e.getUniqueId().toString(), 0);
        } else if(APIUsages.hasMobNBT(e, "lich")) {
            value = enemyMasterAttackCooldowns.putIfAbsent(e.getUniqueId().toString(), 0);
        } else if(APIUsages.hasMobNBT(e, "fire")) {
            value = enemyMasterAttackCooldowns.putIfAbsent(e.getUniqueId().toString(), 0);
        } else if(APIUsages.hasMobNBT(e, "pharaoh")) {
            value = enemyMasterAttackCooldowns.putIfAbsent(e.getUniqueId().toString(), 0);
        }

        //Rpgcore.getInstance().getLogger().info("value: " + value);

        if(value == null || value > 0) {
            return false;
        }
        return true;
    }

    private void chooseAttackForEntity(LivingEntity e) {

        if(!checkMasterCooldown(e)) {
            return;
        }

        ArrayList<String> attacks = new ArrayList<>();
        if(APIUsages.hasMobNBT(e, "lifeDrainBeam")) {
            attacks.add("lifeDrainBeam");
        }
        if(APIUsages.hasMobNBT(e, "deathsCall")) {
            attacks.add("deathsCall");
        }
        if(APIUsages.hasMobNBT(e, "void")) {
            attacks.add("void");
        }
        if(APIUsages.hasMobNBT(e, "undeadSummoner")) {
            attacks.add("undeadSummoner");
        }
        if(APIUsages.hasMobNBT(e, "findNewTarget")) {
            attacks.add("findNewTarget");
        }
        if(APIUsages.hasMobNBT(e, "rush")) {
            attacks.add("rush");
        }
        if(APIUsages.hasMobNBT(e, "magiGuard")) {
            attacks.add("magiGuard");
        }
        if(APIUsages.hasMobNBT(e, "meteor")) {
            attacks.add("meteor");
        }
        if(APIUsages.hasMobNBT(e, "flameWave")) {
            attacks.add("flameWave");
        }
        if(APIUsages.hasMobNBT(e, "egyptianMinions")) {
            attacks.add("egyptianMinions");
        }
        if(APIUsages.hasMobNBT(e, "slowBeam")) {
            attacks.add("slowBeam");
        }
        if(APIUsages.hasMobNBT(e, "affliction")) {
            attacks.add("affliction");
        }

        if(attacks.size() > 0) {
            String randomAttack = attacks.get(random.nextInt(attacks.size()));
            boolean attacked = processEnemyAttack(e, randomAttack);

            if (attacked) {
                resetMasterCooldownForEntity(e);
            }
        }

    }

    private void resetMasterCooldownForEntity(LivingEntity e) {
        if(APIUsages.hasMobNBT(e,"hollow")) {
            enemyMasterAttackCooldowns.put(e.getUniqueId().toString(), MobSpecialAttackCooldownTimes.HOLLOWGHAST.cooldown);
        } else if(APIUsages.hasMobNBT(e,"ghost")) {
            enemyMasterAttackCooldowns.put(e.getUniqueId().toString(), MobSpecialAttackCooldownTimes.GHOST.cooldown);
        } else if(APIUsages.hasMobNBT(e,"lich")) {
            enemyMasterAttackCooldowns.put(e.getUniqueId().toString(), MobSpecialAttackCooldownTimes.NAMELESS_ONE.cooldown);
        } else if(APIUsages.hasMobNBT(e, "fire")) {
            enemyMasterAttackCooldowns.put(e.getUniqueId().toString(), MobSpecialAttackCooldownTimes.FIRE_ELEMENTAL.cooldown);
        } else if(APIUsages.hasMobNBT(e, "pharaoh")) {
            enemyMasterAttackCooldowns.put(e.getUniqueId().toString(), MobSpecialAttackCooldownTimes.PHARAOH.cooldown);
        }
    }


    private boolean processEnemyAttack(LivingEntity e, String type) {
        String keyString = e.getUniqueId().toString() + ":" + type;
        enemySpecialCooldowns.putIfAbsent(keyString, 0);

        //returns null if keyString is not in the map
        boolean usedSpecial = true;
        switch (type) {
            case "lifeDrainBeam":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new LifeDrainBeam().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.LIFE_DRAIN.cooldown);
                }
                break;
            case "deathsCall":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new DeathsCall().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.DEATH_CALL.cooldown);
                }
                break;
            case "void":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new VoidTarget().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.VOID.cooldown);
                }
                break;
            case "undeadSummoner":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    int countedEntities = 0;
                    for(Entity ent: e.getNearbyEntities(10, 10, 10)) {
                        if(ent instanceof Player) {
                            continue;
                        }
                        if(!(ent instanceof LivingEntity)) {
                            continue;
                        }
                        countedEntities++;
                    }
                    if(countedEntities < 30) {
                        new UndeadSummoner().start(e);
                        enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.UNDEAD_SUMMONER.cooldown);
                    }
                }
                break;
            case "findNewTarget":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new FindNewTarget().start((Creature) e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.FIND_NEW_TARGET.cooldown);
                }
                break;
            case "rush":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new Rush().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.RUSH.cooldown);
                }
                break;
            case "magiGuard":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new MagiGuard().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.MAGIGUARD.cooldown);
                }
                break;
            case "meteor":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new Meteor().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.METEOR.cooldown);
                }
                break;
            case "flameWave":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new FlameWave().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.FLAME_WAVE.cooldown);
                }
                break;
            case "egyptianMinions":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new EgyptianMinionSummoner().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.EGYPTIAN_MINION_SUMMONER.cooldown);
                }
                break;
            case "slowBeam":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new SlowBeam().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.SLOW_BEAM.cooldown);
                }
                break;
            case "affliction":
                if (enemySpecialCooldowns.get(keyString) <= 0) {
                    new Affliction().start(e);
                    enemySpecialCooldowns.put(keyString, MobAbilityCoolDownTimes.AFFLICTION.cooldown);
                }
                break;
            default:
                usedSpecial = false;
                break;
        }
        return usedSpecial;
    }

    public static Player getRandomPlayerTarget(LivingEntity ent) {
        List<Entity> nearbyPlayers = ent.getNearbyEntities(20, 20, 20)
                .stream()
                .filter(e -> e instanceof Player)
                .collect(Collectors.toList());

        if(nearbyPlayers.isEmpty()) return null;

        return (Player) nearbyPlayers.get(new Random().nextInt(nearbyPlayers.size()));
    }

    @EventHandler
    public void onEnemyDie(EntityDeathEvent event) {
        enemySpecialCooldowns.keySet().removeIf(keyString -> keyString.contains(event.getEntity().getUniqueId().toString()));
    }
}
