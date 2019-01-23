package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;

import java.util.NavigableMap;

public interface RankRepository {

    NavigableMap<Integer, Rank> getRanks(LegendsTeam team);

    void save(LegendsTeam team, NavigableMap<Integer, Rank> ranks);
}
