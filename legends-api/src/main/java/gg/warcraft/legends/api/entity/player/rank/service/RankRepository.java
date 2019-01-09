package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;

import java.util.Map;
import java.util.NavigableMap;

public interface RankRepository {

    Rank getRank(int level);

    NavigableMap<Integer, Rank> getRanks(LegendsTeam team);

    int getMaxFavor();

    void save(Map<Integer, Rank> ranksByLevel);

    void save(LegendsTeam team, NavigableMap<Integer, Rank> ranks);

    void save(int maxFavor);
}
