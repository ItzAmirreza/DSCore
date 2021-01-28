package me.prismskey.rpgcore.Enums;

public enum CooldownTimes {
    CAMELOTS_MIGHT(20 * 60 * 6),
    STRIKE_BOLT(20 * 10 * 1),
    LIFE_DRAIN(20 * 2),
    DEATHS_CALL(20 * 30),
    STAR_RAIN(20 * 30),
    MASTER_SWORD_BEAM(20 * 1);

    public final int cooldown;

    CooldownTimes(int cooldown) {
        this.cooldown = cooldown;
    }
}
