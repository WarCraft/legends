package gg.warcraft.legends.app.core.challenge.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.challenge.Challenge;
import gg.warcraft.legends.api.core.challenge.service.ChallengeRepository;
import gg.warcraft.monolith.api.core.PersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class DefaultChallengeRepository implements ChallengeRepository {
    private static final String DAILY_CHALLENGES_KEY = "challenges:dailies";
    private static final String WARRY_CHALLENGES_KEY = "challenges:warries";

    private final PersistenceService persistenceService;
    private final List<Challenge> dailies;
    private final List<Challenge> warries;

    @Inject
    public DefaultChallengeRepository(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        this.dailies = persistenceService.getList(DAILY_CHALLENGES_KEY).stream()
                .map(Challenge::valueOf)
                .collect(Collectors.toList());
        this.warries = persistenceService.getList(WARRY_CHALLENGES_KEY).stream()
                .map(Challenge::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<Challenge> getDailies() {
        return new ArrayList<>(dailies);
    }

    @Override
    public void setDailies(List<Challenge> challenges) {
        List<String> challengeIds = challenges.stream()
                .map(Challenge::name)
                .collect(Collectors.toList());
        persistenceService.setList(DAILY_CHALLENGES_KEY, challengeIds);

        dailies.clear();
        dailies.addAll(challenges);
    }

    @Override
    public List<Challenge> getWarries() {
        return new ArrayList<>(warries);
    }

    @Override
    public void setWarries(List<Challenge> challenges) {
        List<String> challengeIds = challenges.stream()
                .map(Challenge::name)
                .collect(Collectors.toList());
        persistenceService.setList(WARRY_CHALLENGES_KEY, challengeIds);

        warries.clear();
        warries.addAll(challenges);
    }
}
