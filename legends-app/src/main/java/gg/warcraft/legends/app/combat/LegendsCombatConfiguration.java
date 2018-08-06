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
import gg.warcraft.legends.app.combat.mage.wizard.ArcaneMissileFactory;
import gg.warcraft.legends.app.combat.mage.wizard.MassBamboozle;
import gg.warcraft.legends.app.combat.mage.wizard.MassBamboozleFactory;
import gg.warcraft.legends.app.combat.mage.wizard.Teleport;
import gg.warcraft.legends.app.combat.mage.wizard.TeleportFactory;
import gg.warcraft.legends.app.combat.ranger.hunter.Disengage;
import gg.warcraft.legends.app.combat.ranger.hunter.DisengageFactory;
import gg.warcraft.legends.app.combat.ranger.hunter.ToxicArrow;
import gg.warcraft.legends.app.combat.ranger.hunter.ToxicArrowFactory;
import gg.warcraft.legends.app.combat.ranger.hunter.Trap;
import gg.warcraft.legends.app.combat.ranger.hunter.TrapFactory;
import gg.warcraft.legends.app.combat.ranger.marksman.Focus;
import gg.warcraft.legends.app.combat.ranger.marksman.FocusFactory;
import gg.warcraft.legends.app.combat.ranger.marksman.PowerShot;
import gg.warcraft.legends.app.combat.ranger.marksman.PowerShotFactory;
import gg.warcraft.legends.app.combat.ranger.marksman.Sneak;
import gg.warcraft.legends.app.combat.ranger.marksman.SneakFactory;
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
        registerMageAbilities(abilityCommandService);
        registerRangerAbilities(abilityCommandService);
        registerWarriorAbilities(abilityCommandService);
    }

    private void registerMageAbilities(AbilityCommandService abilityCommandService) {
        abilityCommandService.registerRangedAbility("Fireball", Fireball.class, FireballFactory.class);
        // TODO register flame totem
        // Tempest
        abilityCommandService.registerRangedAbility("Bolt", Bolt.class, BoltFactory.class);
        abilityCommandService.registerRangedAbility("Hail Blast", HailBlast.class, HailBlastFactory.class);
        abilityCommandService.registerRangedAbility("Molten Storm", MoltenStorm.class, MoltenStormFactory.class);
        // Wizard
        abilityCommandService.registerRangedAbility("Arcane Missile", ArcaneMissile.class, ArcaneMissileFactory.class);
        abilityCommandService.registerRangedAbility("Mass Bamboozle", MassBamboozle.class, MassBamboozleFactory.class);
        abilityCommandService.registerRangedAbility("Teleport", Teleport.class, TeleportFactory.class);
    }

    private void registerRangerAbilities(AbilityCommandService abilityCommandService) {
        // Hunter
        abilityCommandService.registerMeleeAbility("Disengage", Disengage.class, DisengageFactory.class);
        abilityCommandService.registerMeleeAbility("Toxic Arrow", ToxicArrow.class, ToxicArrowFactory.class);
        abilityCommandService.registerMeleeAbility("Trap", Trap.class, TrapFactory.class);
        // Marksman
        abilityCommandService.registerMeleeAbility("Focus", Focus.class, FocusFactory.class);
        abilityCommandService.registerMeleeAbility("Power Shot", PowerShot.class, PowerShotFactory.class);
        abilityCommandService.registerMeleeAbility("Sneak", Sneak.class, SneakFactory.class);
    }

    private void registerWarriorAbilities(AbilityCommandService abilityCommandService) {
        // Berzerker
        abilityCommandService.registerMeleeAbility("Hamstring", Hamstring.class, HamstringFactory.class);
        abilityCommandService.registerMeleeAbility("Slam", Slam.class, SlamFactory.class);
        abilityCommandService.registerMeleeAbility("Stand Your Ground", StandYourGround.class, StandYourGroundFactory.class);
        // Armsmaster
        abilityCommandService.registerMeleeAbility("Grudge", Grudge.class, GrudgeFactory.class);
        abilityCommandService.registerMeleeAbility("Maul", Maul.class, MaulFactory.class);
        abilityCommandService.registerRangedAbility("Shatter", Shatter.class, ShatterFactory.class);
    }
}
