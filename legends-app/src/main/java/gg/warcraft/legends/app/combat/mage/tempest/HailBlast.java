package gg.warcraft.legends.app.combat.mage.tempest;

import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.PotionEffectType;
import gg.warcraft.monolith.api.world.block.Block;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HailBlast extends AbstractAbility implements RangedAbility {

    // TODO use cone attribute from config
    public HailBlast(AbilitySnapshot snapshot,
                     @Assisted("caster") Entity caster, @Assisted("target") Entity rangedTarget, Block blockTarget) {
        super(caster.getId(), snapshot);
        PotionConfiguration slow = config.getPotionConfiguration(PotionEffectType.SLOW);

        Location location = caster.getLocation().add(0, 1, 0);
        Vector3f direction = caster.getLocation().getDirection();
        Location location1 = location.clone().add(direction.clone().multiply(1));
        Location location2 = location.clone().add(direction.clone().multiply(3.25));
        Location location3 = location.clone().add(direction.clone().multiply(6));
        Location location4 = location.clone().add(direction.clone().multiply(9.25));
        Location location5 = location.clone().add(direction.clone().multiply(13));

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
