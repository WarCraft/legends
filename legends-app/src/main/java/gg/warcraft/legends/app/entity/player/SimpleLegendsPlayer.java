package gg.warcraft.legends.app.entity.player;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.service.SpecializationQueryService;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.PlayerDecorator;
import gg.warcraft.monolith.api.entity.team.Team;

public class SimpleLegendsPlayer implements LegendsPlayer, PlayerDecorator {
    private final RankQueryService rankQueryService;
    private final SpecializationQueryService specializationQueryService;
    private final Player player;

    @Inject
    public SimpleLegendsPlayer(RankQueryService rankQueryService, SpecializationQueryService specializationQueryService,
                               @Assisted Player player) {
        this.rankQueryService = rankQueryService;
        this.specializationQueryService = specializationQueryService;
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getGold() {
        return getCurrency(LegendsCurrency.GOLD.getName());
    }

    @Override
    public int getLifetimeGold() {
        return getLifetimeCurrency(LegendsCurrency.GOLD.getName());
    }

    @Override
    public int getFavor() {
        return getCurrency(LegendsCurrency.FAVOR.getName());
    }

    @Override
    public int getLifetimeFavor() {
        return getLifetimeCurrency(LegendsCurrency.FAVOR.getName());
    }

    @Override
    public int getFragments() {
        return getCurrency(LegendsCurrency.FRAGMENTS.getName());
    }

    @Override
    public int getLifetimeFragments() {
        return getLifetimeCurrency(LegendsCurrency.FRAGMENTS.getName());
    }

    @Override
    public TutorialStep getTutorialStep() {
        try {
            return TutorialStep.valueOf(getData(LegendsPlayerData.TUTORIAL_STEP.getName()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return TutorialStep.START;
        }
    }

    @Override
    public Rank getRank() {
        return rankQueryService.getRank(getTeam(), getLifetimeFavor());
    }

    @Override
    public Specialization getSpecialization() {
        return specializationQueryService.getSpecialization(getId());
    }

    @Override
    public long getSpecializationCooldown() {
        try {
            return Long.parseLong(getData(LegendsPlayerData.SPECIALIZATION_COOLDOWN.getName()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @Override
    public int getRankUpRewardsClaimed() {
        try {
            String rankUpRewardsClaimed = getData(LegendsPlayerData.RANK_UP_REWARDS_CLAIMED.getName());
            if (rankUpRewardsClaimed == null) {
                return 0;
            }
            return Integer.parseInt(rankUpRewardsClaimed);
        } catch (NumberFormatException ex) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public LegendsTeam getTeam() {
        Team team = player.getTeam();
        if (team instanceof LegendsTeam) {
            return (LegendsTeam) team;
        }
        return null;
    }
}
