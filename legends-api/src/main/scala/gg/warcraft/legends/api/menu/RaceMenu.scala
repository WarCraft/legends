package gg.warcraft.legends.api.menu

import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.ItemType
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.player.currency.CurrencyService

object RaceMenu {
  private def infoButton(player: Player): Button = {
    val teamName = player.team.map { _.name }.getOrElse(Wayfarers.name)
    Button(
      ItemType.BOOK,
      "Choosing a team",
      "You are currently fighting for the"
        :: s"$YELLOW$teamName"
        :: ""
        :: "At the moment you can switch teams freely,"
        :: "but this will be changed at a later time."
        :: Nil
    )
    /*
        builder.addTooltip("You can switch teams freely the first");
        builder.addTooltip("3 days of a war (start of the month).");
        builder.addTooltip("Alternatively, if you really want to");
        builder.addTooltip("switch teams right now you can do so");
        builder.addTooltip("for a few Fragments.");
        builder.addTooltip("");
        if (true) {
            builder.addTooltip(GREEN + "Changing teams is currently free");
        } else {
            builder.addTooltip(ORANGE + "Changing teams at this time costs 800 Fragments");
        }
     */

    /*
        List<String> createSwitchView(Team team, boolean canSwitch, String cooldown) {
        List<String> view = new ArrayList<>();
        if (canSwitch) {
            if (cooldown == null) {
                view.add(GREEN + "[SHIFT RIGHT CLICK] To switch to the " + ((LegendsTeam) team).getPlural());
            } else {
                view.add(RED + "You can change teams in " + cooldown);
            }
        } else {
            view.add("This is your current team");
        }
        return view;
    }
     */
  }

  private def humanButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Humans.name,
    "[Shift Right Click] To switch to this team" :: Nil
  ) { click =>
    if (click.isShiftRight) {
      playerService.setTeam(player.data.copy(team = Humans))
    }
  }

  private def orcButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Orcs.name,
    "[Shift Right Click] To switch to this team" :: Nil
  ) { click =>
    if (click.isShiftRight) {
      playerService.setTeam(player.data.copy(team = Orcs))
    }
  }

  private def dwarfButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Dwarves.name,
    "[Shift Right Click] To switch to this team" :: Nil
  ) { click =>
    if (click.isShiftRight) {
      playerService.setTeam(player.data.copy(team = Dwarves))
    }
  }

  private def elfButton(player: Player)(implicit
      playerService: PlayerService
  ): Button = Button.skull(
    "fishb6nes",
    Elves.name,
    "[Shift Right Click] To switch to this team" :: Nil
  ) { click =>
    if (click.isShiftRight) {
      playerService.setTeam(player.data.copy(team = Elves))
    }
  }

  def apply(
      player: Player
  )(implicit
      menuService: MenuService,
      playerService: PlayerService,
      attributeService: AttributeService,
      currencyService: CurrencyService
  ): Menu = {
    Menu(
      "Race",
      MenuSize.ONE_ROW,
      Map(
        0 -> infoButton,
        8 -> Button.back(MainMenu.apply),
        // 1st row
        2 -> humanButton(player),
        3 -> orcButton(player),
        4 -> dwarfButton(player),
        5 -> elfButton(player)
      )
    )
  }
}
