package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;

public interface RankQueryService {

    int getRankCount();

    Rank getRank(int level);

    Rank getRank(LegendsTeam team, int favor);
}
