package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Singleton
public class DefaultRankRepository implements RankRepository {
    private final Map<String, NavigableMap<Integer, Rank>> ranks;

    private int maxFavor;

    public DefaultRankRepository() {
        this.ranks = new HashMap<>();
    }

    @Override
    public NavigableMap<Integer, Rank> getRanks(Team team) {
        String teamName = team.getName();
        return new TreeMap<>(ranks.get(teamName));
    }

    @Override
    public int getMaxFavor() {
        return maxFavor;
    }

    @Override
    public void save(Team team, NavigableMap<Integer, Rank> ranks) {
        String teamName = team.getName();
        this.ranks.put(teamName, new TreeMap<>(ranks));
    }

    @Override
    public void save(int maxFavor) {
        this.maxFavor = maxFavor;
    }
}
