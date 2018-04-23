package gg.warcraft.legends.combat.warrior.armsmaster;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.legends.combat.effect.warrior.armsmaster.HamstringEffect;
import gg.warcraft.monolith.combat.SimpleTargetedMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class Hamstring extends SimpleTargetedMonolithAbility {

    public Hamstring(MonolithEntity caster, MonolithEntity target, AbilityConfiguration config) {
        this(LegendsCombatSound.HAMSTRING_HIT, caster, target, config);
    }

    public Hamstring(MonolithSound onHitSound, MonolithEntity caster, MonolithEntity target, AbilityConfiguration config) {
        super(caster, target, config);
        PotionConfiguration slow = config.getPotionConfiguration(PotionEffectType.SLOW);
        publishHitAndDamageEvents(this, target).forEach(damageEvent -> {
            damageEvent.getEntity().damage(damageEvent.getDamage());
            damageEvent.getEntity().addPotionEffect(PotionEffectType.SLOW, slow);
        });
        new HamstringEffect(target, slow.getDuration());
        onHitSound.play(target.getLocation());
    }

    @Override
    public String toString() {
        return "Hamstring";
    }
}
