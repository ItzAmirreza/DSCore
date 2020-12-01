package me.prismskey.rpgcore.rpgcore.mobs;

public enum MobAbilityCoolDownTimes {
    LIFE_DRAIN(20 * 10);

    public final int cooldown;

    MobAbilityCoolDownTimes(int cooldown) {
        this.cooldown = cooldown;
    }
}
