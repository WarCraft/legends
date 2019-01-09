package gg.warcraft.legends.spigot.core.handler;

import com.google.inject.Inject;
import de.slikey.effectlib.util.ReflectionUtils;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.War;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.siege.api.objective.Objective;
import gg.warcraft.siege.api.objective.service.ObjectiveQueryService;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Map;
import java.util.logging.Logger;

import static de.slikey.effectlib.util.ReflectionUtils.PackageType;
import static gg.warcraft.legends.api.entity.LegendsTeam.HUMAN;
import static gg.warcraft.legends.api.entity.LegendsTeam.ORC;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.WHITE;

public class SpigotScoreboardHandler implements Runnable {
    private static final String WAR_HEADER;
    private static final String SKIRMISH_HEADER;
    private static final String FOOTER = "\\nJoin your team on the WarCraft Community Discord at discord.warcraft.gg!";
    private static final String TEXT_JSON = "{\"text\": \"%s\"}";

    private static final LocalTime FIRST_DAILY_SKIRMISH_TIME = LocalTime.of(2, 0); // in UTC = 21PM NYT
    private static final LocalTime SECOND_DAILY_SKIRMISH_TIME = LocalTime.of(20, 0); // in UTC = 15PM NYT
    private static final LocalTime THIRD_DAILY_SKIRMISH_TIME = LocalTime.of(23, 0); // in UTC = 18PM NYT

    static {
        WAR_HEADER = ColorCode.GOLD + "WarCraft: Legends War\\n" + RESET +
                "Next skirmish in %s\\n" +
                HUMAN.getChatColor() + HUMAN.getPlural() + RESET + " | " +
                ORC.getChatColor() + ORC.getPlural() + RESET + "\\n" +
                "\\n" +
                "Monthly Score\\n" +
                "%s\\n" + RESET +
                "\\n" +
                "Daily Points\\n" +
                "%s\\n" + RESET +
                "\\n" +
                "Next tally in %s\\n" +
                "%s\\n";

        SKIRMISH_HEADER = ColorCode.GOLD + "WarCraft: Legends Skirmish\\n" + RESET +
                HUMAN.getChatColor() + HUMAN.getPlural() + RESET + " | " +
                ORC.getChatColor() + ORC.getPlural() + RESET + "\\n" +
                "\\n" +
                "Timer\\n" +
                "%s\\n" +
                "\\n" +
                "Points\\n" +
                "%s\\n" + RESET +
                "\\n" +
                "Objectives\\n" +
                "%s\\n";
    }

    private final GameQueryService gameQueryService;
    private final ObjectiveQueryService objectiveQueryService;
    private final TimeUtils timeUtils;
    private final Server server;
    private final Logger pluginLogger;

    private Field headerField;
    private Field footerField;

    @Inject
    public SpigotScoreboardHandler(GameQueryService gameQueryService, ObjectiveQueryService objectiveQueryService,
                                   TimeUtils timeUtils, Server server, @PluginLogger Logger pluginLogger) {
        this.gameQueryService = gameQueryService;
        this.objectiveQueryService = objectiveQueryService;
        this.timeUtils = timeUtils;
        this.server = server;
        this.pluginLogger = pluginLogger;

        try {
            headerField = ReflectionUtils.getField("PacketPlayOutPlayerListHeaderFooter", PackageType.MINECRAFT_SERVER, true, "a");
        } catch (ClassNotFoundException | NoSuchFieldException ex) {
            pluginLogger.severe("Could not get header field from 'PacketPlayOutPlayerListHeaderFooter': " + ex.getMessage());
        }
        try {
            footerField = ReflectionUtils.getField("PacketPlayOutPlayerListHeaderFooter", PackageType.MINECRAFT_SERVER, true, "b");
        } catch (ClassNotFoundException | NoSuchFieldException ex) {
            pluginLogger.severe("Could not get footer field from 'PacketPlayOutPlayerListHeaderFooter': " + ex.getMessage());
        }
    }

    private String getFormattedSkirmishCountdown() {
        LocalTime thisMinute = LocalTime.now().withSecond(0).withNano(0);
        int hours;
        int minutes = 60 - thisMinute.getMinute();
        if (thisMinute.isBefore(FIRST_DAILY_SKIRMISH_TIME)) {
            hours = FIRST_DAILY_SKIRMISH_TIME.getHour() - thisMinute.getHour();
        } else if (thisMinute.isBefore(SECOND_DAILY_SKIRMISH_TIME)) {
            hours = SECOND_DAILY_SKIRMISH_TIME.getHour() - thisMinute.getHour();
        } else if (thisMinute.isBefore(THIRD_DAILY_SKIRMISH_TIME)) {
            hours = THIRD_DAILY_SKIRMISH_TIME.getHour() - thisMinute.getHour();
        } else {
            hours = 24 - thisMinute.getHour() + FIRST_DAILY_SKIRMISH_TIME.getHour();
        }

        if (hours > 1) {
            return (hours + 1) + " hours";
        } else {
            if (minutes == 1) {
                return "1 minute";
            }
            return minutes + " minutes";
        }
    }

