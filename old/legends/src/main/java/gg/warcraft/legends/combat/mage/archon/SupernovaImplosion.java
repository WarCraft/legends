package gg.warcraft.legends.combat.mage.archon;

import gg.warcraft.legends.combat.effect.mage.archon.SupernovaImplosionEffect;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import org.bukkit.Location;
import org.bukkit.Sound;

public class SupernovaImplosion extends SimpleMonolithAbility {

    public SupernovaImplosion(MonolithEntity caster, Location location, double radius, double gravity) {
        this(EntityService.getInstance(), caster, location, radius, gravity);
    }

    public SupernovaImplosion(EntityService entityService, MonolithEntity caster, Location location, double radius, double gravity) {
        super(caster);
        new SupernovaImplosionEffect(location);
        location.getWorld().playSound(location, Sound.ENTITY_WITHER_DEATH, 1f, 1f);
        for (MonolithEntity entity : entityService.getNearbyMonolithEntities(location, radius)) {
            entity.knockback(location.toVector().subtract(entity.getLocation().toVector()).normalize(), gravity);
        }
    }

    @Override
    public String toString() {
        return "Supernova";
    }
}
