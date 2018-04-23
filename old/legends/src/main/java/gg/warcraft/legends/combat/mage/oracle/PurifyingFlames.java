package gg.warcraft.legends.combat.mage.oracle;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class PurifyingFlames extends SimpleMonolithAbility {

    public PurifyingFlames(MonolithEntity caster, MonolithEntity target, double damage, PotionConfiguration regen) {
        super(caster);
        target.damage(damage);
        target.addPotionEffect(PotionEffectType.REGENERATION, regen);
        target.setFireTicks((int) regen.getDuration().as(TICKS));
    }

    @Override
    public String toString() {
        return "Purifying Flames";
    }
}
