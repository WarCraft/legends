package gg.warcraft.legends.api.player.handler

import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class GoldCommandHandler(implicit
    playerService: PlayerService,
    entityService: EntityService
) extends Command.Handler {
  private final val tradeDistance = 3

  private def giveGold(player: Player, target: Player, amount: Int): Unit = {
    // TODO
  }

  override def handle(
      executor: Principal,
      command: Command,
      args: String*
  ): Command.Result = executor.principalId match {
    case Some(playerId) =>
      if (args.length == 2) {
        val player = playerService.getPlayer(playerId)
        val target = entityService.getNearbyEntities(player.location, tradeDistance)
          .find { _.name == args(0) }
          .map { _.asInstanceOf[Player] }
        if (target.isDefined) {
          giveGold(player, target.get, args(1).toInt)
          Command.success
        } else Command.invalid
      } else Command.invalid
    case None => Command.playersOnly
  }
}

/*
 boolean giveGold(LegendsPlayer player, List<String> arguments) {
      if (arguments.size() != 2) {
          playerCommandService.sendNotification(player.getId(), "Usage: /gold give <name> <amount>");
          return false;
      }

      String targetPlayerName = arguments.get(0);
      UUID targetPlayerId = playerQueryService.resolvePlayerId(targetPlayerName);
      if (targetPlayerId == null) {
          playerCommandService.sendNotification(player.getId(), "There appears to be no player named " +
                  targetPlayerName);
          return false;
      }

      int goldToGive;
      try {
          goldToGive = Integer.parseInt(arguments.get(1));
      } catch (Exception ex) {
          playerCommandService.sendNotification(player.getId(), "That is not a valid amount of gold.");
          return false;
      }

      int playerGold = player.getGold();
      if (goldToGive > playerGold) {
          playerCommandService.sendNotification(player.getId(), "You don't have that much gold.");
          return false;
      }

      LegendsPlayer targetPlayer = playerQueryService.getPlayer(targetPlayerId);
      Vector3f playerLocation = player.getLocation().translation();
      Vector3f targetPlayerLocation = targetPlayer.getLocation().translation();
      if (playerLocation.distanceTo(targetPlayerLocation) > TRADE_DISTANCE) {
          playerCommandService.sendNotification(player.getId(), "You need to be closer to " + targetPlayerName + "" +
                  " to give them gold.");
          return false;
      }

      playerCommandService.removeCurrency(player.getId(), LegendsCurrency.GOLD, goldToGive);
      playerCommandService.addCurrency(targetPlayerId, LegendsCurrency.GOLD, goldToGive);
      playerCommandService.sendNotification(player.getId(), "You've given " + goldToGive + " gold to " + targetPlayerName);
      playerCommandService.sendNotification(targetPlayerId, "You received " + goldToGive + " gold from " + player.getName());
      return true;
  }
 */
