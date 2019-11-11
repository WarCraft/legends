package gg.warcraft.legends.app.combat.warrior.berserker;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.WorldService;

import java.util.UUID;

public class Maul extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound HIT_SOUND = Sound.ENTITY_ZOMBIE_STEP;
    private static final Sound MISS_SOUND = Sound.ENTITY_FISHING_BOBBER_THROW;

    private final EntityCommandService entityCommandService;
    private final WorldService WorldService;
    private final AbilitySnapshot snapshot;
    private final UUID casterId;

    @Inject
    public Maul(EntityCommandService entityCommandService, WorldService WorldService,
                @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.WorldService = WorldService;
        this.snapshot = snapshot;
        this.casterId = caster.getId();
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue damage = snapshot.getValue(AbilityConfigurationValue.DAMAGE);
        PotionEffect absorption = snapshot.getPotionEffect(PotionEffectType.ABSORPTION);
        entityCommandService.addPotionEffect(casterId, absorption);
        entityCommandService.damage(entity.getId(), damage);

        // TODO create effect
        WorldService.playSound(entity.getLocation(), HIT_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void onMiss(Entity entity) {
        WorldService.playSound(entity.getLocation(), MISS_SOUND, SOUND_CATEGORY, 1, 1);
    }
}
