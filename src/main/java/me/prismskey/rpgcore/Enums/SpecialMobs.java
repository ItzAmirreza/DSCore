package me.prismskey.rpgcore.Enums;

public enum SpecialMobs {
    IRON_ZOMBIE("IRON_ZOMBIE"),
    DRAGONIC_PHANTOM("DRAGONIC_PHANTOM"),
    HOLLOWGAST("HOLLOWGAST"),
    KILLER_BUNNY("KILLER_BUNNY"),
    KING_SLIME("KING_SLIME"),
    NAMELESS_ONE("NAMELESS_ONE"),
    EARCH("EARTH"),
    FIRE("FIRE"),
    CHOPPER("CHOPPER"),
    SWRODSMAN("SWORDSMAN"),
    UNSET("UNSET"),
    ADAMANT("ADAMANT"),
    DRAGONSTONE("DRAGONSTONE"),
    MITHRIL("MITHRIL"),
    ORICHALCUM("ORICHALCUM");





    private String name;
    private SpecialMobs(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


}
