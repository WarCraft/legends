package gg.warcraft.legends.app.combat.passive;

import gg.warcraft.monolith.combat.event.MonolithEntityDamageEvent;
import gg.warcraft.monolith.combat.event.MonolithEntityHealthChangedEvent;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;
import org.bukkit.attribute.Attribute;

public class DwarfWarriorPassive extends AbstractStatusEffect {

    private final MonolithEntity entity;

    private double damageModifier;

    public DwarfWarriorPassive(MonolithEntity entity) {
        this.entity = entity;
        this.damageModifier = 1;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void onHealthChangedEvent(MonolithEntityHealthChangedEvent event) {
        System.out.println("MonolithEntityHealthChangedEvent for " + this.getClass().getSimpleName());
        double health = event.getEntity().getHealth();
        double maxHealth = event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        damageModifier = 1 - (0.25 * (1 - health / maxHealth));

        // TODO remove
        event.getEntity().sendMessage("[DwarfWarriorPassive]: damageModifier set to " + damageModifier);
    }

    @Override
    public void onDamageEvent(MonolithEntityDamageEvent event) {
        System.out.println("MonolithEntityDamageEvent for " + this.getClass().getSimpleName());
        // TODO remove
        double damage = event.getDamage();

        event.setDamage(event.getDamage() * damageModifier);

        // TODO remove
        event.getEntity().sendMessage("[DwarfWarriorPassive]: damage reduced from " + damage + " to " + event.getDamage() + " (modifier: " + damageModifier + ")");
    }
}
