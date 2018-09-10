package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.NavigableMap;

public interface RankRepository {

    NavigableMap<Integer, Rank> getRanks(Team team);

    int getMaxFavor();

    void save(Team team, NavigableMap<Integer, Rank> ranks);

    void save(int maxFavor);
}
