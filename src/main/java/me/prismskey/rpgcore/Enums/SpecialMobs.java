package me.prismskey.rpgcore.Enums;

public enum SpecialMobs {
    DRACONIC_PHANTOM("boss/draconic_phantom"),
    GIANT("boss/giant"),
    HOLLOWGAST("boss/hollowgast"),
    KILLER_BUNNY("boss/killer_bunny"),
    KING_SLIME("boss/king_slime"),
    NAMELESS_ONE("boss/nameless_one"),
    EARTH("elemental/earth"),
    FIRE("elemental/fire"),
    GHOST("ghost");



    private String name;
    private SpecialMobs(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


}
