package gg.warcraft.legends.api.entity.player.service;

import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import java.util.UUID;

public interface LegendsPlayerCommandService extends PlayerCommandService {

    void setTutorialStep(UUID playerId, TutorialStep step);

    void setSpecialization(UUID playerId, String specialization);

    void addGold(UUID playerId, int amount);

    void removeGold(UUID playerId, int amount);

    void revokeGold(UUID playerId, int amount);

    void addFavor(UUID playerId, int amount);

    void removeFavor(UUID playerId, int amount);

    void revokeFavor(UUID playerId, int amount);

    void addFragments(UUID playerId, int amount);

    void removeFragments(UUID playerId, int amount);

    void revokeFragments(UUID playerId, int amount);
}
