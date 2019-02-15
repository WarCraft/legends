package gg.warcraft.legends.app.core.challenge.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.challenge.Challenge;
import gg.warcraft.legends.api.core.challenge.service.ChallengeRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DefaultChallengeRepository implements ChallengeRepository {
    private final List<Challenge> dailies;
    private final List<Challenge> warries;

    public DefaultChallengeRepository() {
        this.dailies = new ArrayList<>();
        this.warries = new ArrayList<>();
    }

    @Override
    public List<Challenge> getDailies() {
        return new ArrayList<>(dailies);
    }

    @Override
    public void setDailies(List<Challenge> challenges) {
        dailies.clear();
        dailies.addAll(challenges);
    }

    @Override
    public List<Challenge> getWarries() {
        return new ArrayList<>(warries);
    }

    @Override
    public void setWarries(List<Challenge> challenges) {
        warries.clear();
        warries.addAll(challenges);
    }
}
