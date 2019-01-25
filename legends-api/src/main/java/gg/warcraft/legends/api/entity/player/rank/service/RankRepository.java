package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.item.crate.LootCrateType;

import java.util.Map;
import java.util.NavigableMap;

public interface RankRepository {

    NavigableMap<Integer, Rank> getRanks(LegendsTeam team);

    Map<Integer, Map<LootCrateType, Integer>> getRankUpRewards();

    void save(LegendsTeam team, NavigableMap<Integer, Rank> ranks);

    void save(Map<Integer, Map<LootCrateType, Integer>> rankUpRewards);
}
