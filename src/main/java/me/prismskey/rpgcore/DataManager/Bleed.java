package me.prismskey.rpgcore.DataManager;

import org.bukkit.entity.LivingEntity;

import java.util.HashSet;

public class Bleed {
    public int bleedTimeRemaining;
    public int bleedDamage;
    public LivingEntity bleedTarget;
    public LivingEntity bleedSource;
    public static HashSet<Bleed> bleeds = new HashSet<>();

    public Bleed(int bleedTime, int bleedDamage, LivingEntity bleedTarget, LivingEntity bleedSource) {
        bleedTimeRemaining = bleedTime;
        this.bleedDamage = bleedDamage;
        this.bleedTarget = bleedTarget;
        this.bleedSource = bleedSource;
    }
}
