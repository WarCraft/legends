package gg.warcraft.legends.app.combat.passive.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.event.PlayerSpecializationChangedEvent;
import gg.warcraft.legends.api.combat.passive.PassiveFactory;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.status.Status;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.entity.status.service.StatusQueryService;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PassiveAssignmentHandler {
    private final PlayerQueryService playerQueryService;
    private final StatusQueryService statusQueryService;
    private final StatusCommandService statusCommandService;
    private final PassiveFactory passiveFactory;
    private final Map<String, Function<UUID, StatusEffect>> passives;

    @Inject
    public PassiveAssignmentHandler(PlayerQueryService playerQueryService, StatusQueryService statusQueryService,
                                    StatusCommandService statusCommandService, PassiveFactory passiveFactory) {
        this.playerQueryService = playerQueryService;
        this.statusQueryService = statusQueryService;
        this.statusCommandService = statusCommandService;
        this.passiveFactory = passiveFactory;

        Map<String, Function<UUID, StatusEffect>> passives = new HashMap<>();
        passives.put("Human:legends.tempest", passiveFactory::createHumanMagePassive);
        passives.put("Human:legends.wizard", passiveFactory::createHumanMagePassive);
        passives.put("Human:legends.berserker", passiveFactory::createHumanWarriorPassive);
        passives.put("Human:legends.armsmaster", passiveFactory::createHumanWarriorPassive);

        passives.put("Orc:legends.tempest", passiveFactory::createOrcMagePassive);
        passives.put("Orc:legends.wizard", passiveFactory::createOrcMagePassive);
        passives.put("Orc:legends.berserker", passiveFactory::createOrcWarriorPassive);
        passives.put("Orc:legends.armsmaster", passiveFactory::createOrcWarriorPassive);
        this.passives = passives;
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        StatusEffect attributesPassive = passiveFactory.createAttributesPassive(playerId);
        statusCommandService.addStatusEffect(attributesPassive, playerId);
        StatusEffect critPassive = passiveFactory.createCritPassive(playerId);
        statusCommandService.addStatusEffect(critPassive, playerId);
    }

    private void removeOldPassives(Status status) {
        status.getEffects().stream()
                .filter(effect -> "legends.racial".equals(effect.getType()))
                .forEach(effect -> statusCommandService.removeStatusEffect(effect, status.getEntityId()));
    }

    @Subscribe
    public void onPlayerSpecializationChangedEvent(PlayerSpecializationChangedEvent event) {
        UUID playerId = event.getPlayerId();
        Player player = playerQueryService.getPlayer(playerId);
        Status status = statusQueryService.getStatus(playerId);
        removeOldPassives(status);

        Team team = player.getTeam();
        if (team != null) {
            Specialization newSpecialization = event.getNewSpecialization();
            String newPassiveKey = team.getName() + ":" + newSpecialization.getPermission();
            Function<UUID, StatusEffect> newPassiveFactory = passives.get(newPassiveKey);
            if (newPassiveFactory != null) {
                StatusEffect newPassive = newPassiveFactory.apply(playerId);
                statusCommandService.addStatusEffect(newPassive, playerId);
            }
        }
    }
}
