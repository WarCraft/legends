package gg.warcraft.legends.app.combat.warrior.berserker;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;

import java.util.UUID;

public class Maul extends AbstractAbility implements MeleeAbility {
    private final EntityCommandService entityCommandService;
    private final AbilitySnapshot snapshot;
    private final UUID casterId;

    @Inject
    public Maul(EntityCommandService entityCommandService, @Assisted AbilitySnapshot snapshot,
                @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.snapshot = snapshot;
        this.casterId = caster.getId();
    }

    @Override
    public void onHit(Entity entity) {
        PotionEffect absorption = snapshot.getPotionEffect(PotionEffectType.ABSORPTION);
        entityCommandService.addPotionEffect(casterId, absorption);
        entityCommandService.damage(entity.getId(), snapshot.getFloat(AbilityConfigurationValue.DAMAGE));

        // TODO create effect
        // TODO play hit sound
    }

    @Override
    public void onMiss(Entity entity) {
        // TODO play miss sound
    }
}
