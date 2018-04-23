package gg.warcraft.legends.combat.warrior.armsmaster;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.monolith.combat.SimpleTargetedMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class Slam extends SimpleTargetedMonolithAbility {

    public Slam(MonolithEntity caster, MonolithEntity target, AbilityConfiguration config) {
        this(LegendsCombatSound.SLAM_HIT, caster, target, config);
    }

    public Slam(MonolithSound onHitSound, MonolithEntity caster, MonolithEntity target, AbilityConfiguration config) {
        super(caster, target, config);
        double knockback = config.get(AbilityConfiguration.Attribute.KNOCKBACK);
        PotionConfiguration confusion = config.getPotionConfiguration(PotionEffectType.CONFUSION);
        publishHitAndDamageEvents(this, target).forEach(damageEvent -> {
            damageEvent.getEntity().damage(damageEvent.getDamage());
            damageEvent.getEntity().knockback(getCaster().getEyeLocation().getDirection(), knockback);
            damageEvent.getEntity().addPotionEffect(PotionEffectType.CONFUSION, confusion);
        });
        onHitSound.play(target.getLocation());
    }

    @Override
    public String toString() {
        return "Slam";
    }
}
