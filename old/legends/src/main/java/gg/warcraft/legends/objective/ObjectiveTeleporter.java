package gg.warcraft.legends.objective;

import gg.warcraft.legends.objective.effect.ObjectivePortalEffect;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ObjectiveTeleporter implements Runnable {
    private static final long PERIOD = 4;
    private static final EntityService entityService = EntityService.getInstance();
    private final Location entryLocation;
    private final Location exitLocation;
    private Predicate<LegendsPlayer> teleportPredicate;

    public ObjectiveTeleporter(Location entryLocation, Location exitLocation, Supplier<Race> teleportPredicate) {
        this.entryLocation = entryLocation;
        this.exitLocation = exitLocation;
        this.teleportPredicate = player -> player.getRace() == teleportPredicate.get();
        RunnableUtils.runTask(this, PERIOD, PERIOD);

        new ObjectivePortalEffect(entryLocation, () -> {
            Race race = teleportPredicate.get();
            if (race != null) {
                return chatColorToColor(teleportPredicate.get().getColor());
            } else {
                return Color.WHITE;
            }
        });
    }

    public void setTeleportPredicate(Predicate<LegendsPlayer> teleportPredicate) {
        this.teleportPredicate = teleportPredicate;
    }

    @Override
    public void run() {
        entityService.getNearbyMonolithEntities(entryLocation, 1).stream()
                .filter(entity -> entity instanceof MonolithPlayer)
                .map(entity -> (LegendsPlayer) entity)
                .filter(teleportPredicate)
                .forEach(player -> {
                    Vector direction = player.getEyeLocation().getDirection();
                    Location exitLocationWithDirection = exitLocation.clone();
                    exitLocationWithDirection.setDirection(direction);
                    player.teleport(exitLocationWithDirection, PlayerTeleportEvent.TeleportCause.PLUGIN);
                });
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
}
