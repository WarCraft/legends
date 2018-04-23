package gg.warcraft.legends.objective;

import de.slikey.effectlib.util.ReflectionUtils;
import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.legends.objective.event.PointsTalliedEvent;
import gg.warcraft.monolith.LoggerService;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.player.PlayerService;
import gg.warcraft.monolith.util.Timer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static de.slikey.effectlib.util.ReflectionUtils.PackageType;
import static gg.warcraft.legends.entity.LegendsRace.HUMAN;
import static gg.warcraft.legends.entity.LegendsRace.ORC;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.WHITE;

public class SimpleScoreboard implements Scoreboard, Observer {
    private static final String HEADER = "This is an intermediate War that will not count!\\nNext tally in %s\\n%s\\n";
    private static final String SKIRMISH_HEADER = "Skirmish: %s\\n%s\\n";
    private static final String FOOTER = "\\nJoin your team on the WarCraft Community Discord at discord.warcraft.gg!";
    private static final String TEXT_JSON = "{\"text\": \"%s\"}";

    private final LegendsObjectiveService objectiveService;
    private final LoggerService loggerService;
    private final PlayerService playerService;
    private final HashMap<Race, Integer> scores;

    private Timer timerObject;
    private Field headerField;
    private Field footerField;
    private String timer;

    public SimpleScoreboard(LegendsObjectiveService objectiveService) {
        this(LoggerService.getInstance(), PlayerService.getInstance(), objectiveService);
    }

    public SimpleScoreboard(LoggerService loggerService, PlayerService playerService, LegendsObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
        this.loggerService = loggerService;
        this.playerService = playerService;
        this.scores = new HashMap<>();

        try {
            headerField = ReflectionUtils.getField("PacketPlayOutPlayerListHeaderFooter", PackageType.MINECRAFT_SERVER, true, "a");
        } catch (ClassNotFoundException | NoSuchFieldException ex) {
            loggerService.severe("Could not get header field from 'PacketPlayOutPlayerListHeaderFooter': " + ex.getMessage());
        }
        try {
            footerField = ReflectionUtils.getField("PacketPlayOutPlayerListHeaderFooter", PackageType.MINECRAFT_SERVER, true, "b");
        } catch (ClassNotFoundException | NoSuchFieldException ex) {
            loggerService.severe("Could not get footer field from 'PacketPlayOutPlayerListHeaderFooter': " + ex.getMessage());
        }
    }

    public void setTimer(String timer) {
        this.timer = timer;
        broadcast();
    }

    public void setTimer(Timer timer) {
        if (timerObject != null) {
            timerObject.deleteObserver(this);
        }
        timer.addObserver(this);
        timerObject = timer;
    }

    private String getFormattedScore() {
        return "" +
                HUMAN.getColor() + scores.getOrDefault(HUMAN, 0) + RESET + " | " +
                ORC.getColor() + scores.getOrDefault(ORC, 0);
        // ELF.getColor() + scores.getOrDefault(ELF, 0) + RESET + " | " +
        // DWARF.getColor() + scores.getOrDefault(DWARF, 0);
    }

    private ChatColor getColorOrDefault(Race race) {
        return race != null ? race.getColor() : WHITE;
    }

    private String getFormattedObjectivesFor(AbstractCapturableObjective keepEast, AbstractCapturableObjective keepWest,
                                             AbstractCapturableObjective farm, AbstractCapturableObjective lumberMill, AbstractCapturableObjective mine,
                                             AbstractCapturableObjective outpost) {
        ChatColor keepEastColor = getColorOrDefault(keepEast.getOccupiers());
        ChatColor keepWestColor = getColorOrDefault(keepWest.getOccupiers());
        ChatColor farmColor = getColorOrDefault(farm.getOccupiers());
        ChatColor lumberMillColor = getColorOrDefault(lumberMill.getOccupiers());
        ChatColor mineColor = getColorOrDefault(mine.getOccupiers());
        ChatColor outpostColor = getColorOrDefault(outpost.getOccupiers());
        return keepEastColor + "K" + keepWestColor + "K" +
                farmColor + "F" + lumberMillColor + "L" + mineColor + "M" +
                outpostColor + "O";
    }

