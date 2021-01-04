/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import com.typesafe.config.Config
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

  def saveCompleted(playerId: UUID, challenges: List[Challenge]): Future[Unit]

  def deleteCompletedDailies(): Future[Unit]

  def deleteCompletedMonthlies(): Future[Unit]
}

private trait ChallengeContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryActive(implicit
      q: EntityQuery[ActiveChallenge]
  ): Quoted[EntityQuery[Challenge]] =
    quote { q.map { _.challenge } }

  def insertActive(challenge: ActiveChallenge)(implicit
      q: EntityQuery[ActiveChallenge]
  ): Quoted[Insert[ActiveChallenge]] =
    quote { q.insert(challenge) }

  def _deleteActiveDailies(implicit
      q: EntityQuery[ActiveChallenge]
  ): Quoted[Delete[ActiveChallenge]] =
    quote { q.filter { _.isDaily }.delete }

  def _deleteActiveMonthlies(implicit
      q: EntityQuery[ActiveChallenge]
  ): Quoted[Delete[ActiveChallenge]] =
    quote { q.filter { _.isMonthly }.delete }

  def queryCompleted(playerId: UUID)(implicit
      q: EntityQuery[CompletedChallenge]
  ): Quoted[EntityQuery[CompletedChallenge]] =
    quote { q.filter { _.playerId == playerId } }

  def insertCompleted(challenge: CompletedChallenge)(implicit
      q: EntityQuery[CompletedChallenge]
  ): Quoted[Insert[CompletedChallenge]] =
    quote { q.insert(challenge) }

  def _deleteCompletedDailies(implicit
      q: EntityQuery[CompletedChallenge]
  ): Quoted[Delete[CompletedChallenge]] =
    quote { q.filter { _.isDaily }.delete }

  def _deleteCompletedMonthlies(implicit
      q: EntityQuery[CompletedChallenge]
  ): Quoted[Delete[CompletedChallenge]] =
    quote { q.filter { _.isMonthly }.delete }
}

private[legends] class PostgresStatisticRepository(
    config: Config
) extends ChallengeRepository {
  private val databaseContext = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[PostgresDialect, SnakeCase]
  import databaseContext._

  private implicit val q1: EntityQuery[ActiveChallenge] =
    query[ActiveChallenge]
  private implicit val q2: EntityQuery[CompletedChallenge] =
    query[CompletedChallenge]

  override def loadActive(): List[Challenge] =
    run { queryActive }

  override def saveActive(challenges: List[Challenge]): Future[Unit] = Future {
    val activeChallenges = challenges.map(ActiveChallenge)
    run { liftQuery(activeChallenges).foreach(insertActive) }
  }

  override def deleteActiveDailies(): Future[Unit] = Future {
    run { _deleteActiveDailies }
  }

  override def deleteActiveMonthlies(): Future[Unit] = Future {
    run { _deleteActiveMonthlies }
  }

  override def loadCompleted(playerId: UUID): Map[String, Challenge] =
    run { queryCompleted(lift(playerId)) }
      .iterator
      .map { it => it.challenge.id -> it.challenge }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenges: List[Challenge]
  ): Future[Unit] = Future {
    val completedChallenges = challenges.map { CompletedChallenge(playerId, _) }
    run { liftQuery(completedChallenges).foreach(insertCompleted) }
  }

  override def deleteCompletedDailies(): Future[Unit] = Future {
    run { _deleteCompletedDailies }
  }

  override def deleteCompletedMonthlies(): Future[Unit] = Future {
    run { _deleteCompletedMonthlies }
  }
}

private[legends] class SqliteStatisticRepository(
    config: Config
) extends ChallengeRepository {
  private val context = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[SqliteDialect, SnakeCase]
  import context._

  private implicit val q1: EntityQuery[ActiveChallenge] =
    query[ActiveChallenge]
  private implicit val q2: EntityQuery[CompletedChallenge] =
    query[CompletedChallenge]

  override def loadActive(): List[Challenge] =
    run { queryActive }

  override def saveActive(challenges: List[Challenge]): Future[Unit] = Future {
    val activeChallenges = challenges.map(ActiveChallenge)
    run { liftQuery(activeChallenges).foreach(insertActive) }
  }

  override def deleteActiveDailies(): Future[Unit] = Future {
    run { _deleteActiveDailies }
  }

  override def deleteActiveMonthlies(): Future[Unit] = Future {
    run { _deleteActiveMonthlies }
  }

  override def loadCompleted(playerId: UUID): Map[String, Challenge] =
    run { queryCompleted(lift(playerId)) }
      .iterator
      .map { it => it.challenge.id -> it.challenge }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenges: List[Challenge]
  ): Future[Unit] = Future {
    val completedChallenges = challenges.map { CompletedChallenge(playerId, _) }
    run { liftQuery(completedChallenges).foreach(insertCompleted) }
  }

  override def deleteCompletedDailies(): Future[Unit] = Future {
    run { _deleteCompletedDailies }
  }

  override def deleteCompletedMonthlies(): Future[Unit] = Future {
    run { _deleteCompletedMonthlies }
  }
}