    private String getFormattedScore(War war) {
        Map<LegendsTeam, Integer> score = war.getWarScore();
        return "" +
                HUMAN.getChatColor() + score.getOrDefault(HUMAN, 0) + RESET + " | " +
                ORC.getChatColor() + score.getOrDefault(ORC, 0);
        // ELF.getColor() + score.getOrDefault(ELF, 0) + RESET + " | " +
        // DWARF.getColor() + score.getOrDefault(DWARF, 0);
    }

    private String getFormattedPoints(Game game) {
        Map<LegendsTeam, Integer> points = game.getPoints();
        return "" +
                HUMAN.getChatColor() + points.getOrDefault(HUMAN, 0) + RESET + " | " +
                ORC.getChatColor() + points.getOrDefault(ORC, 0);
        // ELF.getColor() + points.getOrDefault(ELF, 0) + RESET + " | " +
        // DWARF.getColor() + points.getOrDefault(DWARF, 0);
    }

    private String getColorOrDefault(Team team) {
        return team != null ? team.getChatColor().toString() : WHITE.toString();
    }

    private String getFormattedObjectivesFor(String keepEastId, String keepWestId, String farmId, String lumberMillId,
                                             String mineId, String outpostId) {
        Objective keepEast = objectiveQueryService.getObjective(keepEastId);
        Objective keepWest = objectiveQueryService.getObjective(keepWestId);
        Objective farm = objectiveQueryService.getObjective(farmId);
        Objective lumberMill = objectiveQueryService.getObjective(lumberMillId);
        Objective mine = objectiveQueryService.getObjective(mineId);
        Objective outpost = objectiveQueryService.getObjective(outpostId);
        return getColorOrDefault(keepEast.getOccupiers()) + "K" +
                getColorOrDefault(keepWest.getOccupiers()) + "K" +
                getColorOrDefault(farm.getOccupiers()) + "F" +
                getColorOrDefault(lumberMill.getOccupiers()) + "L" +
                getColorOrDefault(mine.getOccupiers()) + "M" +
                getColorOrDefault(outpost.getOccupiers()) + "O";
    }

    private String getFormattedObjectives() {
        String formattedHumanObjectives = getFormattedObjectivesFor("HUMAN_KEEP_EAST", "HUMAN_KEEP_WEST",
                "HUMAN_FARM", "HUMAN_LUMBER_MILL", "HUMAN_MINE", "HUMAN_OUTPOST_ORC");
        String formattedOrcObjectives = getFormattedObjectivesFor("ORC_KEEP_EAST", "ORC_KEEP_WEST",
                "ORC_FARM", "ORC_LUMBER_MILL", "ORC_MINE", "ORC_OUTPOST_HUMAN");
//        String formattedElfObjectives = getFormattedObjectivesFor("ELF_KEEP_EAST", "ELF_KEEP_WEST",
//                "ELF_FARM", "ELF_LUMBER_MILL", "ELF_MINE", "ELF_OUTPOST_DWARF", "ELF_OUTPOST_ORC");
//        String formattedDwarfObjectives = getFormattedObjectivesFor("DWARF_KEEP_EAST", "DWARF_KEEP_WEST",
//                "DWARF_FARM", "DWARF_LUMBER_MILL", "DWARF_MINE", "DWARF_OUTPOST_HUMAN", "DWARF_OUTPOST_ELF");
        return "" +
                formattedHumanObjectives + RESET + " | " +
                formattedOrcObjectives;
//                formattedElfObjectives + RESET + " | " +
//                formattedDwarfObjectives;
    }

    @Override
    public void run() {
        Game activeGame = gameQueryService.getActiveGame();

        String header;
        String timer = timeUtils.getDigitalDisplay(activeGame.getTimer());
        String formattedPoints = getFormattedPoints(activeGame);
        switch (activeGame.getType()) {
            case WAR:
                War activeWar = (War) activeGame;
                String skirmishCountdown = getFormattedSkirmishCountdown();
                String formattedScore = getFormattedScore(activeWar);
                header = String.format(WAR_HEADER, skirmishCountdown, formattedScore, formattedPoints, timer,
                        getFormattedObjectives());
                break;
            case SKIRMISH:
                header = String.format(SKIRMISH_HEADER, timer, formattedPoints, getFormattedObjectives());
                break;
            default:
                header = "Could not display current score.";
        }
        try {
            IChatBaseComponent serialisedHeader = IChatBaseComponent.ChatSerializer.a(String.format(TEXT_JSON, header));
            IChatBaseComponent serialisedFooter = IChatBaseComponent.ChatSerializer.a(String.format(TEXT_JSON, FOOTER));
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            headerField.set(packet, serialisedHeader);
            footerField.set(packet, serialisedFooter);
            server.getOnlinePlayers().forEach(player ->
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));
        } catch (IllegalAccessException ex) {
            pluginLogger.severe("Could not broadcast Scoreboard: " + ex.getMessage());
        }
    }
}
