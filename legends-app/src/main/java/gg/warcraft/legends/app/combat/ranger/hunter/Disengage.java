package gg.warcraft.legends.app.combat.ranger.hunter;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import org.joml.Vector3f;

public class Disengage extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_ENDERDRAGON_FLAP; // TODO use bat takeoff sound in stead?

    @Inject
    public Disengage(EntityCommandService entityCommandService, WorldCommandService worldCommandService,
                     @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                     @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        Vector3f direction = caster.getEyeLocation().getDirection();
        direction.y = 0;
        direction.mul(-1);
        float distance = snapshot.getFloat("distance");
        entityCommandService.knockback(caster.getId(), direction, distance);
        worldCommandService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void onHit(Entity entity) {
        // do nothing
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
        // TODO do we want a specific MeleeAbility flag which does NOT do melee target hit/miss checks i.e.
        // TODO insta cast self buffs? Maybe even make an entirely new SelfCastFactory?
    }
}
