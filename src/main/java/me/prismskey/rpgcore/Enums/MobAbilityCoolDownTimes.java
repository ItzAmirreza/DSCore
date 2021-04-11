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
    MAGIGUARD(20 * 10),
    METEOR(20 * 10),
    EGYPTIAN_MINION_SUMMONER(20 * 10),
    SLOW_BEAM(20 * 15),
    AFFLICTION(20 * 30),
    POISON_ARROW(20 * 5),
    BANSHEE_SCREAM(20 * 5),
    STONE_GAZE(20 * 15),
    HARPY_CLAW_ATTACK(20 * 10),
    GRYPHON_CLAW_ATTACK(20 * 10),
    WEREWOLF_CLAW_ATTACK(20 * 10),
    LIZARD_SPIT_ATTACK(20 * 10);

    public final int cooldown;

    MobAbilityCoolDownTimes(int cooldown) {
        this.cooldown = cooldown;
    }
}
