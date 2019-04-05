package gg.warcraft.legends.app.combat.passive.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.event.PlayerSpecializationChangedEvent;
import gg.warcraft.legends.api.combat.passive.PassiveFactory;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.event.EntityTeamChangedEvent;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.status.Status;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.entity.status.service.StatusQueryService;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class PassiveAssignmentHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final StatusQueryService statusQueryService;
    private final StatusCommandService statusCommandService;
    private final PassiveFactory passiveFactory;
    private final Map<String, Function<UUID, StatusEffect>> passives;

    @Inject
    public PassiveAssignmentHandler(LegendsPlayerQueryService playerQueryService,
                                    StatusQueryService statusQueryService,
                                    StatusCommandService statusCommandService,
                                    PassiveFactory passiveFactory) {
        this.playerQueryService = playerQueryService;
        this.statusQueryService = statusQueryService;
        this.statusCommandService = statusCommandService;
        this.passiveFactory = passiveFactory;

        Map<String, Function<UUID, StatusEffect>> passives = new HashMap<>();
        passives.put("Human:warcraft.tempest", passiveFactory::createHumanMagePassive);
        passives.put("Human:warcraft.wizard", passiveFactory::createHumanMagePassive);
        passives.put("Human:warcraft.berserker", passiveFactory::createHumanWarriorPassive);
        passives.put("Human:warcraft.armsmaster", passiveFactory::createHumanWarriorPassive);

        passives.put("Orc:warcraft.tempest", passiveFactory::createOrcMagePassive);
        passives.put("Orc:warcraft.wizard", passiveFactory::createOrcMagePassive);
        passives.put("Orc:warcraft.berserker", passiveFactory::createOrcWarriorPassive);
        passives.put("Orc:warcraft.armsmaster", passiveFactory::createOrcWarriorPassive);
        this.passives = passives;
    }

    private void removePassives(Status status, Predicate<String> predicate) {
        status.getEffects().stream()
                .filter(effect -> predicate.test(effect.getType()))
                .forEach(effect -> statusCommandService.removeStatusEffect(effect, status.getEntityId()));
    }

    private void assignRacialPassive(Team team, Specialization specialization, UUID playerId) {
        if (team == null || specialization == null) {
            return;
        }

        String passiveKey = team.getName() + ":" + specialization.getPermission();
        Function<UUID, StatusEffect> newPassiveFactory = passives.get(passiveKey);
        if (newPassiveFactory != null) {
            StatusEffect newPassive = newPassiveFactory.apply(playerId);
            statusCommandService.addStatusEffect(newPassive, playerId);
        }
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Status status = statusQueryService.getStatus(playerId);
        removePassives(status, type -> true);

        // Add general effects
        StatusEffect attributesPassive = passiveFactory.createAttributesPassive(playerId);
        statusCommandService.addStatusEffect(attributesPassive, playerId);
        StatusEffect critPassive = passiveFactory.createCritPassive(playerId);
        statusCommandService.addStatusEffect(critPassive, playerId);

        Specialization specialization = player.getSpecialization();
        assignRacialPassive(player.getTeam(), specialization, playerId);
    }

    @Subscribe
    public void onPlayerRaceChangedEvent(EntityTeamChangedEvent event) {
        UUID playerId = event.getEntityId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            return;
        }

        Status status = statusQueryService.getStatus(playerId);
        removePassives(status, "legends.racial"::equals);

        Team newTeam = event.getNewTeam();
        Specialization specialization = player.getSpecialization();
        assignRacialPassive(newTeam, specialization, playerId);
    }

    @Subscribe
    public void onPlayerSpecializationChangedEvent(PlayerSpecializationChangedEvent event) {
        UUID playerId = event.getPlayerId();
        Player player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            return;
        }

        Status status = statusQueryService.getStatus(playerId);
        removePassives(status, "legends.racial"::equals);

        Team team = player.getTeam();
        Specialization newSpecialization = event.getNewSpecialization();
        assignRacialPassive(team, newSpecialization, playerId);
    }
}