    private String getFormattedObjectives() {
        AbstractCapturableObjective HUMAN_KEEP_EAST = (AbstractCapturableObjective) objectiveService.getObjective("HUMAN_KEEP_EAST");
        AbstractCapturableObjective HUMAN_KEEP_WEST = (AbstractCapturableObjective) objectiveService.getObjective("HUMAN_KEEP_WEST");
        AbstractCapturableObjective HUMAN_FARM = (AbstractCapturableObjective) objectiveService.getObjective("HUMAN_FARM");
        AbstractCapturableObjective HUMAN_LUMBER_MILL = (AbstractCapturableObjective) objectiveService.getObjective("HUMAN_LUMBER_MILL");
        AbstractCapturableObjective HUMAN_MINE = (AbstractCapturableObjective) objectiveService.getObjective("HUMAN_MINE");
        AbstractCapturableObjective HUMAN_OUTPOST_ORC = (AbstractCapturableObjective) objectiveService.getObjective("HUMAN_OUTPOST_ORC");
        AbstractCapturableObjective ORC_KEEP_EAST = (AbstractCapturableObjective) objectiveService.getObjective("ORC_KEEP_EAST");
        AbstractCapturableObjective ORC_KEEP_WEST = (AbstractCapturableObjective) objectiveService.getObjective("ORC_KEEP_WEST");
        AbstractCapturableObjective ORC_FARM = (AbstractCapturableObjective) objectiveService.getObjective("ORC_FARM");
        AbstractCapturableObjective ORC_LUMBER_MILL = (AbstractCapturableObjective) objectiveService.getObjective("ORC_LUMBER_MILL");
        AbstractCapturableObjective ORC_MINE = (AbstractCapturableObjective) objectiveService.getObjective("ORC_MINE");
        AbstractCapturableObjective ORC_OUTPOST_HUMAN = (AbstractCapturableObjective) objectiveService.getObjective("ORC_OUTPOST_HUMAN");
        String formattedHumanObjectives = getFormattedObjectivesFor(HUMAN_KEEP_EAST, HUMAN_KEEP_WEST,
                HUMAN_FARM, HUMAN_LUMBER_MILL, HUMAN_MINE, HUMAN_OUTPOST_ORC);
        String formattedOrcObjectives = getFormattedObjectivesFor(ORC_KEEP_EAST, ORC_KEEP_WEST,
                ORC_FARM, ORC_LUMBER_MILL, ORC_MINE, ORC_OUTPOST_HUMAN);
//        String formattedElfObjectives = getFormattedObjectivesFor(ELF_KEEP_EAST, ELF_KEEP_WEST,
//                ELF_FARM, ELF_LUMBER_MILL, ELF_MINE, ELF_OUTPOST_DWARF, ELF_OUTPOST_ORC);
//        String formattedDwarfObjectives = getFormattedObjectivesFor(DWARF_KEEP_EAST, DWARF_KEEP_WEST,
//                DWARF_FARM, DWARF_LUMBER_MILL, DWARF_MINE, DWARF_OUTPOST_HUMAN, DWARF_OUTPOST_ELF);
        return "" +
                formattedHumanObjectives + RESET + " | " +
                formattedOrcObjectives;
//                formattedElfObjectives + RESET + " | " +
//                formattedDwarfObjectives;
    }

    private void broadcast() {
        String THE_HEADER = DefaultLegendsObjectiveService.skirmishActive ? SKIRMISH_HEADER : HEADER;
        String header = String.format(THE_HEADER, timer, getFormattedScore() + "\n" + getFormattedObjectives());
        try {
            IChatBaseComponent serialisedHeader = IChatBaseComponent.ChatSerializer.a(String.format(TEXT_JSON, header));
            IChatBaseComponent serialisedFooter = IChatBaseComponent.ChatSerializer.a(String.format(TEXT_JSON, FOOTER));
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            headerField.set(packet, serialisedHeader);
            footerField.set(packet, serialisedFooter);
            playerService.getOnlinePlayers().forEach(player -> {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            });
        } catch (IllegalAccessException ex) {
            loggerService.severe("Could not broadcast Scoreboard: " + ex.getMessage());
        }
    }

    @Override
    public int getScore(Race race) {
        return scores.getOrDefault(race, 0);
    }

    @Override
    public void addScore(Race race, int score) {
        scores.put(race, scores.getOrDefault(race, 0) + score);
    }

    public void setScore(Race race, int score) {
        scores.put(race, score);
    }

    private void performTally() {
        objectiveService.getObjectives().stream()
                .map(objective -> (AbstractCapturableObjective) objective)
                .forEach(objective -> {
                    Race occupiers = objective.getOccupiers();
                    int points;
                    switch (objective.getType()) {
                        case "KEEP":
                            points = 25;
                            break;
                        case "OUTPOST":
                            points = 10;
                            break;
                        case "RESOURCE":
                            points = 5;
                            break;
                        default:
                            points = 0;
                            break;
                    }
                    if (occupiers != null) {
                        addScore(occupiers, points);
                    }
                });
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Timer) {
            {
                Timer timer = (Timer) observable;
                if (timer.getSecondsRemaining() <= 0) {
                    if (!DefaultLegendsObjectiveService.skirmishActive) {
                        performTally();
                        EventService.getInstance().publishEvent(
                                new PointsTalliedEvent(scores.get(LegendsRace.HUMAN), scores.get(LegendsRace.ORC)));
                    }
                }
            }

            timer = observable.toString();
            broadcast();
        }
    }
}
