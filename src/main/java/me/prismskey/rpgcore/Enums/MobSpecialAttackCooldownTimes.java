package me.prismskey.rpgcore.Enums;

public enum MobSpecialAttackCooldownTimes {

    HOLLOWGHAST(20 * 5),
    GHOST(20 * 5),
    NAMELESS_ONE(20 * 5),
    FIRE_ELEMENTAL(20 * 5),
    PHARAOH(20 * 5);


    public final int cooldown;

    MobSpecialAttackCooldownTimes(int cooldown) {
        this.cooldown = cooldown;
    }
}
