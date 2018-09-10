package gg.warcraft.legends.api.shop;

public enum MiniPetId {
    // Epic mini pets
    BAT("BAT_MINI_PET"),
    CHICKEN("CHICKEN_MINI_PET"),
    COW("COW_MINI_PET"),
    LLAMA("LLAMA_MINI_PET"),  // FIXME what about variants
    OCELOT("OCELOT_MINI_PET"),
    PIG("PIG_MINI_PET"),
    RABBIT("RABBIT_MINI_PET"),
    SHEEP("SHEEP_MINI_PET"),// FIXME what about variants
    SILVERFISH("SILVERFISH_MINI_PET"),
    WOLF("WOLF_MINI_PET"),

    // Legendary mini pets
    ENDERMITE("ENDERMITE_MINI_PET"),
    MAGMA_CUBE("MAGMA_CUBE_MINI_PET"),
    MOOSHROOM("MOOSHROOM_MINI_PET"),
    PARROT("PARROT_MINI_PET"),// FIXME what about variants
    WISP("WISP_MINI_PET"),
    POLAR_BEAR("POLAR_BEAR_MINI_PET"),
    SKELETON_HORSE("SKELETON_HORSE_MINI_PET"),
    SLIME("SLIME_MINI_PET"),
    SQUID("SQUID_MINI_PET"),
    VILLAGER("VILLAGER_MINI_PET"),
    ZOMBIE_PIGMEN("ZOMBIE_PIGMEN_MINI_PET"),
    ZOMBIE("ZOMBIE_MINI_PET"), // FIXME what about variants incl villagers

    // Ancient mini pets
    RAINBOW_BUNCHIE("RAINBOW_BUNCHIE_MINI_PET"),
    PARTY_PARROT("PARTY_PARROT_MINI_PET"),
    RAINBOW_SHEEP(""),
    CHICKEN_JOCKEY("");

    private final String id;

    MiniPetId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
