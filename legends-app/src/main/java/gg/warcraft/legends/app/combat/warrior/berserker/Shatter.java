package gg.warcraft.legends.app.combat.warrior.berserker;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import org.joml.Vector3f;

public class Shatter extends AbstractAbility implements MeleeAbility {
    private final EntityQueryService entityQueryService;
    private final EntityCommandService entityCommandService;

    @Inject
    public Shatter(EntityQueryService entityQueryService, EntityCommandService entityCommandService,
                   @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                   @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityQueryService = entityQueryService;
        this.entityCommandService = entityCommandService;

        float distance = snapshot.getFloat(AbilityConfigurationValue.DISTANCE);
        Vector3f direction = caster.getEyeLocation().getDirection();
        entityCommandService.leap(caster.getId(), direction, distance);

        // TODO        this.task = RunnableUtils.runTask(this::tick, 1, 1);
    }

    private void tick() {
        Entity caster = entityQueryService.getEntity(getCasterId());
        /*
        if (caster.isOnGround()) {
            float radius = getSnapshot().getFloat(AbilityConfigurationValue.RADIUS);
            float launch = getSnapshot().getFloat(AbilityConfigurationValue.LAUNCH);
            PotionEffect confusion = getSnapshot().getPotionEffect(PotionEffectType.CONFUSION);

            List<Entity> casualties = entityQueryService.getNearbyEntities(caster.getLocation(), radius).stream()
                    .filter(new HostileTo(getCaster()))
                    .collect(Collectors.toList());
            publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
                damageEvent.getEntity().damage(damageEvent.getDamage());
                damageEvent.getEntity().knockup(launch);
                damageEvent.getEntity().addPotionEffect(PotionEffectType.CONFUSION, confusion);
            });
            task.cancel();
        }
        */
    }

    @Override
    public void onHit(Entity entity) {

    }

    @Override
    public void onMiss(Entity entity) {

    }
}
