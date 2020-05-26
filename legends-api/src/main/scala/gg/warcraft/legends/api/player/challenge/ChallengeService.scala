/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import gg.warcraft.legends.api.LegendsConfig

class ChallengeService {
  private var _dailies: List[Challenge] = Nil
  private var _monthlies: List[Challenge] = Nil
  private var _alltimes: List[Challenge] = Nil
  private var _activeDailies: List[Challenge] = Nil
  private var _activeMonthlies: List[Challenge] = Nil

  def dailies: List[Challenge] = _dailies
  def monthlies: List[Challenge] = _monthlies
  def alltimes: List[Challenge] = _alltimes
  def activeDailies: List[Challenge] = _activeDailies
  def activeMonthlies: List[Challenge] = _activeMonthlies

  def readConfig(config: LegendsConfig): Unit = {
    config.challenges.foreach {
      case it if it.id.contains("DAILY")   => _dailies ::= it
      case it if it.id.contains("MONTHLY") => _monthlies ::= it
      case it if it.id.contains("ALLTIME") => _alltimes ::= it
    }
  }

  def rotateDailies(): Unit = {}

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