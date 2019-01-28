package gg.warcraft.legends.api.entity.player;

import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.player.Player;

public interface LegendsPlayer extends Player {

    int getGold();

    int getLifetimeGold();

    int getFavor();

    int getLifetimeFavor();

    int getFragments();

    int getLifetimeFragments();

    TutorialStep getTutorialStep();

    Rank getRank();

    Specialization getSpecialization();

    long getSpecializationCooldown();

    int getRankUpRewardsClaimed();

    @Override
    LegendsTeam getTeam();
}
