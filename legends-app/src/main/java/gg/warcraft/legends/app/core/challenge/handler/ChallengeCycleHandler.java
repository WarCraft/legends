package gg.warcraft.legends.app.core.challenge.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.challenge.Challenge;
import gg.warcraft.legends.api.core.challenge.service.ChallengeRepository;
import gg.warcraft.legends.api.core.game.event.WarStartedEvent;
import gg.warcraft.monolith.api.core.event.DailyTickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeCycleHandler {
    private final ChallengeRepository challengeRepository;

    @Inject
    public ChallengeCycleHandler(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
        if (challengeRepository.getDailies().isEmpty()) {
            cycleDailies();
        }
        if (challengeRepository.getWarries().isEmpty()) {
            cycleWarries();
        }
    }

    private List<Challenge> getRandomChallenges(EnumSet<Challenge> challenges, int count) {
        List<Challenge> shuffledChallenges = new ArrayList<>(challenges);
        Collections.shuffle(shuffledChallenges);
        return shuffledChallenges.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    private void cycleDailies() {
        List<Challenge> newDailies = getRandomChallenges(Challenge.getDailies(), 3);
        challengeRepository.setDailies(newDailies);
    }

    private void cycleWarries() {
        List<Challenge> newWarries = getRandomChallenges(Challenge.getWarries(), 10);
        challengeRepository.setWarries(newWarries);
    }

    @Subscribe
    public void onDailyTickEvent(DailyTickEvent event) {
        cycleDailies();
    }

    @Subscribe
    public void onWarStartedEvent(WarStartedEvent event) {
        cycleWarries();
    }
}
