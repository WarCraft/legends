package gg.warcraft.legends;

import gg.warcraft.legends.world.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GuidingHolograms {

    public GuidingHolograms(World ouros, World ourosTheEnd) {
        // TUTORIAL
        // starting island
        List<String> tutorialFirstIslandText = new ArrayList<>();
        tutorialFirstIslandText.add(ChatColor.GOLD + "Welcome to WarCraft: Legends!");
        tutorialFirstIslandText.add("We've put together a short tutorial to help you get started as quickly as possible.");
        tutorialFirstIslandText.add("If at any point you have any questions do not hesitate to ask someone in chat.");

        Location tutorialFirstIslandLocation = new Location(ourosTheEnd, 24, 161, 148);
        tutorialFirstIslandLocation.setDirection(new Vector(1, 0, 0));
        new Hologram(tutorialFirstIslandLocation, tutorialFirstIslandText);

        String raceMenu = ChatColor.YELLOW + "/race" + ChatColor.RESET + " menu";
        // race island
        List<String> tutorialRaceIslandText = new ArrayList<>();
        tutorialRaceIslandText.add("Ouros was home to four great races who now battle it out on it's shattered moon.");
        tutorialRaceIslandText.add("At the moment you can choose between the Humenun (humans) and the Vorkulth (orcs).");
        tutorialRaceIslandText.add("");
        tutorialRaceIslandText.add("You can change your race at any time using the " + raceMenu + ",");
        tutorialRaceIslandText.add("this wont cause you to lose anything.");
        tutorialRaceIslandText.add("");
        tutorialRaceIslandText.add("When you are ready go down towards the next island via the water.");

        Location tutorialRaceIslandLocation = new Location(ourosTheEnd, 1, 169, 152);
        tutorialRaceIslandLocation.setDirection(new Vector(1, 0, 0));
        new Hologram(tutorialRaceIslandLocation, tutorialRaceIslandText);

        // race island human
        List<String> tutorialRaceIslandHumanText = new ArrayList<>();
        tutorialRaceIslandHumanText.add("The Imperium Of Man is a collective of cultures formed during the Fifth Age to");
        tutorialRaceIslandHumanText.add("unite all human civilizations into the single greatest kingdom Ouros has known.");
        tutorialRaceIslandHumanText.add("");
        tutorialRaceIslandHumanText.add("To join the Humenun open up the " + raceMenu + " and click on their button.");
        tutorialRaceIslandHumanText.add("");
        tutorialRaceIslandHumanText.add("To read more about their background story go to warcraft.gg/humenum");

        Location tutorialRaceIslandHumanLocation = new Location(ourosTheEnd, 6, 173, 145);
        tutorialRaceIslandHumanLocation.setDirection(new Vector(0, 0, 1));
        new Hologram(tutorialRaceIslandHumanLocation, tutorialRaceIslandHumanText);

        // race island orc
        List<String> tutorialRaceIslandOrcText = new ArrayList<>();
        tutorialRaceIslandOrcText.add("Vorkulth, the self-proclaimed \"Sons of the Bull\" are a warlike race that has");
        tutorialRaceIslandOrcText.add("upheld their own concept of honor.");
        tutorialRaceIslandOrcText.add("");
        tutorialRaceIslandOrcText.add("To join the Vorkulth open up the " + raceMenu + " and click on their button.");
        tutorialRaceIslandOrcText.add("");
        tutorialRaceIslandOrcText.add("To read more about their background story go to warcraft.gg/vorkulth");

        Location tutorialRaceIslandOrcLocation = new Location(ourosTheEnd, 6, 173, 159);
        tutorialRaceIslandOrcLocation.setDirection(new Vector(0, 0, -1));
        new Hologram(tutorialRaceIslandOrcLocation, tutorialRaceIslandOrcText);

        // MAIN SERVER
        // human spawn
        List<String> howToGetStartedHumanText = new ArrayList<>();
        howToGetStartedHumanText.add(ChatColor.GOLD + "Welcome to WarCraft: Legends!");
        howToGetStartedHumanText.add("First lets use the '/kit' command to get some basic items and equip the armor.");
        howToGetStartedHumanText.add("You can cast abilities while holding the correct weapon for your spec in your main hand");
        howToGetStartedHumanText.add("and clicking your left and right mouse buttons while, or while not, holding shift.");
        howToGetStartedHumanText.add("To get started follow the road ahead out of the capital city and go down into the forest,");
        howToGetStartedHumanText.add("when you run into a large sign board turn left towards the farm to get some food.");

        Location howToGetStartedHumanLocation = new Location(ouros, -1543, 56, 357);
        howToGetStartedHumanLocation.setDirection(new Vector(-1, 0, 0));
        new Hologram(howToGetStartedHumanLocation, howToGetStartedHumanText);

        List<String> howToGetStartedHumanText2 = new ArrayList<>();
        howToGetStartedHumanText2.add("After you have farmed some food we suggest you try to follow the signs to an easy mob");
        howToGetStartedHumanText2.add("camp to improve the quality of your weapon and armor. When you're confident you're ready");
        howToGetStartedHumanText2.add("for some PvP head towards the orc border and keep your eyes peeled for any objectives");
        howToGetStartedHumanText2.add("along the way that you can take back from the orcs! If their capture circles are not light");
        howToGetStartedHumanText2.add("blue it means your race does not currently control them.");
        howToGetStartedHumanText2.add("");
        howToGetStartedHumanText2.add("If you need any more help do not hesitate to ask someone in chat, good luck!");

        Location howToGetStartedHumanLocation2 = new Location(ouros, -1531, 56, 357);
        howToGetStartedHumanLocation2.setDirection(new Vector(-1, 0, 0));
        new Hologram(howToGetStartedHumanLocation2, howToGetStartedHumanText2);

        // orc spawn
        List<String> howToGetStartedOrcText = new ArrayList<>();
        howToGetStartedOrcText.add(ChatColor.GOLD + "Welcome to WarCraft: Legends! ");
        howToGetStartedOrcText.add("First lets use the '/kit' command to get some basic items and equip the armor.");
        howToGetStartedOrcText.add("You can cast abilities while holding the correct weapon for your spec in your main hand");
        howToGetStartedOrcText.add("and clicking your left and right mouse buttons while, or while not, holding shift.");
        howToGetStartedOrcText.add("To get started follow the road ahead out of the capital city and turn left towards");
        howToGetStartedOrcText.add("the pig farm to get some food. Don't forget to use your abilities to speed up the process!");
        howToGetStartedOrcText.add("On your way to the farm you will run into a large sign board that will guide you to other places.");
        howToGetStartedOrcText.add("");
        howToGetStartedOrcText.add("After you have farmed some food we suggest you try to follow the signs to an easy mob");
        howToGetStartedOrcText.add("camp to improve the quality of your weapon and armor. When you're confident you're ready");
        howToGetStartedOrcText.add("for some PvP head towards the human border and keep your eyes peeled for any objectives");
        howToGetStartedOrcText.add("along the way that you can take back from the humans! If their capture circles are not red");
        howToGetStartedOrcText.add("it means your race does not currently control them.");
        howToGetStartedOrcText.add("");
        howToGetStartedOrcText.add("If you need any more help do not hesitate to ask someone in chat, good luck!");

        Location howToGetStartedOrcLocation = new Location(ouros, 429, 45, 406);
        howToGetStartedOrcLocation.setDirection(new Vector(1, 0, 0));
        new Hologram(howToGetStartedOrcLocation, howToGetStartedOrcText);
    }
}
