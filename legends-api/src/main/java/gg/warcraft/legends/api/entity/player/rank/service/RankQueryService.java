package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.team.Team;

public interface RankQueryService {

    Rank getRank(int level);

    Rank getRank(Team team, int favor);
}
