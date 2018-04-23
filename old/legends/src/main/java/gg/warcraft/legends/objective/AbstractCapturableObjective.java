package gg.warcraft.legends.objective;

import gg.warcraft.legends.config.ObjectiveConfiguration;
import gg.warcraft.monolith.effect.ProgressRingEffect;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.objective.CapturableObjective;
import gg.warcraft.monolith.objective.event.ObjectiveAssaultedEvent;
import gg.warcraft.monolith.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.monolith.objective.event.ObjectiveNeutralizedEvent;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractCapturableObjective implements CapturableObjective, Runnable {
    private static final double RADIUS = 5;
    private static final int MAX_PROGRESS = 1000;
    private static final int PROGRESS_PER_TICK = 5;
    private static final int DECAY_PROGRESS_PER_TICK = 1;

    private final EventService eventService;
    private final EntityService entityService;
    private final String id;
    private final String type;
    private final String name;
    private final Location captureLocation;
    private final ProgressRingEffect progressEffect;

    private final ObjectiveTeleporter outerPortal;
    private final ObjectiveTeleporter innerPortal;

    private Race occupiers;
    private Race contestants;
    private int progress;

    public AbstractCapturableObjective(ObjectiveConfiguration configuration) {
        this(EventService.getInstance(), EntityService.getInstance(), configuration);
    }

    public AbstractCapturableObjective(EventService eventService, EntityService entityService, ObjectiveConfiguration configuration) {
        this.eventService = eventService;
        this.entityService = entityService;
        this.id = configuration.getId();
        this.type = configuration.getType();
        this.name = configuration.getName();
        this.captureLocation = configuration.getCaptureLocation();

        this.innerPortal = new ObjectiveTeleporter(
                configuration.getInnerPortalEntryLocation(),
                configuration.getInnerPortalExitLocation(),
                this::getOccupiers
        );
        this.outerPortal = new ObjectiveTeleporter(
                configuration.getOuterPortalEntryLocation(),
                configuration.getOuterPortalExitLocation(),
                this::getOccupiers
        );

        this.progressEffect = new ProgressRingEffect(captureLocation);
        RunnableUtils.runTaskEachTick(this);
    }

    private Map<Race, List<MonolithEntity>> sortEntitiesByRace(List<MonolithEntity> entities) {
        Map<Race, List<MonolithEntity>> sortedEntities = new HashMap<>();
        entities.stream()
                .filter(entity -> entity.getRace() != null)
                .forEach(entity -> {
                    List<MonolithEntity> friends = sortedEntities.computeIfAbsent(entity.getRace(), key -> new ArrayList<>());
                    friends.add(entity);
                });
        return sortedEntities;
    }

    private Color chatColorToColor(ChatColor color) {
        // TODO put in some util class
        switch (color) {
            case BLACK:
                return Color.BLACK;
            case DARK_BLUE:
                return Color.NAVY;
            case DARK_GREEN:
                return Color.OLIVE;
            case DARK_AQUA:
                return Color.TEAL;
            case DARK_RED:
                return Color.RED;
            case DARK_PURPLE:
                return Color.PURPLE;
            case GOLD:
                return Color.ORANGE;
            case GRAY:
                return Color.GRAY;
            case DARK_GRAY:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case AQUA:
                return Color.AQUA;
            case RED:
                return Color.RED;
            case LIGHT_PURPLE:
                return Color.FUCHSIA;
            case YELLOW:
                return Color.YELLOW;
            case WHITE:
                return Color.WHITE;
            default:
                return Color.WHITE;
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return captureLocation;
    }

    @Override
    public Race getOccupiers() {
        return occupiers;
    }

    public void setOccupiers(Race occupiers) {
        this.occupiers = occupiers;
        progress = MAX_PROGRESS;
        contestants = null;
    }

    @Override
    public void run() {
        List<MonolithEntity> nearbyEntities = entityService.getNearbyMonolithEntities(captureLocation, RADIUS);
        Map<Race, List<MonolithEntity>> sortedNearbyEntities = sortEntitiesByRace(nearbyEntities);

        // First attackers become contestants and bring the current progress off the occupiers below 100
        // When the occupiers become the attackers the progress goes back up to 100 and the contestants become null
        // When progress hits 0 the occupiers become null
        // Then when progress hits 100 again the contestants become the occupiers

        // When there is no one on the objective the progress resets back to 100 in favor of the occupiers at 1/5 the capture speed

        if (sortedNearbyEntities.isEmpty()) {
            if (occupiers != null) {
                if (progress != MAX_PROGRESS) { // TODO extract into private method DecayInOccupiersFavor
                    progress += DECAY_PROGRESS_PER_TICK;
                    if (progress >= MAX_PROGRESS) {
                        progress = MAX_PROGRESS;
                        contestants = null;
                        // TODO option for shoutcasting "{occupiers} have defended {name} from {contestans}."

                        // TODO provides opportunity to store every occupier and contestant that touched the capture point and award appropriately as attack or defense stops
                    }
                }
            } else if (contestants != null) {
                progress -= DECAY_PROGRESS_PER_TICK; // TODO extract into private method DecayInOccupiersFavor
                if (progress <= 0) {
                    progress = 0;
                    contestants = null;
                }
            }
        } else if (sortedNearbyEntities.size() == 1) {
            Race attackers = sortedNearbyEntities.values().iterator().next().get(0).getRace();
            if (occupiers == null) {
                if (contestants == null) {
                    contestants = attackers;
                } else if (contestants == attackers) {
                    progress += PROGRESS_PER_TICK;
                    if (progress >= MAX_PROGRESS) {
                        progress = MAX_PROGRESS;
                        occupiers = attackers;
                        contestants = null;
                        eventService.publishEvent(new ObjectiveCapturedEvent(this));
                    }
                } else {
                    progress -= PROGRESS_PER_TICK;
                    if (progress <= 0) {
                        progress = 0;
                        contestants = null;
                    }
                }
            } else if (occupiers == attackers) {
                progress += PROGRESS_PER_TICK;
                if (progress >= MAX_PROGRESS) {
                    progress = MAX_PROGRESS;
                    contestants = null;
                }
            } else {
                if (contestants == null) {
                    contestants = attackers;
                    eventService.publishEvent(new ObjectiveAssaultedEvent(this, contestants));
                } else if (contestants == attackers) {
                    progress -= PROGRESS_PER_TICK;
                    if (progress <= 0) {
                        progress = 0;
                        Race previousOccupiers = occupiers;
                        occupiers = null;
                        eventService.publishEvent(new ObjectiveNeutralizedEvent(this, previousOccupiers));
                    }
                } else {
                    progress -= PROGRESS_PER_TICK;
                    if (progress <= 0) {
                        progress = 0;
                        contestants = null;
                    }
                }
            }
        }

        progressEffect.setProgress((double) progress / MAX_PROGRESS);
        if (occupiers != null) {
            progressEffect.setColor(chatColorToColor(occupiers.getColor()));
        } else if (contestants != null) {
            progressEffect.setColor(chatColorToColor(contestants.getColor()));
        } else {
            progressEffect.setColor(Color.WHITE);
        }
    }
}
