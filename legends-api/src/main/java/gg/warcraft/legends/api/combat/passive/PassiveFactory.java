package gg.warcraft.legends.api.combat.passive;

import com.google.inject.name.Named;
import gg.warcraft.monolith.api.entity.status.StatusEffect;

import java.util.UUID;

public interface PassiveFactory {

    @Named("humanMage")
    StatusEffect createHumanMagePassive(UUID entityId);

    @Named("humanWarrior")
    StatusEffect createHumanWarriorPassive(UUID entityId);

    @Named("orcMage")
    StatusEffect createOrcMagePassive(UUID entityId);

    @Named("orcWarrior")
    StatusEffect createOrcWarriorPassive(UUID entityId);
}
