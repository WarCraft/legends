package gg.warcraft.legends.app.combat.ranger.hunter;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.sound.MonolithSound;
import org.bukkit.util.Vector;

public class Disengage extends SimpleMonolithAbility {

    public Disengage(MonolithEntity caster, double distance) {
        this(LegendsCombatSound.DISENGAGE_CAST, caster, distance);
    }

    public Disengage(MonolithSound onCastSound, MonolithEntity caster, double distance) {
        super(caster);
        Vector direction = caster.getEyeLocation().getDirection();
        direction.setY(0);
        direction.multiply(-1);
        caster.knockback(direction, distance);

        onCastSound.play(caster.getLocation()); // TODO bat takeoff sound in stead?
    }

    @Override
    public String toString() {
        return "Disengage";
    }
}
