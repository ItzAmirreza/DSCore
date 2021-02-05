package me.prismskey.rpgcore.Mobs;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TippedArrow;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class PoisonArrow {
    Random r = new Random();
    public void start(LivingEntity ent) {
        if(((Creature) ent).getTarget() == null) {
            return;
        }
        Vector direction = ent.getLocation().getDirection();
        Vector offset = new Vector(r.nextDouble() * .2, r.nextDouble() * .05, r.nextDouble() * .2);
        Arrow arrow = ent.launchProjectile(Arrow.class, direction.multiply(1.6).add(offset));
        PotionEffect effect = new PotionEffect(PotionEffectType.POISON, 20 * 5, 1 , true, true, true);
        arrow.addCustomEffect(effect, true);
    }
}
