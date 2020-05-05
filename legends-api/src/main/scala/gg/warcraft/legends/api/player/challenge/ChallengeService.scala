package gg.warcraft.legends.api.player.challenge

import gg.warcraft.legends.api.LegendsConfig

class ChallengeService {
  private var _dailies: List[Challenge] = Nil
  private var _monthlies: List[Challenge] = Nil
  private var _allTimes: List[Challenge] = Nil
  private var _activeDailies: List[Challenge] = Nil
  private var _activeMonthlies: List[Challenge] = Nil

  def dailies: List[Challenge] = _dailies
  def monthlies: List[Challenge] = _monthlies
  def allTimes: List[Challenge] = _allTimes
  def activeDailies: List[Challenge] = _activeDailies
  def activeMonthlies: List[Challenge] = _activeMonthlies

  def readConfig(config: LegendsConfig): Unit = {
    config.challenges.foreach {
      case it if it.id.contains("DAILY")   => _dailies ::= it
      case it if it.id.contains("MONTHLY") => _monthlies ::= it
      case it if it.id.contains("ALLTIME") => _allTimes ::= it
    }
  }

  def rotateDailies(): Unit = {}

  def rotateMonthlies(): Unit = {}
}
