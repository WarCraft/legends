package gg.warcraft.legends.combat.passive;

import gg.warcraft.legends.event.LegendsPlayerSpecializationChangedEvent;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.event.MonolithEntityRaceChangedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PassiveAssignmentListener implements Listener {
    private final LegendsPlayerService playerService;

    public PassiveAssignmentListener(LegendsPlayerService playerService) {
        this.playerService = playerService;
    }

    private void updatePassives(LegendsPlayer player) {
        player.getStatusEffects().iterator().forEachRemaining(player::removeStatusEffect);
        if (player.getRace() == null || player.getSpecialization() == null) {
            return;
        }
        switch (player.getSpecialization()) {
            case ARMSMASTER:
            case BERSERKER:
                switch (player.getRace()) {
                    case HUMAN:
                        player.addStatusEffect(new HumanWarriorPassive(player));
                        break;
                    case ORC:
                        player.addStatusEffect(new OrcWarriorPassive(player));
                        break;
                }
                break;
            case TEMPEST:
            case WIZARD:
                switch (player.getRace()) {
                    case HUMAN:
                        player.addStatusEffect(new HumanMagePassive(player));
                        break;
                    case ORC:
                        player.addStatusEffect(new OrcMagePassive(player));
                        break;
                }
                break;
        }
    }

    @EventHandler
    public void onMonolithEntityRaceChangedEvent(MonolithEntityRaceChangedEvent event) {
        LegendsPlayer player = playerService.getLegendsPlayer(event.getEntity().getUniqueId());
        if (player != null) {
            updatePassives(player);
        } else {
            System.out.println("Could not find LegendsPlayer for EntityRaceChangedEvent, this should never happen!");
        }
    }

    @EventHandler
    public void onLegendsPlayerSpecializationChangedEvent(LegendsPlayerSpecializationChangedEvent event) {
        updatePassives(event.getPlayer());
    }
}
