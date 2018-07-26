package gg.warcraft.legends.app;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerProfileRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.combat.mage.effect.FireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.FlameTotemFireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.legends.app.combat.mage.effect.MoltenStormCircleEffect;
import gg.warcraft.legends.app.combat.warrior.effect.HamstringEffect;
import gg.warcraft.legends.app.combat.warrior.effect.StandYourGroundEffect;
import gg.warcraft.legends.app.combat.warrior.effect.WarriorEffectFactory;
import gg.warcraft.legends.app.combat.warrior.status.StandYourGroundStatusEffect;
import gg.warcraft.legends.app.combat.warrior.status.WarriorStatusEffectFactory;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerCommandService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerProfileRepository;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerQueryService;
import gg.warcraft.monolith.api.effect.Effect;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerProfileRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;

public abstract class AbstractLegendsModule extends AbstractModule {

    @Override
    protected void configure() {
        configureCombat();
        configureEntity();
    }

    private void configureCombat() {
        install(new FactoryModuleBuilder()
                .implement(Effect.class, Names.named("fireball"), FireballEffect.class)
                .implement(Effect.class, Names.named("moltenStormRing"), MoltenStormCircleEffect.class)
                .implement(Effect.class, Names.named("flameTotemFireball"), FlameTotemFireballEffect.class)
                .build(MageEffectFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Effect.class, Names.named("hamstring"), HamstringEffect.class)
                .implement(Effect.class, Names.named("standYourGround"), StandYourGroundEffect.class)
                .build(WarriorEffectFactory.class));

        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("standYourGround"), StandYourGroundStatusEffect.class)
                .build(WarriorStatusEffectFactory.class));
    }

    private void configureEntity() {
        bind(PlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        bind(LegendsPlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        bind(PlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
        bind(LegendsPlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
        bind(PlayerProfileRepository.class).to(DefaultLegendsPlayerProfileRepository.class);
        bind(LegendsPlayerProfileRepository.class).to(DefaultLegendsPlayerProfileRepository.class);
    }
}
