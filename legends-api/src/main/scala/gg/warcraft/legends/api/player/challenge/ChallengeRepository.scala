/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import com.typesafe.config.Config
import gg.warcraft.legends.api.util.quill._
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ChallengeRepository {
  def loadActive(): List[Challenge]

  def saveActive(challenges: List[Challenge]): Future[Unit]

  def deleteActiveDailies(): Future[Unit]

  def deleteActiveMonthlies(): Future[Unit]

  def loadCompleted(playerId: UUID): Map[String, Challenge]

  def saveCompleted(playerId: UUID, challenge: Challenge): Future[Unit]

  def deleteCompletedDailies(): Future[Unit]

  def deleteCompletedMonthlies(): Future[Unit]
}

private trait ChallengeContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryActive = quote {
    (q: EntityQuery[ActiveChallenge]) => q.map { _.challenge }
  }

  def insertActive = quote {
    (q: EntityQuery[ActiveChallenge], challenges: List[ActiveChallenge]) =>
      liftQuery(challenges).foreach { it => q.insert(it) }
  }

  def _deleteActiveDailies = quote {
    (q: EntityQuery[ActiveChallenge]) => q.filter { _.isDaily }.delete
  }

  def _deleteActiveMonthlies = quote {
    (q: EntityQuery[ActiveChallenge]) => q.filter { _.isMonthly }.delete
  }

  def queryCompleted = quote {
    (q: EntityQuery[CompletedChallenge], playerId: UUID) =>
      q.filter { _.playerId == lift(playerId) }
  }

  def insertCompleted = quote {
    (q: EntityQuery[CompletedChallenge], challenge: CompletedChallenge) =>
      q.insert(lift(challenge))
  }

  def _deleteCompletedDailies = quote {
    (q: EntityQuery[CompletedChallenge]) => q.filter { _.isDaily }.delete
  }

  def _deleteCompletedMonthlies = quote {
    (q: EntityQuery[CompletedChallenge]) => q.filter { _.isMonthly }.delete
  }
}

private[legends] class PostgresStatisticRepository(
    config: Config
)(implicit
    challengeService: ChallengeService
) extends ChallengeRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[PostgresDialect, SnakeCase]
  import database._

  private implicit val decoder: MappedEncoding[String, Challenge] = challengeDecoder

  override def loadActive(): List[Challenge] =
    run { queryActive(query[ActiveChallenge]) }

  override def saveActive(challenges: List[Challenge]): Future[Unit] = Future {
    val activeChallenges = challenges.map(ActiveChallenge)
    run { insertActive(query[ActiveChallenge], activeChallenges) }
  }

  override def deleteActiveDailies(): Future[Unit] = Future {
    run { _deleteActiveDailies(query[ActiveChallenge]) }
  }

  override def deleteActiveMonthlies(): Future[Unit] = Future {
    run { _deleteActiveMonthlies(query[ActiveChallenge]) }
  }

  override def loadCompleted(playerId: UUID): Map[String, Challenge] =
    run { queryCompleted(query[CompletedChallenge], playerId) }
      .iterator
      .map { it => it.challenge.id -> it.challenge }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenge: Challenge
  ): Future[Unit] = Future {
    val completedChallenge = CompletedChallenge(playerId, challenge)
    run { insertCompleted(query[CompletedChallenge], completedChallenge) }
  }

  override def deleteCompletedDailies(): Future[Unit] = Future {
    run { _deleteCompletedDailies(query[CompletedChallenge]) }
  }

  override def deleteCompletedMonthlies(): Future[Unit] = Future {
    run { _deleteCompletedMonthlies(query[CompletedChallenge]) }
  }
}

private[legends] class SqliteStatisticRepository(
    config: Config
)(implicit
    challengeService: ChallengeService
) extends ChallengeRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[SqliteDialect, SnakeCase]
  import database._

  private implicit val decoder: MappedEncoding[String, Challenge] = challengeDecoder

  override def loadActive(): List[Challenge] =
    run { queryActive(query[ActiveChallenge]) }

  override def saveActive(challenges: List[Challenge]): Future[Unit] = Future {
    val activeChallenges = challenges.map(ActiveChallenge)
    run { insertActive(query[ActiveChallenge], activeChallenges) }
  }

  override def deleteActiveDailies(): Future[Unit] = Future {
    run { _deleteActiveDailies(query[ActiveChallenge]) }
  }

  override def deleteActiveMonthlies(): Future[Unit] = Future {
    run { _deleteActiveMonthlies(query[ActiveChallenge]) }
  }

  override def loadCompleted(playerId: UUID): Map[String, Challenge] =
    run { queryCompleted(query[CompletedChallenge], playerId) }
      .iterator
      .map { it => it.challenge.id -> it.challenge }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenge: Challenge
  ): Future[Unit] = Future {
    val completedChallenge = CompletedChallenge(playerId, challenge)
    run { insertCompleted(query[CompletedChallenge], completedChallenge) }
  }

  override def deleteCompletedDailies(): Future[Unit] = Future {
    run { _deleteCompletedDailies(query[CompletedChallenge]) }
  }

  override def deleteCompletedMonthlies(): Future[Unit] = Future {
    run { _deleteCompletedMonthlies(query[CompletedChallenge]) }
  }
}
