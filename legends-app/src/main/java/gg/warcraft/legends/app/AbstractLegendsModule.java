package gg.warcraft.legends.app;

import com.google.inject.PrivateModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import gg.warcraft.legends.api.combat.passive.PassiveFactory;
import gg.warcraft.legends.api.entity.LegendsEntityFactory;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.rank.service.RankCommandService;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.legends.api.menu.DecorationPackMenuFactory;
import gg.warcraft.legends.api.menu.DecorationsMenuFactory;
import gg.warcraft.legends.api.menu.LegendsMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.MiniPetsMenuFactory;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
import gg.warcraft.legends.api.menu.TeamMenuFactory;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileRepository;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
import gg.warcraft.legends.api.shop.service.ShopRepository;
import gg.warcraft.legends.app.combat.mage.effect.ArcaneMissileEffect;
import gg.warcraft.legends.app.combat.mage.effect.FireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.FlameTotemFireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.HailBlastEffect;
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.legends.app.combat.mage.effect.MoltenStormCircleEffect;
import gg.warcraft.legends.app.combat.passive.AttributesPassive;
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
import gg.warcraft.legends.app.entity.player.SimpleLegendsPlayer;
import gg.warcraft.legends.app.entity.player.rank.service.DefaultRankCommandService;
import gg.warcraft.legends.app.entity.player.rank.service.DefaultRankQueryService;
import gg.warcraft.legends.app.entity.player.rank.service.DefaultRankRepository;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerCommandService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerQueryService;
import gg.warcraft.legends.app.menu.DefaultDailyLoginMenuFactory;
import gg.warcraft.legends.app.menu.DefaultDecorationPackMenuFactory;
import gg.warcraft.legends.app.menu.DefaultDecorationsMenuFactory;
import gg.warcraft.legends.app.menu.DefaultLegendsMenuFactory;
import gg.warcraft.legends.app.menu.DefaultMainMenuFactory;
import gg.warcraft.legends.app.menu.DefaultMiniPetsMenuFactory;
import gg.warcraft.legends.app.menu.DefaultSpecializationMenuFactory;
import gg.warcraft.legends.app.menu.DefaultTeamMenuFactory;
import gg.warcraft.legends.app.shop.profile.service.DefaultShopProfileCommandService;
import gg.warcraft.legends.app.shop.profile.service.DefaultShopProfileQueryService;
import gg.warcraft.legends.app.shop.profile.service.DefaultShopProfileRepository;
import gg.warcraft.legends.app.shop.service.DefaultShopCommandService;
import gg.warcraft.legends.app.shop.service.DefaultShopQueryService;
import gg.warcraft.legends.app.shop.service.DefaultShopRepository;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.util.Cancellable;

public abstract class AbstractLegendsModule extends PrivateModule {

    @Override
    protected void configure() {
        configureCombat();
        configureMenu();
        configureEntity();
        configureShop();
    }

    private void configureCombat() {
        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("attributes"), AttributesPassive.class)
                .implement(StatusEffect.class, Names.named("humanMage"), HumanMagePassive.class)
                .implement(StatusEffect.class, Names.named("humanWarrior"), HumanWarriorPassive.class)
                .implement(StatusEffect.class, Names.named("orcMage"), OrcMagePassive.class)
                .implement(StatusEffect.class, Names.named("orcWarrior"), OrcWarriorPassive.class)
                .build(PassiveFactory.class));
        expose(PassiveFactory.class);

        // Effect factory bindings
        install(new FactoryModuleBuilder()
                .implement(Cancellable.class, Names.named("arcaneMissile"), ArcaneMissileEffect.class)
                .implement(Cancellable.class, Names.named("fireball"), FireballEffect.class)
                .implement(Cancellable.class, Names.named("hailBlast"), HailBlastEffect.class)
                .implement(Cancellable.class, Names.named("moltenStormRing"), MoltenStormCircleEffect.class)
                .implement(Cancellable.class, Names.named("flameTotemFireball"), FlameTotemFireballEffect.class)
                .build(MageEffectFactory.class));
        expose(MageEffectFactory.class);

        install(new FactoryModuleBuilder()
                .implement(Cancellable.class, Names.named("focus"), FocusEffect.class)
                .build(RangerEffectFactory.class));
        expose(RangerEffectFactory.class);

        install(new FactoryModuleBuilder()
                .implement(Cancellable.class, Names.named("grudge"), GrudgeEffect.class)
                .implement(Cancellable.class, Names.named("hamstring"), HamstringEffect.class)
                .implement(Cancellable.class, Names.named("standYourGround"), StandYourGroundEffect.class)
                .build(WarriorEffectFactory.class));
        expose(WarriorEffectFactory.class);

        // Status effect factory bindings
        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("focus"), FocusStatusEffect.class)
                .build(RangerStatusEffectFactory.class));
        expose(RangerStatusEffectFactory.class);

        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("grudge"), GrudgeStatusEffect.class)
                .implement(StatusEffect.class, Names.named("standYourGround"), StandYourGroundStatusEffect.class)
                .build(WarriorStatusEffectFactory.class));
        expose(WarriorStatusEffectFactory.class);
    }

    private void configureMenu() {
        bind(LegendsMenuFactory.class).to(DefaultLegendsMenuFactory.class);
        bind(MainMenuFactory.class).to(DefaultMainMenuFactory.class);
        bind(TeamMenuFactory.class).to(DefaultTeamMenuFactory.class);
        bind(SpecializationMenuFactory.class).to(DefaultSpecializationMenuFactory.class);
        bind(DailyLoginMenuFactory.class).to(DefaultDailyLoginMenuFactory.class);
        bind(DecorationsMenuFactory.class).to(DefaultDecorationsMenuFactory.class);
        bind(DecorationPackMenuFactory.class).to(DefaultDecorationPackMenuFactory.class);
        bind(MiniPetsMenuFactory.class).to(DefaultMiniPetsMenuFactory.class);

        expose(LegendsMenuFactory.class);
    }

    private void configureEntity() {
        // Entity bindings
        install(new FactoryModuleBuilder()
                .implement(LegendsPlayer.class, SimpleLegendsPlayer.class)
                .build(LegendsEntityFactory.class));
        expose(LegendsEntityFactory.class);

        // Player bindings
        bind(LegendsPlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        expose(LegendsPlayerCommandService.class);

        bind(LegendsPlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
        expose(LegendsPlayerQueryService.class);

        // Rank bindings
        bind(RankCommandService.class).to(DefaultRankCommandService.class);
        expose(RankCommandService.class);

        bind(RankQueryService.class).to(DefaultRankQueryService.class);
        expose(RankQueryService.class);

        bind(RankRepository.class).to(DefaultRankRepository.class);
        expose(RankRepository.class);
    }

    private void configureShop() {
        // Shop bindings
        bind(ShopCommandService.class).to(DefaultShopCommandService.class);
        expose(ShopCommandService.class);

        bind(ShopQueryService.class).to(DefaultShopQueryService.class);
        expose(ShopQueryService.class);

        bind(ShopRepository.class).to(DefaultShopRepository.class);
        expose(ShopRepository.class);

        // Shop profile bindings
        bind(ShopProfileCommandService.class).to(DefaultShopProfileCommandService.class);
        expose(ShopProfileCommandService.class);

        bind(ShopProfileQueryService.class).to(DefaultShopProfileQueryService.class);
        expose(ShopProfileQueryService.class);

        bind(ShopProfileRepository.class).to(DefaultShopProfileRepository.class);
        expose(ShopProfileRepository.class);
    }
}
