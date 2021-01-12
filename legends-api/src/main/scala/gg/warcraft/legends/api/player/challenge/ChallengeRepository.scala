/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import com.typesafe.config.Config
import gg.warcraft.legends.api.util.codecs.legends._
import gg.warcraft.monolith.api.util.codecs.quill._
import gg.warcraft.monolith.api.util.future.FutureOps
import io.getquill._
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom

import java.util.UUID
import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ChallengeRepository {
  def loadAllActive()(implicit
      service: ChallengeService
  ): List[Challenge]

  def saveActive(challenges: List[Challenge]): Future[Unit]

  def deleteAllActiveDailies(): Future[Unit]

  def deleteAllActiveMonthlies(): Future[Unit]

  def loadCompleted(playerId: UUID)(implicit
      service: ChallengeService
  ): Map[String, Challenge]

  def saveCompleted(playerId: UUID, challenge: Challenge): Future[Unit]

  def deleteAllCompletedDailies(): Future[Unit]

  def deleteAllCompletedMonthlies(): Future[Unit]
}

private trait ChallengeContext[I <: SqlIdiom, N <: NamingStrategy] {
  this: JdbcContext[I, N] =>

  def queryAllActive = quote {
    (q: EntityQuery[ActiveChallenge]) => q
  }

  def queryAllActiveLike = quote {
    (q: EntityQuery[ActiveChallenge], like: String) => q.filter { _.id.like(like) }
  }

  def insertActive = quote {
    (q: EntityQuery[ActiveChallenge], challenge: ActiveChallenge) =>
      q.insert(challenge)
  }

  def queryCompletedById = quote {
    (q: EntityQuery[CompletedChallenge], playerId: UUID) =>
      q.filter { _.playerId == playerId }
  }

  def insertCompleted = quote {
    (q: EntityQuery[CompletedChallenge], challenge: CompletedChallenge) =>
      q.insert(challenge)
  }

  def queryAllCompletedLike = quote {
    (q: EntityQuery[CompletedChallenge], like: String) =>
      q.filter { _.id.like(like) }
  }
}

private[legends] class PostgresChallengeRepository(
    config: Config
)(implicit
    logger: Logger
) extends ChallengeRepository {
  private val database = new PostgresJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[PostgresDialect, SnakeCase]
  import database._

  override def loadAllActive()(implicit
      service: ChallengeService
  ): List[Challenge] =
    run { queryAllActive(query[ActiveChallenge]) }
      .map { it => service.challengesById(it.id) }

  override def saveActive(challenges: List[Challenge]) = Future[Unit] {
    val activeChallenges = challenges.map { it => ActiveChallenge(it.id) }
    run {
      liftQuery(activeChallenges).foreach { insertActive(query[ActiveChallenge], _) }
    }
  }.andThenLog("ChallengeRepository", "saveActive", challenges)

  override def deleteAllActiveDailies() = Future[Unit] {
    run { queryAllActiveLike(query[ActiveChallenge], "DAILY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllActiveLike", "DAILY%")

  override def deleteAllActiveMonthlies() = Future[Unit] {
    run { queryAllActiveLike(query[ActiveChallenge], "MONTHLY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllActiveLike", "MONTHLY%")

  override def loadCompleted(playerId: UUID)(implicit
      service: ChallengeService
  ): Map[String, Challenge] =
    run { queryCompletedById(query[CompletedChallenge], lift(playerId)) }
      .iterator
      .map { it => it.id -> service.challengesById(it.id) }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenge: Challenge
  ) = Future[Unit] {
    val completedChallenge = CompletedChallenge(challenge.id, playerId)
    run { insertCompleted(query[CompletedChallenge], lift(completedChallenge)) }
  }.andThenLog("ChallengeRepository", "saveCompleted", playerId, challenge)

  override def deleteAllCompletedDailies() = Future[Unit] {
    run { queryAllCompletedLike(query[CompletedChallenge], "DAILY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllCompletedLike", "DAILY%")

  override def deleteAllCompletedMonthlies() = Future[Unit] {
    run { queryAllCompletedLike(query[CompletedChallenge], "MONTHLY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllCompletedLike", "MONTHLY%")
}

private[legends] class SqliteChallengeRepository(
    config: Config
)(implicit
    logger: Logger
) extends ChallengeRepository {
  private val database = new SqliteJdbcContext[SnakeCase](SnakeCase, config)
    with ChallengeContext[SqliteDialect, SnakeCase]
  import database._

  override def loadAllActive()(implicit
      service: ChallengeService
  ): List[Challenge] =
    run { queryAllActive(query[ActiveChallenge]) }
      .map { it => service.challengesById(it.id) }

  override def saveActive(challenges: List[Challenge]) = Future[Unit] {
    val activeChallenges = challenges.map { it => ActiveChallenge(it.id) }
    run {
      liftQuery(activeChallenges).foreach { insertActive(query[ActiveChallenge], _) }
    }
  }.andThenLog("ChallengeRepository", "saveActive", challenges)

  override def deleteAllActiveDailies() = Future[Unit] {
    run { queryAllActiveLike(query[ActiveChallenge], "DAILY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllActiveLike", "DAILY%")

  override def deleteAllActiveMonthlies() = Future[Unit] {
    run { queryAllActiveLike(query[ActiveChallenge], "MONTHLY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllActiveLike", "MONTHLY%")

  override def loadCompleted(playerId: UUID)(implicit
      service: ChallengeService
  ): Map[String, Challenge] =
    run { queryCompletedById(query[CompletedChallenge], lift(playerId)) }
      .iterator
      .map { it => it.id -> service.challengesById(it.id) }
      .toMap

  override def saveCompleted(
      playerId: UUID,
      challenge: Challenge
  ) = Future[Unit] {
    val completedChallenge = CompletedChallenge(challenge.id, playerId)
    run { insertCompleted(query[CompletedChallenge], lift(completedChallenge)) }
  }.andThenLog("ChallengeRepository", "saveCompleted", playerId, challenge)

  override def deleteAllCompletedDailies() = Future[Unit] {
    run { queryAllCompletedLike(query[CompletedChallenge], "DAILY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllCompletedLike", "DAILY%")

  override def deleteAllCompletedMonthlies() = Future[Unit] {
    run { queryAllCompletedLike(query[CompletedChallenge], "MONTHLY%").delete }
  }.andThenLog("ChallengeRepository", "deleteAllCompletedLike", "MONTHLY%")
}
