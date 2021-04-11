package me.prismskey.rpgcore.Enums;

public enum MobSpecialAttackCooldownTimes {

    HOLLOWGHAST(20 * 5),
    GHOST(20 * 5),
    NAMELESS_ONE(20 * 5),
    FIRE_ELEMENTAL(20 * 5),
    PHARAOH(20 * 5),
    EARTH_ELEMENTAL(20 * 5),
    DARK_ELF(20 * 5),
    WEREWOLF(20 * 5),
    PHOENIX(20 * 5),
    LIZARDMAN(20 * 5),
    HARPY(20 * 5),
    GRIFFIN(20 * 5),
    BASILISK(20 * 5),
    BANSHEE(20 * 5);


    public final int cooldown;

    MobSpecialAttackCooldownTimes(int cooldown) {
        this.cooldown = cooldown;
    }
}
