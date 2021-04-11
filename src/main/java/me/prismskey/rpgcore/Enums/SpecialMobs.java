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
    GHOST("ghost"),
    PHARAOH("boss/pharaoh"),
    MUMMY("mummy"),
    DARK_ELF("elf"),
    DRYAD("dryad"),
    BANSHEE("banshee"),
    BASILISK("basilisk"),
    GOBLIN_SWORD("goblin/swordsman"),
    GOBLIN_CHOPPER("goblin/chopper"),
    GRIFFIN("griffin"),
    HARPY("harpy"),
    ILLUSIONER("illusioner"),
    LIZARD_GUY("lizard_guy"),
    NAGA("naga"),
    PHOENIX("phoenix"),
    SCARAB("scarab"),
    URUK_HAI("uruk-hai"),
    WEREWOLF("werewolf");









    private String name;
    private SpecialMobs(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


}
