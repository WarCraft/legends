package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.service.SpecializationCommandService;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandServiceDecorator;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.UUID;

public class DefaultLegendsPlayerCommandService implements LegendsPlayerCommandService, PlayerCommandServiceDecorator {
    private final PlayerCommandService playerCommandService;
    private final SpecializationCommandService specializationCommandService;

    @Inject
    public DefaultLegendsPlayerCommandService(PlayerCommandService playerCommandService,
                                              SpecializationCommandService specializationCommandService) {
        this.playerCommandService = playerCommandService;
        this.specializationCommandService = specializationCommandService;
    }

    @Override
    public PlayerCommandService getPlayerCommandService() {
        return playerCommandService;
    }

    @Override
    public void setTeam(UUID playerId, Team team) {
        playerCommandService.setTeam(playerId, team);
    }

    @Override
    public void setData(UUID playerId, String data, String value) {
        playerCommandService.setData(playerId, data, value);
    }

    @Override
    public void setTutorialStep(UUID playerId, TutorialStep step) {
        playerCommandService.setData(playerId, LegendsPlayerData.TUTORIAL_STEP.getName(), step.name());
    }

    @Override
    public void setSpecialization(UUID playerId, String specialization) {
        specializationCommandService.setSpecialization(specialization, playerId);
    }

    @Override
    public void addGold(UUID playerId, int amount) {
        addCurrency(playerId, LegendsCurrency.GOLD, amount);
    }

    @Override
    public void removeGold(UUID playerId, int amount) {
        removeCurrency(playerId, LegendsCurrency.GOLD, amount);
    }

    @Override
    public void revokeGold(UUID playerId, int amount) {
        revokeCurrency(playerId, LegendsCurrency.GOLD, amount);
    }

    @Override
    public void addFavor(UUID playerId, int amount) {
        addCurrency(playerId, LegendsCurrency.FAVOR, amount);
    }

    @Override
    public void removeFavor(UUID playerId, int amount) {
        removeCurrency(playerId, LegendsCurrency.FAVOR, amount);
    }

    @Override
    public void revokeFavor(UUID playerId, int amount) {
        revokeCurrency(playerId, LegendsCurrency.FAVOR, amount);
    }

    @Override
    public void addFragments(UUID playerId, int amount) {
        addCurrency(playerId, LegendsCurrency.FRAGMENTS, amount);
    }

    @Override
    public void removeFragments(UUID playerId, int amount) {
        removeCurrency(playerId, LegendsCurrency.FRAGMENTS, amount);
    }

    @Override
    public void revokeFragments(UUID playerId, int amount) {
        revokeCurrency(playerId, LegendsCurrency.FRAGMENTS, amount);
    }
}
