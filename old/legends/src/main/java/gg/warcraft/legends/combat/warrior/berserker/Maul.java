package gg.warcraft.legends.combat.warrior.berserker;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.monolith.combat.SimpleTargetedMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class Maul extends SimpleTargetedMonolithAbility {

    public Maul(MonolithEntity caster, MonolithEntity target, AbilityConfiguration config) {
        this(LegendsCombatSound.MAUL_HIT, caster, target, config);
    }

    public Maul(MonolithSound onHitSound, MonolithEntity caster, MonolithEntity target, AbilityConfiguration config) {
        super(caster, target, config);
        PotionConfiguration absorption = config.getPotionConfiguration(PotionEffectType.ABSORPTION);

        publishHitAndDamageEvents(this, target).forEach(damageEvent -> {
            caster.addPotionEffect(PotionEffectType.ABSORPTION, absorption);
            damageEvent.getEntity().damage(damageEvent.getDamage());
        });

        onHitSound.play(target.getLocation());
    }

    @Override
    public String toString() {
        return "Maul";
    }
}
