/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import gg.warcraft.legends.api.LegendsConfig

import java.util.UUID

class ChallengeService {
  private var _challenges: Map[String, Challenge] = Map.empty
  private var dailies: List[Challenge] = Nil
  private var monthlies: List[Challenge] = Nil
  private var alltimes: List[Challenge] = Nil
  def challengesById: Map[String, Challenge] = _challenges

  private var _activeDailies: List[Challenge] = Nil
  private var _activeMonthlies: List[Challenge] = Nil
  def activeDailies: List[Challenge] = _activeDailies
  def activeMonthlies: List[Challenge] = _activeMonthlies

  private var _completedChallenges: Map[UUID, Map[String, Challenge]] = Map.empty
  def completedChallenges: Map[UUID, Map[String, Challenge]] = _completedChallenges

  def readConfig(config: LegendsConfig): Unit = {
    config.challenges.foreach { it =>
      _challenges += (it.id -> it)
      it match {
        case it if it.id.startsWith("DAILY")   => dailies ::= it
        case it if it.id.startsWith("MONTHLY") => monthlies ::= it
        case it if it.id.startsWith("ALLTIME") => alltimes ::= it
      }
    }

  }

  def rotateDailies(): Unit = {
    // TODO select new dailys and delete active and completed from db
  }

  def rotateMonthlies(): Unit = {}
}

/*
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

 */

/*
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
 */
