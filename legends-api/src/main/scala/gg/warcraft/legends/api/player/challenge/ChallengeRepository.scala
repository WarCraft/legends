/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import com.typesafe.config.Config
import gg.warcraft.legends.api.util.codecs.legends._
import gg.warcraft.monolith.api.util.codecs.quill._
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ChallengeRepository {
  def loadActive()(implicit
      service: ChallengeService
  ): List[Challenge]

  def saveActive(challenges: List[Challenge]): Future[Unit]

  def deleteActiveDailies(): Future[Unit]

  def deleteActiveMonthlies(): Future[Unit]

  def loadCompleted(playerId: UUID)(implicit
      service: ChallengeService
  ): Map[String, Challenge]

  def saveCompleted(playerId: UUID, challenge: Challenge): Future[Unit]

  def deleteCompletedDailies(): Future[Unit]

  def deleteCompletedMonthlies(): Future[Unit]
}

private trait ChallengeContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryActive = quote {
    (q: EntityQuery[ActiveChallenge]) => q
  }

  def insertActive = quote {
    (q: EntityQuery[ActiveChallenge], challenge: ActiveChallenge) =>
      q.insert(challenge)
  }

  def _deleteActiveDailies = quote {
    (q: EntityQuery[ActiveChallenge]) =>
      q.filter { _.id.like("DAILY%") }.delete
  }

  def _deleteActiveMonthlies = quote {
    (q: EntityQuery[ActiveChallenge]) =>
      q.filter { _.id.like("MONTHLY%") }.delete
  }

  def queryCompleted = quote {
    (q: EntityQuery[CompletedChallenge], playerId: UUID) =>
      q.filter { _.playerId == playerId }
  }

  def insertCompleted = quote {
    (q: EntityQuery[CompletedChallenge], challenge: CompletedChallenge) =>
      q.insert(challenge)
  }

  def _deleteCompletedDailies = quote {
    (q: EntityQuery[CompletedChallenge]) =>
      q.filter { _.id.like("DAILY%") }.delete
  }

  def _deleteCompletedMonthlies = quote {
    (q: EntityQuery[CompletedChallenge]) =>
      q.filter { _.id.like("MONTHLY%") }.delete
  }
}

private[legends] class PostgresChallengeRepository(
    config: Config
) extends ChallengeRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[PostgresDialect, SnakeCase]
  import database._

  override def loadActive()(implicit
      service: ChallengeService
  ): List[Challenge] =
    run { queryActive(query[ActiveChallenge]) }
      .map { it => service.challengesById(it.id) }

  override def saveActive(challenges: List[Challenge]): Future[Unit] = Future {
    val activeChallenges = challenges.map { it => ActiveChallenge(it.id) }
    run {
      liftQuery(activeChallenges).foreach {
        insertActive(query[ActiveChallenge], _)
      }
    }
  }

  override def deleteActiveDailies(): Future[Unit] = Future {
    run { _deleteActiveDailies(query[ActiveChallenge]) }
  }

  override def deleteActiveMonthlies(): Future[Unit] = Future {
    run { _deleteActiveMonthlies(query[ActiveChallenge]) }
  }

  override def loadCompleted(playerId: UUID)(implicit
      service: ChallengeService
  ): Map[String, Challenge] =
    run { queryCompleted(query[CompletedChallenge], lift(playerId)) }
      .iterator
      .map { it => it.id -> service.challengesById(it.id) }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenge: Challenge
  ): Future[Unit] = Future {
    val completedChallenge = CompletedChallenge(challenge.id, playerId)
    run { insertCompleted(query[CompletedChallenge], lift(completedChallenge)) }
  }

  override def deleteCompletedDailies(): Future[Unit] = Future {
    run { _deleteCompletedDailies(query[CompletedChallenge]) }
  }

  override def deleteCompletedMonthlies(): Future[Unit] = Future {
    run { _deleteCompletedMonthlies(query[CompletedChallenge]) }
  }
}

private[legends] class SqliteChallengeRepository(
    config: Config
) extends ChallengeRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[SqliteDialect, SnakeCase]
  import database._

  override def loadActive()(implicit
      service: ChallengeService
  ): List[Challenge] =
    run { queryActive(query[ActiveChallenge]) }
      .map { it => service.challengesById(it.id) }

  override def saveActive(challenges: List[Challenge]): Future[Unit] = Future {
    val activeChallenges = challenges.map { it => ActiveChallenge(it.id) }
    run {
      liftQuery(activeChallenges).foreach {
        insertActive(query[ActiveChallenge], _)
      }
    }
  }

  override def deleteActiveDailies(): Future[Unit] = Future {
    run { _deleteActiveDailies(query[ActiveChallenge]) }
  }

  override def deleteActiveMonthlies(): Future[Unit] = Future {
    run { _deleteActiveMonthlies(query[ActiveChallenge]) }
  }

  override def loadCompleted(playerId: UUID)(implicit
      service: ChallengeService
  ): Map[String, Challenge] =
    run { queryCompleted(query[CompletedChallenge], lift(playerId)) }
      .iterator
      .map { it => it.id -> service.challengesById(it.id) }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenge: Challenge
  ): Future[Unit] = Future {
    val completedChallenge = CompletedChallenge(challenge.id, playerId)
    run { insertCompleted(query[CompletedChallenge], lift(completedChallenge)) }
  }

  override def deleteCompletedDailies(): Future[Unit] = Future {
    run { _deleteCompletedDailies(query[CompletedChallenge]) }
  }

  override def deleteCompletedMonthlies(): Future[Unit] = Future {
    run { _deleteCompletedMonthlies(query[CompletedChallenge]) }
  }
}
