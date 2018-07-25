package gg.warcraft.legends.app.combat.ranger.hunter;

import gg.warcraft.legends.combat.LegendsCombatService;
import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.MonolithArrow;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.SimpleMonolithEntity;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffectType;

public class ToxicArrow extends SimpleMonolithAbility implements MonolithArrow.Observer {

    public ToxicArrow(MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        MonolithArrow arrow = new MonolithArrow(caster, new HostileTo(caster));
        arrow.addObserver(this);
    }

    @Override
    public void onHit(MonolithEntity entity) {
        PotionConfiguration poison = getConfiguration().getPotionConfiguration(PotionEffectType.POISON);
        publishHitAndDamageEvents(this, entity).forEach(damageEvent -> {
            entity.damage(damageEvent.getDamage());
            entity.addPotionEffect(PotionEffectType.POISON, poison);

            // TODO refactor into single place
            if (getCaster() instanceof MonolithPlayer) {
                Tameable pet = LegendsCombatService.getInstance().getCombatPetFor((MonolithPlayer) getCaster());
                if (pet != null && pet instanceof Wolf) {
                    ((Wolf) pet).setTarget(((SimpleMonolithEntity) entity).getLivingEntity());
                }
            }
        });
    }

    @Override
    public String toString() {
        return "Power Shot";
    }
}
