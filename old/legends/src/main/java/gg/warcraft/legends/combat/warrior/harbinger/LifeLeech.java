package gg.warcraft.legends.combat.warrior.harbinger;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.potion.PotionEffectType;

public class LifeLeech extends SimpleMonolithAbility {

    public LifeLeech(MonolithEntity caster, MonolithEntity target, double heal, PotionConfiguration wither) {
        super(caster);
        caster.damage(-heal);
        target.addPotionEffect(PotionEffectType.WITHER, wither);
        // TODO: new StrikeEffect(target.getLocation(), ParticleEffect.DRIP_LAVA);
    }

    @Override
    public String toString() {
        return "Life Leech";
    }
}
