package gg.warcraft.legends.api.player.challenge.handler

class ChallengeRotateHandler {

}


/* TODO
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

 */