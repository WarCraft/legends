package gg.warcraft.legends.combat.warrior.berserker;

import gg.warcraft.monolith.combat.event.MonolithEntityDamageEvent;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;
import org.bukkit.attribute.Attribute;

public class GrudgeStatusEffect extends AbstractStatusEffect {

    private final MonolithEntity berserker;
    private final double heal;

    public GrudgeStatusEffect(MonolithEntity berserker, double heal) {
        this.berserker = berserker;
        this.heal = heal;
    }

    @Override
    public boolean hasExpired() {
        return false; // TODO rethink expired func, could check everytime they are iterated and remove all expired
    }

    @Override
    public void onDamageEvent(MonolithEntityDamageEvent event) {
        if (event.getDamager() != null) {
            double maxHealth = berserker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (berserker.getHealth() + heal <= maxHealth) {
                berserker.sendMessage("[Grudge] Healing you to " + (berserker.getHealth() + heal));
                berserker.setHealth(berserker.getHealth() + heal);
            } else {
                berserker.sendMessage("[Grudge] Healing you to full");
                berserker.setHealth(maxHealth);
            }
        }
    }
}
