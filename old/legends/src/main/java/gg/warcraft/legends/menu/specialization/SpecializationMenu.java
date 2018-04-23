package gg.warcraft.legends.menu.specialization;

import gg.warcraft.legends.combat.Specialization;
import gg.warcraft.legends.event.LegendsPlayerSpecializationChangedEvent;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.command.CommandService;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpecializationMenu extends Menu {
    private static final Long COOLDOWN = 10L * TimeUtils.MILLIS_PER_MINUTE;
    private static final Map<UUID, Long> timeOfLastSpecChange = new HashMap<>();

    private final CommandService commandService;
    private final EventService eventService;
    private final LegendsPlayer player;

    public SpecializationMenu(LegendsPlayer viewer) {
        this(viewer, CommandService.getInstance(), EventService.getInstance());
    }

    public SpecializationMenu(LegendsPlayer viewer, CommandService commandService, EventService eventService) {
        super(viewer, "Specialization");
        this.commandService = commandService;
        this.eventService = eventService;
        this.player = viewer;
        update(ButtonSlot.FIRST, new BerserkerButton(viewer, () -> setSpecialization(Specialization.BERSERKER)));
        update(ButtonSlot.SECOND, new ArmsmasterButton(viewer, () -> setSpecialization(Specialization.ARMSMASTER)));
        update(ButtonSlot.THIRD, new MarksmanButton(viewer, () -> setSpecialization(Specialization.MARKSMAN)));
        update(ButtonSlot.FOURTH, new HunterButton(viewer, () -> setSpecialization(Specialization.HUNTER)));
        update(ButtonSlot.FIFTH, new TempestButton(viewer, () -> setSpecialization(Specialization.TEMPEST)));
        update(ButtonSlot.SIXTH, new WizardButton(viewer, () -> setSpecialization(Specialization.WIZARD)));
    }

    public void setSpecialization(Specialization specialization) {
        Runnable settingTheSpec = () -> {
            player.setSpecialization(specialization);
            player.sendMessage(ChatColor.YELLOW + "You are now a " + specialization.name().toLowerCase() +
                    ", if you do not already have a weapon for this specialization you can get a basic one via the /kit command.");
            eventService.publishEvent(new LegendsPlayerSpecializationChangedEvent(player));
            close();
        };

        if (player.getWorld().getName().equals("Ouros_the_end")) {
            settingTheSpec.run();
            return;
        }

        long currentTime = System.currentTimeMillis();
        long cooldown = timeOfLastSpecChange.getOrDefault(player.getUniqueId(), 0L);
        if (cooldown <= currentTime) {
            timeOfLastSpecChange.put(player.getUniqueId(), currentTime + COOLDOWN);
            settingTheSpec.run();
        } else {
            int timeLeft = ((int) (cooldown - currentTime) / TimeUtils.MILLIS_PER_MINUTE);
            String suffix;
            if (timeLeft == 0) {
                timeLeft = ((int) (cooldown - currentTime) / TimeUtils.MILLIS_PER_SECOND);
                suffix = timeLeft == 1 ? " second." : " seconds.";
            } else {
                suffix = timeLeft == 1 ? " minute." : " minutes.";
            }
            player.sendMessage(ChatColor.YELLOW + "Sorry you can only change your spec once every 10 minutes. Please wait "
                    + timeLeft + suffix);
        }
    }
}
