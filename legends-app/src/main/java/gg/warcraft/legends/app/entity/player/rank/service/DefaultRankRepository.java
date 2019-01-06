package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.monolith.api.entity.team.Team;

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
        return ranksByLevel.get(clippedLevel);
    }

    @Override
    public NavigableMap<Integer, Rank> getRanks(Team team) {
        if (team != null) {
            String teamName = team.getName();
            return new TreeMap<>(ranksByFavor.getOrDefault(teamName, Collections.emptyNavigableMap()));
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
    public void save(Team team, NavigableMap<Integer, Rank> ranksByFavor) {
        String teamName = team.getName();
        this.ranksByFavor.put(teamName, new TreeMap<>(ranksByFavor));
    }

    @Override
    public void save(int maxFavor) {
        this.maxFavor = maxFavor;
    }
}
