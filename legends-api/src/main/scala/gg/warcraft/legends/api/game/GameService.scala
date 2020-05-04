package gg.warcraft.legends.api.game

import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.entity.team.Team

class GameService {
  private final val tallyTimerDuration = 900.seconds
  private final val dailyWinnerWarScore = 5
  private final val skirmishWinnerWarScore = 1

  private var _activeGame: Game = _
  private var _currentWar: War = _
  private var _defaultCaptures: Map[String, Team] = _

  def activeGame: Game = _activeGame
  def currentWar: War = _currentWar

  def readConfig(config: LegendsConfig): Unit = {
    _defaultCaptures = config.defaultCaptures
  }

  def initActiveGame: Unit = ???

  def tickActiveGame: Unit = ???

  def startWar: Unit = ???

  def runTally: Unit = ???

  def runScore: Unit = ??? // TODO what is this?

  def endWar: Unit = ???

  def startSkirmish: Unit = ???

  def endSkirmish: Unit = ???
}


/*
public interface GameQueryService {
    Game getGame(int id);
}

    void initializeActiveGame();


public interface GameRepository {
    int getCapturePoints(String objectiveType);

    void save(Game game);

    void saveActiveGame(int gameId);

    void saveCurrentWar(int gameId);

    void saveCapturePoints(String objectiveType, int points);
}
 */