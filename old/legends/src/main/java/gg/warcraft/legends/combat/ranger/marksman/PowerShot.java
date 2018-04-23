package gg.warcraft.legends.combat.ranger.marksman;

import gg.warcraft.legends.combat.LegendsCombatService;
import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.MonolithArrow;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.SimpleMonolithEntity;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.util.Vector;

public class PowerShot extends SimpleMonolithAbility implements MonolithArrow.Observer {

    private final MonolithArrow arrow;
    private final Vector direction;

    public PowerShot(MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        arrow = new MonolithArrow(caster, new HostileTo(caster));
        arrow.addObserver(this);
        direction = caster.getLocation().getDirection();
    }

    @Override
    public void onHit(MonolithEntity entity) {
        double knockback = getConfiguration().get(AbilityConfiguration.Attribute.KNOCKBACK);
        publishHitAndDamageEvents(this, entity).forEach(damageEvent -> {
            entity.damage(damageEvent.getDamage());
            entity.knockback(direction, knockback);

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
