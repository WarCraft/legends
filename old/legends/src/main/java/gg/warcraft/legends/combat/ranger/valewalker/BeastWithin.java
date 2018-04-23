package gg.warcraft.legends.combat.ranger.valewalker;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class BeastWithin extends SimpleMonolithAbility {

    public BeastWithin(MonolithEntity caster, PotionConfiguration jump, PotionConfiguration speed, PotionConfiguration regen,
                       PotionConfiguration damage, PotionConfiguration nightVision, PotionConfiguration saturation) {
        super(caster);
        caster.addPotionEffect(PotionEffectType.JUMP, jump);
        caster.addPotionEffect(PotionEffectType.SPEED, speed);
        caster.addPotionEffect(PotionEffectType.REGENERATION, regen);
        caster.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, damage);
        caster.addPotionEffect(PotionEffectType.NIGHT_VISION, nightVision);
        caster.addPotionEffect(PotionEffectType.SATURATION, saturation);
    }

    @Override
    public String toString() {
        return "Beast Within";
    }
}
