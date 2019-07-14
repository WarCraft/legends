package gg.warcraft.legends.app.combat.ranger.hunter;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;

import javax.annotation.Nullable;

public class ToxicArrow extends AbstractAbility implements MeleeAbility {
    private static final float SPEED = 3; // suggested speed of 0.6 by the spigot docs is way too low
    private static final float SPREAD = 2; // suggested spread of 12 by the spigot docs is way too wide

    private final EntityCommandService entityCommandService;

    @Inject
    public ToxicArrow(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                      @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                      @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        abilityCommandService.createArrow(this, caster.getId(), caster.getEyeLocation(),
                caster.getEyeOrientation().toVector(), SPEED, SPREAD);
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        PotionEffect poison = getSnapshot().getPotionEffect(PotionEffectType.POISON);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.addPotionEffect(entity.getId(), poison);
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}
