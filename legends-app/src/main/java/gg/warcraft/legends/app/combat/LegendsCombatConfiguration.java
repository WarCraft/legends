package gg.warcraft.legends.app.combat;

import com.google.inject.Inject;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.legends.app.combat.mage.Fireball;
import gg.warcraft.legends.app.combat.mage.FireballFactory;
import gg.warcraft.legends.app.combat.mage.tempest.Bolt;
import gg.warcraft.legends.app.combat.mage.tempest.BoltFactory;
import gg.warcraft.legends.app.combat.mage.tempest.HailBlast;
import gg.warcraft.legends.app.combat.mage.tempest.HailBlastFactory;
import gg.warcraft.legends.app.combat.mage.tempest.MoltenStorm;
import gg.warcraft.legends.app.combat.mage.tempest.MoltenStormFactory;
import gg.warcraft.legends.app.combat.mage.wizard.ArcaneMissile;
import gg.warcraft.legends.app.combat.mage.wizard.Teleport;
import gg.warcraft.legends.app.combat.warrior.armsmaster.Hamstring;
import gg.warcraft.legends.app.combat.warrior.armsmaster.HamstringFactory;
import gg.warcraft.legends.app.combat.warrior.armsmaster.Slam;
import gg.warcraft.legends.app.combat.warrior.armsmaster.SlamFactory;
import gg.warcraft.legends.app.combat.warrior.armsmaster.StandYourGround;
import gg.warcraft.legends.app.combat.warrior.armsmaster.StandYourGroundFactory;
import gg.warcraft.legends.app.combat.warrior.berserker.Grudge;
import gg.warcraft.legends.app.combat.warrior.berserker.GrudgeFactory;
import gg.warcraft.legends.app.combat.warrior.berserker.Maul;
import gg.warcraft.legends.app.combat.warrior.berserker.MaulFactory;
import gg.warcraft.legends.app.combat.warrior.berserker.Shatter;
import gg.warcraft.legends.app.combat.warrior.berserker.ShatterFactory;

public class LegendsCombatConfiguration {

    @Inject
    public LegendsCombatConfiguration(AbilityCommandService abilityCommandService) {
        // Register mage abilities
        abilityCommandService.registerRangedAbility("Fireball", Fireball.class, FireballFactory.class);
        // TODO register flame totem
        abilityCommandService.registerRangedAbility("Bolt", Bolt.class, BoltFactory.class);
        abilityCommandService.registerRangedAbility("Hail Blast", HailBlast.class, HailBlastFactory.class);
        abilityCommandService.registerRangedAbility("Molten Storm", MoltenStorm.class, MoltenStormFactory.class);
        abilityCommandService.registerRangedAbility("Arcane Missile", ArcaneMissile.class, ArcaneMissileFactory.class);
        abilityCommandService.registerRangedAbility("Mass Bamboozle", MassBamboozle.class, MassBamboozleFactory.class);
        abilityCommandService.registerRangedAbility("Teleport", Teleport.class, TeleportFactory.class);

        // Register ranger abilities
        // TODO

        // Register warrior abilities
        abilityCommandService.registerMeleeAbility("Hamstring", Hamstring.class, HamstringFactory.class);
        abilityCommandService.registerMeleeAbility("Slam", Slam.class, SlamFactory.class);
        abilityCommandService.registerMeleeAbility("Stand Your Ground", StandYourGround.class, StandYourGroundFactory.class);
        abilityCommandService.registerMeleeAbility("Grudge", Grudge.class, GrudgeFactory.class);
        abilityCommandService.registerMeleeAbility("Maul", Maul.class, MaulFactory.class);
        abilityCommandService.registerMeleeAbility("Shatter", Shatter.class, ShatterFactory.class);
    }
}
