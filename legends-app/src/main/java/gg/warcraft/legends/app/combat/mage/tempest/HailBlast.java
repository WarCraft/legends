package gg.warcraft.legends.app.combat.mage.tempest;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Hitbox;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;
import gg.warcraft.monolith.api.world.block.Block;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HailBlast extends AbstractAbility implements RangedAbility { // NOTE this should not be a melee ability as we want to fire a projectile

    // TODO use cone attribute from config
    @Inject
    public HailBlast(AbilityCommandService abilityCommandService, MovementFactory movementFactory,
                     HitboxFactory hitboxFactory, AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                     @Assisted("target") Entity rangedTarget, Block blockTarget) {
        super(caster.getId(), snapshot);
        PotionEffect slow = snapshot.getPotionEffect(PotionEffectType.SLOW);

        float range = snapshot.getFloat(AbilityConfigurationValue.RANGE);
        float speed = snapshot.getFloat(AbilityConfigurationValue.SPEED);
        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);

        Movement movement = movementFactory.createLinearMovement();
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());

        abilityCommandService.createProjectile(this, movement, hitbox, () -> {});


        ////////////////////


        Location location = caster.getLocation().add(0, 1, 0);
        Vector3f direction = caster.getLocation().getDirection();
        Location location1 = location.add(direction.mul(1));
        Location location2 = location.add(direction.mul(3.25f));
        Location location3 = location.add(direction.mul(6));
        Location location4 = location.add(direction.mul(9.25f));
        Location location5 = location.add(direction.mul(13));

        Set<MonolithEntity> entities = new HashSet<>();
        entities.addAll(entityService.getNearbyMonolithEntities(location1, 1));
        entities.addAll(entityService.getNearbyMonolithEntities(location2, 1.25));
        entities.addAll(entityService.getNearbyMonolithEntities(location3, 1.5));
        entities.addAll(entityService.getNearbyMonolithEntities(location4, 1.75));
        entities.addAll(entityService.getNearbyMonolithEntities(location5, 2));
        List<MonolithEntity> casualties = entities.stream()
                .filter(entity -> entity.getRace() != caster.getRace())
                .collect(Collectors.toList());
        publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
            MonolithEntity entity = damageEvent.getEntity();
            entity.damage(damageEvent.getDamage());
            entity.addPotionEffect(PotionEffectType.SLOW, slow);
        });

        onCastSound2.play(caster.getLocation());
        onCastSound.play(caster.getLocation());
        RunnableUtils.runLater(() -> onCastSound.play(caster.getLocation()), 2);
        RunnableUtils.runLater(() -> onCastSound.play(caster.getLocation()), 4);
        RunnableUtils.runLater(() -> onCastSound.play(caster.getLocation()), 6);
        RunnableUtils.runLater(() -> onCastSound.play(caster.getLocation()), 8);
        RunnableUtils.runLater(() -> onCastSound.play(caster.getLocation()), 10);
        RunnableUtils.runLater(() -> onCastSound.play(caster.getLocation()), 12);

        new HailBlastEffect(caster);
    }
}
