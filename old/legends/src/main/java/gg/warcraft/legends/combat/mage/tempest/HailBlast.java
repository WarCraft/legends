package gg.warcraft.legends.combat.mage.tempest;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.legends.combat.effect.mage.tempest.HailBlastEffect;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.PotionConfiguration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HailBlast extends SimpleMonolithAbility {

    public HailBlast(MonolithEntity caster, AbilityConfiguration config) {
        this(EntityService.getInstance(), LegendsCombatSound.HAILBLAST_CAST, LegendsCombatSound.HAILBLAST_CAST2,
                caster, config);
    }

    // TODO use cone attribute from config
    public HailBlast(EntityService entityService, LegendsCombatSound onCastSound, LegendsCombatSound onCastSound2,
                     MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        PotionConfiguration slow = config.getPotionConfiguration(PotionEffectType.SLOW);

        Location location = caster.getLocation().add(0, 1, 0);
        Vector direction = caster.getLocation().getDirection();
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

    @Override
    public String toString() {
        return "Hail Blast";
    }
}
