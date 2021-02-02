package me.prismskey.rpgcore.Enums;

public enum MobAbilityCoolDownTimes {
    LIFE_DRAIN(20 * 10),
    DEATH_CALL(20 * 30),
    VOID(20 * 10),
    FLAME_WAVE(20 * 10),
    EARTH_BLAST(20 * 10),
    UNDEAD_SUMMONER(20 * 10),
    FIND_NEW_TARGET(20 * 10),
    RUSH(20 * 10),
    MAGIGUARD(20 * 10);

    public final int cooldown;

    MobAbilityCoolDownTimes(int cooldown) {
        this.cooldown = cooldown;
    }
}
