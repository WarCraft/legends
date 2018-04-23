package gg.warcraft.legends.combat.warrior.armsmaster;

import gg.warcraft.legends.combat.effect.warrior.armsmaster.StandYourGroundEffect;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.PotionConfiguration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.potion.PotionEffectType;

public class StandYourGround extends SimpleMonolithAbility {

    public StandYourGround(MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        PotionConfiguration resistance = config.getPotionConfiguration(PotionEffectType.DAMAGE_RESISTANCE);
        PotionConfiguration fireResistance = config.getPotionConfiguration(PotionEffectType.FIRE_RESISTANCE);
        PotionConfiguration slow = config.getPotionConfiguration(PotionEffectType.SLOW);

        caster.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE, resistance);
        caster.addPotionEffect(PotionEffectType.FIRE_RESISTANCE, fireResistance);
        caster.addPotionEffect(PotionEffectType.SLOW, slow);

        AttributeInstance kbr = caster.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        kbr.setBaseValue(1);
        RunnableUtils.runLater(() -> kbr.setBaseValue(kbr.getDefaultValue()), slow.getDuration());

        new StandYourGroundEffect(caster, slow.getDuration());

        Location location = caster.getLocation();
        location.getWorld().playSound(location, Sound.ENTITY_IRONGOLEM_DEATH, 1, 1);
    }

    @Override
    public String toString() {
        return "Stand Your Ground";
    }
}
