package gg.warcraft.legends.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopProfile {

    private final UUID playerId;
    private final Map<String, Integer> promotional;
    private final Map<String, Integer> miniPets;
    private final Map<String, Integer> decorations;

    public ShopProfile(UUID playerId) {
        this.playerId = playerId;
        this.miniPets = new HashMap<>();
        this.promotional = new HashMap<>();
        this.decorations = new HashMap<>();
    }

    public boolean hasPromotionalUnlocked(String item) {
        return (promotional.containsKey(item) && promotional.get(item) == 1) ||
                ShopService.isGrandfathered(playerId);
    }

    public boolean hasMiniPetUnlocked(String item) {
        return (miniPets.containsKey(item) && miniPets.get(item) == 1) ||
                ShopService.isGrandfathered(playerId);
    }

    public boolean hasDecorationUnlocked(String item) {
        return (decorations.containsKey(item) && decorations.get(item) == 1) ||
                ShopService.isGrandfathered(playerId);
    }

    public void unlockPromotion(String item) {
        promotional.put(item, 1);
    }

    public void lockPromotion(String item) {
        promotional.put(item, 0);
    }

    public void unlockMiniPet(String item) {
        miniPets.put(item, 1);
    }

    public void lockMiniPet(String item) {
        miniPets.put(item, 0);
    }

    public void unlockDecoration(String item) {
        decorations.put(item, 1);
    }

    public void lockDecoration(String item) {
        decorations.put(item, 0);
    }
}
