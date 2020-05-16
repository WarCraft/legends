package gg.warcraft.legends.api.menu

import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.ItemType
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.Player
import gg.warcraft.monolith.api.player.data.PlayerDataService

object TeamMenu {

  def infoButton(player: Player): Button = {
    val teamName = player.team.map { _.name }.getOrElse(LegendsTeam.Wayfarers.name)
    Button(
      ItemType.BOOK,
      "Choosing a team",
      List(
        "You are currently fighting for the",
        s"$YELLOW$teamName",
        "",
        "At the moment you can switch teams freely,",
        "but this will be changed at a later time."
      )
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

  def humanButton(player: Player)(implicit
      playerDataService: PlayerDataService
  ): Button = {
    Button.skull(
      "fishb6nes",
      LegendsTeam.Humans.name,
      List("[Shift Right Click] To switch to this team"),
      if (_.isShiftRightClick) {
        playerDataService.setPlayerData(player.data.copy(team = LegendsTeam.Humans))
      }
    )
  }

  def orcButton(player: Player): Button = {
    Button.skull(
      "fishb6nes",
      LegendsTeam.Orcs.name,
      List("[Shift Right Click] To switch to this team"),
      if (_.isShiftRightClick) {
        playerDataService.setPlayerData(player.data.copy(team = LegendsTeam.Orcs))
      }
    )
  }

  def dwarfButton(player: Player): Button = {
    Button.skull(
      "fishb6nes",
      LegendsTeam.Dwarves.name,
      List("[Shift Right Click] To switch to this team"),
      if (_.isShiftRightClick) {
        playerDataService.setPlayerData(player.data.copy(team = LegendsTeam.Dwarves))
      }
    )
  }

  def elfButton(player: Player): Button = {
    Button.skull(
      "fishb6nes",
      LegendsTeam.Elves.name,
      List("[Shift Right Click] To switch to this team"),
      if (_.isShiftRightClick) {
        playerDataService.setPlayerData(player.data.copy(team = LegendsTeam.Elves))
      }
    )
  }

  def apply(
      player: Player
  )(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService
  ): Menu = {
    Menu(
      "Allegiance",
      Menu.Size.ONE_ROW,
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
