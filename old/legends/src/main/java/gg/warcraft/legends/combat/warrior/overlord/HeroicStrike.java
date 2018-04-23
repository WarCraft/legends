package gg.warcraft.legends.combat.warrior.overlord;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class HeroicStrike extends SimpleMonolithAbility {

    public HeroicStrike(MonolithEntity caster, MonolithEntity target, double damage, PotionConfiguration weakness) {
        super(caster);
        target.damage(damage);
        target.addPotionEffect(PotionEffectType.WEAKNESS, weakness);
        // TODO: new StrikeEffect(target.getEyeLocation(), ParticleEffect.CRIT);
    }

    @Override
    public String toString() {
        return "Heroic Strike";
    }
}
