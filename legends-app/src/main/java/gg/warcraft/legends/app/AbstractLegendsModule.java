package gg.warcraft.legends.app;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import gg.warcraft.legends.api.combat.passive.PassiveFactory;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.combat.mage.effect.ArcaneMissileEffect;
import gg.warcraft.legends.app.combat.mage.effect.FireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.FlameTotemFireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.HailBlastEffect;
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.legends.app.combat.mage.effect.MoltenStormCircleEffect;
import gg.warcraft.legends.app.combat.passive.HumanMagePassive;
import gg.warcraft.legends.app.combat.passive.HumanWarriorPassive;
import gg.warcraft.legends.app.combat.passive.OrcMagePassive;
import gg.warcraft.legends.app.combat.passive.OrcWarriorPassive;
import gg.warcraft.legends.app.combat.ranger.effect.FocusEffect;
import gg.warcraft.legends.app.combat.ranger.effect.RangerEffectFactory;
import gg.warcraft.legends.app.combat.ranger.status.FocusStatusEffect;
import gg.warcraft.legends.app.combat.ranger.status.RangerStatusEffectFactory;
import gg.warcraft.legends.app.combat.warrior.effect.GrudgeEffect;
import gg.warcraft.legends.app.combat.warrior.effect.HamstringEffect;
import gg.warcraft.legends.app.combat.warrior.effect.StandYourGroundEffect;
import gg.warcraft.legends.app.combat.warrior.effect.WarriorEffectFactory;
import gg.warcraft.legends.app.combat.warrior.status.GrudgeStatusEffect;
import gg.warcraft.legends.app.combat.warrior.status.StandYourGroundStatusEffect;
import gg.warcraft.legends.app.combat.warrior.status.WarriorStatusEffectFactory;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerCommandService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.util.Cancellable;

public abstract class AbstractLegendsModule extends AbstractModule {

    @Override
    protected void configure() {
        configureCombat();
        configureEntity();
    }

    private void configureCombat() {
        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("humanMage"), HumanMagePassive.class)
                .implement(StatusEffect.class, Names.named("humanWarrior"), HumanWarriorPassive.class)
                .implement(StatusEffect.class, Names.named("orcMage"), OrcMagePassive.class)
                .implement(StatusEffect.class, Names.named("orcWarrior"), OrcWarriorPassive.class)
                .build(PassiveFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Cancellable.class, Names.named("arcaneMissile"), ArcaneMissileEffect.class)
                .implement(Cancellable.class, Names.named("fireball"), FireballEffect.class)
                .implement(Cancellable.class, Names.named("hailBlast"), HailBlastEffect.class)
                .implement(Cancellable.class, Names.named("moltenStormRing"), MoltenStormCircleEffect.class)
                .implement(Cancellable.class, Names.named("flameTotemFireball"), FlameTotemFireballEffect.class)
                .build(MageEffectFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Cancellable.class, Names.named("focus"), FocusEffect.class)
                .build(RangerEffectFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Cancellable.class, Names.named("grudge"), GrudgeEffect.class)
                .implement(Cancellable.class, Names.named("hamstring"), HamstringEffect.class)
                .implement(Cancellable.class, Names.named("standYourGround"), StandYourGroundEffect.class)
                .build(WarriorEffectFactory.class));

        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("focus"), FocusStatusEffect.class)
                .build(RangerStatusEffectFactory.class));

        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("grudge"), GrudgeStatusEffect.class)
                .implement(StatusEffect.class, Names.named("standYourGround"), StandYourGroundStatusEffect.class)
                .build(WarriorStatusEffectFactory.class));
    }

    private void configureEntity() {
        bind(LegendsPlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        bind(LegendsPlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
    }
}
