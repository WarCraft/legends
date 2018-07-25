package gg.warcraft.legends.api.entity.player.rank;

import gg.warcraft.monolith.api.entity.Team;

public interface RankTable {

    Rank getRankFor(int totalFavor, Team team);
}
