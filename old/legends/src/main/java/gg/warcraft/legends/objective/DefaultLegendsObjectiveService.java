package gg.warcraft.legends.objective;

import gg.warcraft.legends.config.LegendsConfigurationService;
import gg.warcraft.legends.config.ObjectiveConfiguration;
import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.legends.menu.ReturnButton;
import gg.warcraft.legends.objective.event.PointsTalliedEvent;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.LoggerService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.MonolithPlugin;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.event.ServerShutdownEvent;
import gg.warcraft.monolith.objective.Objective;
import gg.warcraft.monolith.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.monolith.objective.event.ObjectiveNeutralizedEvent;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import gg.warcraft.monolith.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultLegendsObjectiveService implements LegendsObjectiveService, Listener {
    public static DefaultLegendsObjectiveService DO_NOT_USE_objectiveService;
    public static final String CURRENT_WAR = "3";
    public static boolean skirmishActive = false;

    private final ObjectiveDataRepository repository;
    private final Map<String, Objective> objectives;
    private final Scoreboard scoreboard;

    private Timer timer;
    private LegendsGameItem nonSkirmishGame;

    public DefaultLegendsObjectiveService(LoggerService loggerService, EventService eventService, LegendsConfigurationService configurationService) {
        DO_NOT_USE_objectiveService = this;
        repository = new ObjectiveDataRepository();
        LegendsGameItem tempCurrentWar = repository.get(CURRENT_WAR);
        if (tempCurrentWar == null) {
            Map<String, Integer> scores = new HashMap<>();
            Map<String, String> objectives = new HashMap<>();
            tempCurrentWar = new LegendsGameItem(CURRENT_WAR, scores, objectives, 900);
        }
        LegendsGameItem currentWar = tempCurrentWar;

        this.objectives = new HashMap<>();
        List<ObjectiveConfiguration> configurations = configurationService.getObjectiveConfigurations();
        configurations.forEach(configuration -> {
            loggerService.info("Initializing objective: " + configuration.getName());
            AbstractCapturableObjective objective = new AbstractCapturableObjective(configuration);
            objectives.put(configuration.getId(), objective);
        });

        initializeObjectives(currentWar);

        SimpleScoreboard scoreboard = new SimpleScoreboard(this);

        // initialize existing data
        int humanScore = currentWar.getScore().getOrDefault("HUMAN", 0);
        int orcScore = currentWar.getScore().getOrDefault("ORC", 0);
        scoreboard.setScore(LegendsRace.HUMAN, humanScore);
        scoreboard.setScore(LegendsRace.ORC, orcScore);

        if (!((MonolithPlugin) Monolith.getInstance()).inMaintenanceMode()) { // pause timer if in maintenance mode
            if (currentWar.getSecondsToTally() != 0) {
                this.timer = new Timer(900, true, currentWar.getSecondsToTally());
            } else {
                this.timer = new Timer(900, true);
            }
        }
        if (timer != null) {
            scoreboard.setTimer(timer);
        }
        this.scoreboard = scoreboard;

        Listener announcer = new ObjectiveCapturedAnnouncer();
        eventService.registerForEvents(announcer);

        eventService.registerForEvents(new KillingBlowListener(scoreboard));

        eventService.registerForEvents(this);
    }

    private void initializeObjectives(LegendsGameItem war) {
        objectives.values().forEach(objective -> {
            AbstractCapturableObjective capObjective = (AbstractCapturableObjective) objective;
            // initialize existing data
            String currentOccupiers = war.getObjectives().get(capObjective.getId());
            if (currentOccupiers != null && !currentOccupiers.isEmpty() && !currentOccupiers.equals("NULL")) {
                capObjective.setOccupiers(LegendsRace.valueOf(currentOccupiers));
            }
        });
    }

    public void startSkirmish(String[] args) {
        int durationInMinutes = 30;
        int countdownInMinutes = 1;
        SimpleScoreboard simpleScoreboard = (SimpleScoreboard) scoreboard;

        new SkirmishCountdownTimer();
        RunnableUtils.runLater(() -> {
            // pause current game
            nonSkirmishGame = persistData();
            timer.stop();

            // start skirmish
            skirmishActive = true;

            Listener skirmishCaptureListener = new SkirmishObjectiveCapturedListener(simpleScoreboard);
            EventService.getInstance().registerForEvents(skirmishCaptureListener);

            ((AbstractCapturableObjective) objectives.get("HUMAN_FARM")).setOccupiers(LegendsRace.HUMAN);
            ((AbstractCapturableObjective) objectives.get("HUMAN_KEEP_EAST")).setOccupiers(LegendsRace.HUMAN);
            ((AbstractCapturableObjective) objectives.get("HUMAN_KEEP_WEST")).setOccupiers(LegendsRace.HUMAN);
            ((AbstractCapturableObjective) objectives.get("HUMAN_LUMBER_MILL")).setOccupiers(LegendsRace.HUMAN);
            ((AbstractCapturableObjective) objectives.get("HUMAN_MINE")).setOccupiers(LegendsRace.HUMAN);
            ((AbstractCapturableObjective) objectives.get("HUMAN_OUTPOST_ORC")).setOccupiers(LegendsRace.HUMAN);

            ((AbstractCapturableObjective) objectives.get("ORC_FARM")).setOccupiers(LegendsRace.ORC);
            ((AbstractCapturableObjective) objectives.get("ORC_KEEP_EAST")).setOccupiers(LegendsRace.ORC);
            ((AbstractCapturableObjective) objectives.get("ORC_KEEP_WEST")).setOccupiers(LegendsRace.ORC);
            ((AbstractCapturableObjective) objectives.get("ORC_LUMBER_MILL")).setOccupiers(LegendsRace.ORC);
            ((AbstractCapturableObjective) objectives.get("ORC_MINE")).setOccupiers(LegendsRace.ORC);
            ((AbstractCapturableObjective) objectives.get("ORC_OUTPOST_HUMAN")).setOccupiers(LegendsRace.ORC);

            simpleScoreboard.setScore(LegendsRace.HUMAN, 0);
            simpleScoreboard.setScore(LegendsRace.ORC, 0);
            Timer theTimer = new Timer(30 * TimeUtils.SECONDS_PER_MINUTE, false);
            simpleScoreboard.setTimer(theTimer);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendTitle("", "The skirmish has started!");
                player.sendMessage(ChatColor.YELLOW + "The skirmish has started!");

                LegendsPlayer legendsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(player.getUniqueId());
                ReturnButton returnButton = new ReturnButton(legendsPlayer);
                returnButton.forceTeleportInstantly();
            });

            RunnableUtils.runLater(() -> {
                new SkirmishEndsInTimer();
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.YELLOW + "The skirmish ends in 60 seconds!"));
            }, new Duration((durationInMinutes - 1) * TimeUtils.SECONDS_PER_MINUTE, Duration.Type.SECONDS));

            RunnableUtils.runLater(() -> {
                int humanScore = simpleScoreboard.getScore(LegendsRace.HUMAN);
                int orcScore = simpleScoreboard.getScore(LegendsRace.ORC);

                if (humanScore > orcScore) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendTitle("", "Humans have won!");
                        player.sendMessage(ChatColor.YELLOW + "The Humans have won the skirmish!");
                    });
                } else if (orcScore > humanScore) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendTitle("", "Orcs have won!");
                        player.sendMessage(ChatColor.YELLOW + "The Orcs have won the skirmish!");
                    });
                } else if (humanScore == orcScore) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendTitle("", "Unbelievable, its a tie!");
                        player.sendMessage(ChatColor.YELLOW + "Unbelievable, the skirmish has ended in a tie!");
                    });
                }

                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(ChatColor.YELLOW + "The skirmish has ended!");

                    LegendsPlayer legendsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(player.getUniqueId());
                    ReturnButton returnButton = new ReturnButton(legendsPlayer);
                    returnButton.forceTeleportInstantly();
                });

                EventService.getInstance().unregisterFromEvents(skirmishCaptureListener);

                initializeObjectives(nonSkirmishGame);

                simpleScoreboard.setScore(LegendsRace.HUMAN, nonSkirmishGame.getScore().get(LegendsRace.HUMAN.toString()));
                simpleScoreboard.setScore(LegendsRace.ORC, nonSkirmishGame.getScore().get(LegendsRace.ORC.toString()));

                timer = new Timer(900, true, nonSkirmishGame.getSecondsToTally());
                simpleScoreboard.setTimer(timer);

                skirmishActive = false;
            }, new Duration(durationInMinutes * TimeUtils.SECONDS_PER_MINUTE, Duration.Type.SECONDS));
        }, new Duration(countdownInMinutes * TimeUtils.SECONDS_PER_MINUTE, Duration.Type.SECONDS));
    }

    @Override
    public List<Objective> getObjectives() {
        List<Objective> obje = new ArrayList<>();
        obje.addAll(objectives.values());
        return obje;
    }

    @Override
    public Objective getObjective(String id) {
        return objectives.get(id);
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private LegendsGameItem persistData() {
        if (skirmishActive) {
            System.out.println("NOT PERSISTING OBJECTIVE DATA: SKIRMISH ACTIVE");
            return nonSkirmishGame;
        }

        Map<String, Integer> scoreData = new HashMap<>();
        scoreData.put(LegendsRace.HUMAN.name(), scoreboard.getScore(LegendsRace.HUMAN));
        scoreData.put(LegendsRace.ORC.name(), scoreboard.getScore(LegendsRace.ORC));

        Map<String, String> objectivesData = new HashMap<>();
        objectives.forEach((id, objective) -> {
            AbstractCapturableObjective capturableObjective = ((AbstractCapturableObjective) objective);
            if (capturableObjective.getOccupiers() != null) {
                String occupiers = ((LegendsRace) capturableObjective.getOccupiers()).name();
                objectivesData.put(id, occupiers);
            } else {
                objectivesData.put(id, "NULL");
            }
        });

        LegendsGameItem currentGame = new LegendsGameItem(CURRENT_WAR, scoreData, objectivesData, timer.getSecondsRemaining());
        repository.save(currentGame);
        return currentGame;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        persistData();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {
        persistData();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPointsTalliedEvent(PointsTalliedEvent event) {
        persistData();
    }

    @EventHandler
    public void onServerShutdownEvent(ServerShutdownEvent event) {
        persistData();
    }
}
