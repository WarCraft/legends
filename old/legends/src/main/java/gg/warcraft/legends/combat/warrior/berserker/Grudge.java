package gg.warcraft.legends.combat.warrior.berserker;

import gg.warcraft.legends.combat.effect.warrior.berserker.GrudgeEffect;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.StatusEffect;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;

public class Grudge extends SimpleMonolithAbility {

    public Grudge(MonolithEntity caster, MonolithEntity target, AbilityConfiguration config) {
        super(caster, config);
        double heal = config.get(AbilityConfiguration.Attribute.HEAL);
        Duration duration = config.getDuration();

        StatusEffect grudge = new GrudgeStatusEffect(caster, heal);
        target.addStatusEffect(grudge);
        RunnableUtils.runLater(() -> target.removeStatusEffect(grudge), duration.as(Duration.Type.TICKS));

        new GrudgeEffect(target, duration);
    }

    @Override
    public String toString() {
        return "Grudge";
    }
}
