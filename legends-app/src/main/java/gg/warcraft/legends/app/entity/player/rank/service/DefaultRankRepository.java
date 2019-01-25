package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.api.item.crate.LootCrateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Singleton
public class DefaultRankRepository implements RankRepository {
    private final Map<String, NavigableMap<Integer, Rank>> ranksByFavor;
    private final Map<Integer, Map<LootCrateType, Integer>> rankUpRewards;

    public DefaultRankRepository() {
        this.ranksByFavor = new HashMap<>();
        this.rankUpRewards = new HashMap<>();
    }

    @Override
    public NavigableMap<Integer, Rank> getRanks(LegendsTeam team) {
        if (team != null) {
            return new TreeMap<>(ranksByFavor.getOrDefault(team.name(), Collections.emptyNavigableMap()));
        } else {
            return Collections.emptyNavigableMap();
        }
    }

    @Override
    public Map<Integer, Map<LootCrateType, Integer>> getRankUpRewards() {
        return new HashMap<>(rankUpRewards);
    }

    @Override
    public void save(LegendsTeam team, NavigableMap<Integer, Rank> ranksByFavor) {
        this.ranksByFavor.put(team.name(), new TreeMap<>(ranksByFavor));
    }

    @Override
    public void save(Map<Integer, Map<LootCrateType, Integer>> rankUpRewards) {
        this.rankUpRewards.clear();
        this.rankUpRewards.putAll(rankUpRewards);
    }
}
