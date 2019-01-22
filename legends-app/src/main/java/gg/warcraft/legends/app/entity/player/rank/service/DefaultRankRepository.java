package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.app.entity.player.rank.SimpleRank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Singleton
public class DefaultRankRepository implements RankRepository {
    private final Map<Integer, Rank> ranksByLevel;
    private final Map<String, NavigableMap<Integer, Rank>> ranksByFavor;

    private int maxFavor;

    public DefaultRankRepository() {
        this.ranksByLevel = new HashMap<>();
        this.ranksByFavor = new HashMap<>();
    }

    @Override
    public Rank getRank(int level) {
        int clippedLevel = level % ranksByLevel.size();
        Rank baseRank = ranksByLevel.get(clippedLevel);

        int prestige = level / ranksByLevel.size();
        return new SimpleRank(baseRank.getName(), baseRank.getLevel(), prestige, baseRank.getColor(),
                baseRank.getRequiredFavor());
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
    public int getMaxFavor() {
        return maxFavor;
    }

    @Override
    public void save(Map<Integer, Rank> ranksByLevel) {
        this.ranksByLevel.clear();
        this.ranksByLevel.putAll(ranksByLevel);
    }

    @Override
    public void save(LegendsTeam team, NavigableMap<Integer, Rank> ranksByFavor) {
        this.ranksByFavor.put(team.name(), new TreeMap<>(ranksByFavor));
    }

    @Override
    public void save(int maxFavor) {
        this.maxFavor = maxFavor;
    }
}
