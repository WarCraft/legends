package gg.warcraft.legends.app;

import com.google.inject.PrivateModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import gg.warcraft.legends.api.ai.npc.menu.BlueprintVendorDialogFactory;
import gg.warcraft.legends.api.ai.npc.service.NpcCommandService;
import gg.warcraft.legends.api.ai.npc.service.NpcQueryService;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;
import gg.warcraft.legends.api.combat.passive.PassiveFactory;
import gg.warcraft.legends.api.core.MetricsService;
import gg.warcraft.legends.api.core.game.service.GameCommandService;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.game.service.GameRepository;
import gg.warcraft.legends.api.core.service.SpawnLocationRepository;
import gg.warcraft.legends.api.entity.LegendsEntityFactory;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.rank.service.RankAttributeModifierRepository;
import gg.warcraft.legends.api.entity.player.rank.service.RankCommandService;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.BlueprintBuilder;
import gg.warcraft.legends.api.item.BlueprintBuilderFactory;
import gg.warcraft.legends.api.item.ConsumableBuilder;
import gg.warcraft.legends.api.item.ConsumableBuilderFactory;
import gg.warcraft.legends.api.item.config.service.EquipmentConfigurationRepository;
import gg.warcraft.legends.api.item.crate.LootCrateBuilder;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateReader;
import gg.warcraft.legends.api.item.crate.LootCrateReaderFactory;
import gg.warcraft.legends.api.item.crate.service.LootCrateCommandService;
import gg.warcraft.legends.api.item.crate.service.LootCrateRepository;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilderFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator;
import gg.warcraft.legends.api.item.equipment.EquipmentGeneratorFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentModifier;
import gg.warcraft.legends.api.item.equipment.EquipmentModifierFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentReader;
import gg.warcraft.legends.api.item.equipment.EquipmentReaderFactory;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeBuilder;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeBuilderFactory;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeGenerator;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeGeneratorFactory;
import gg.warcraft.legends.api.menu.AdminMenuFactory;
import gg.warcraft.legends.api.menu.CollectablesMenuFactory;
import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.legends.api.menu.DecorationPackMenuFactory;
import gg.warcraft.legends.api.menu.DecorationsMenuFactory;
import gg.warcraft.legends.api.menu.LegendsMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.MiniPetsMenuFactory;
import gg.warcraft.legends.api.menu.PromotionsMenuFactory;
import gg.warcraft.legends.api.menu.ShopMenuFactory;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
import gg.warcraft.legends.api.menu.TeamMenuFactory;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileRepository;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
import gg.warcraft.legends.api.shop.service.ShopRepository;
import gg.warcraft.legends.app.ai.npc.menu.DefaultBlueprintVendorDialogFactory;
import gg.warcraft.legends.app.ai.npc.service.DefaultNpcCommandService;
import gg.warcraft.legends.app.ai.npc.service.DefaultNpcQueryService;
import gg.warcraft.legends.app.ai.npc.service.DefaultNpcRepository;
import gg.warcraft.legends.app.combat.mage.effect.ArcaneMissileEffect;
import gg.warcraft.legends.app.combat.mage.effect.FireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.FlameTotemFireballEffect;
import gg.warcraft.legends.app.combat.mage.effect.HailBlastEffect;
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.legends.app.combat.mage.effect.MassBamboozleCountdownEffect;
import gg.warcraft.legends.app.combat.mage.effect.MoltenStormCircleEffect;
import gg.warcraft.legends.app.combat.passive.AttributesPassive;
import gg.warcraft.legends.app.combat.passive.CritPassive;
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
import gg.warcraft.legends.app.core.AwsMetricsService;
import gg.warcraft.legends.app.core.game.service.DefaultGameCommandService;
import gg.warcraft.legends.app.core.game.service.DefaultGameQueryService;
import gg.warcraft.legends.app.core.game.service.DefaultGameRepository;
import gg.warcraft.legends.app.core.service.DefaultSpawnLocationRepository;
import gg.warcraft.legends.app.entity.player.SimpleLegendsPlayer;
import gg.warcraft.legends.app.entity.player.rank.handler.PrestigeAttributesHandler;
import gg.warcraft.legends.app.entity.player.rank.service.DefaultRankAttributeModifierRepository;
import gg.warcraft.legends.app.entity.player.rank.service.DefaultRankCommandService;
import gg.warcraft.legends.app.entity.player.rank.service.DefaultRankQueryService;
import gg.warcraft.legends.app.entity.player.rank.service.DefaultRankRepository;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerCommandService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerQueryService;
import gg.warcraft.legends.app.item.SimpleBlueprintBuilder;
import gg.warcraft.legends.app.item.SimpleConsumableBuilder;
import gg.warcraft.legends.app.item.config.service.DefaultEquipmentConfigurationRepository;
import gg.warcraft.legends.app.item.crate.SimpleLootCrateBuilder;
import gg.warcraft.legends.app.item.crate.SimpleLootCrateReader;
import gg.warcraft.legends.app.item.crate.handler.LootCrateUseHandler;
import gg.warcraft.legends.app.item.crate.service.DefaultLootCrateCommandService;
import gg.warcraft.legends.app.item.crate.service.DefaultLootCrateRepository;
import gg.warcraft.legends.app.item.equipment.SimpleEquipmentBuilder;
import gg.warcraft.legends.app.item.equipment.SimpleEquipmentGenerator;
import gg.warcraft.legends.app.item.equipment.SimpleEquipmentModifier;
import gg.warcraft.legends.app.item.equipment.SimpleEquipmentReader;
import gg.warcraft.legends.app.item.equipment.upgrade.SimpleEquipmentUpgradeBuilder;
import gg.warcraft.legends.app.item.equipment.upgrade.SimpleEquipmentUpgradeGenerator;
import gg.warcraft.legends.app.menu.DefaultAdminMenuFactory;
import gg.warcraft.legends.app.menu.DefaultCollectablesMenuFactory;
import gg.warcraft.legends.app.menu.DefaultDailyLoginMenuFactory;
import gg.warcraft.legends.app.menu.DefaultDecorationPackMenuFactory;
import gg.warcraft.legends.app.menu.DefaultDecorationsMenuFactory;
import gg.warcraft.legends.app.menu.DefaultLegendsMenuFactory;
import gg.warcraft.legends.app.menu.DefaultMainMenuFactory;
import gg.warcraft.legends.app.menu.DefaultMiniPetsMenuFactory;
import gg.warcraft.legends.app.menu.DefaultPromotionsMenuFactory;
import gg.warcraft.legends.app.menu.DefaultShopMenuFactory;
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
        configureAi();
        configureCombat();
        configureCore();
        configureEntity();
        configureItem();
        configureMenu();
        configureShop();
        configureSiege();
    }

    private void configureAi() {
        bind(NpcCommandService.class).to(DefaultNpcCommandService.class);
        expose(NpcCommandService.class);

        bind(NpcQueryService.class).to(DefaultNpcQueryService.class);
        expose(NpcQueryService.class);

        bind(NpcRepository.class).to(DefaultNpcRepository.class);
        expose(NpcRepository.class);
    }

    private void configureCombat() {
        install(new FactoryModuleBuilder()
                .implement(StatusEffect.class, Names.named("attributes"), AttributesPassive.class)
                .implement(StatusEffect.class, Names.named("crit"), CritPassive.class)
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
                .implement(Cancellable.class, Names.named("massBamboozle"), MassBamboozleCountdownEffect.class)
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

    private void configureCore() {
        bind(SpawnLocationRepository.class).to(DefaultSpawnLocationRepository.class);
        expose(SpawnLocationRepository.class);

        bind(MetricsService.class).to(AwsMetricsService.class);
        expose(MetricsService.class);

        // Game bindings
        bind(GameCommandService.class).to(DefaultGameCommandService.class);
        expose(GameCommandService.class);

        bind(GameQueryService.class).to(DefaultGameQueryService.class);
        expose(GameQueryService.class);

        bind(GameRepository.class).to(DefaultGameRepository.class);
        expose(GameRepository.class);
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

        bind(RankAttributeModifierRepository.class).to(DefaultRankAttributeModifierRepository.class);
        expose(RankAttributeModifierRepository.class);

        bind(PrestigeAttributesHandler.class);
        expose(PrestigeAttributesHandler.class);
    }

    private void configureItem() {
        // Equipment bindings
        bind(EquipmentConfigurationRepository.class).to(DefaultEquipmentConfigurationRepository.class);
        expose(EquipmentConfigurationRepository.class);

        bind(LootCrateUseHandler.class);
        expose(LootCrateUseHandler.class);

        install(new FactoryModuleBuilder()
                .implement(EquipmentReader.class, SimpleEquipmentReader.class)
                .build(EquipmentReaderFactory.class));
        expose(EquipmentReaderFactory.class);

        install(new FactoryModuleBuilder()
                .implement(EquipmentBuilder.class, SimpleEquipmentBuilder.class)
                .build(EquipmentBuilderFactory.class));
        expose(EquipmentBuilderFactory.class);

        install(new FactoryModuleBuilder()
                .implement(EquipmentGenerator.class, SimpleEquipmentGenerator.class)
                .build(EquipmentGeneratorFactory.class));
        expose(EquipmentGeneratorFactory.class);

        install(new FactoryModuleBuilder()
                .implement(EquipmentModifier.class, SimpleEquipmentModifier.class)
                .build(EquipmentModifierFactory.class));
        expose(EquipmentModifierFactory.class);

        // Equipment upgrade bindings
        install(new FactoryModuleBuilder()
                .implement(EquipmentUpgradeBuilder.class, SimpleEquipmentUpgradeBuilder.class)
                .build(EquipmentUpgradeBuilderFactory.class));
        expose(EquipmentUpgradeBuilderFactory.class);

        install(new FactoryModuleBuilder()
                .implement(EquipmentUpgradeGenerator.class, SimpleEquipmentUpgradeGenerator.class)
                .build(EquipmentUpgradeGeneratorFactory.class));
        expose(EquipmentUpgradeGeneratorFactory.class);

        // Consumable bindings
        install(new FactoryModuleBuilder()
                .implement(ConsumableBuilder.class, SimpleConsumableBuilder.class)
                .build(ConsumableBuilderFactory.class));
        expose(ConsumableBuilderFactory.class);

        // Loot crate bindings
        bind(LootCrateCommandService.class).to(DefaultLootCrateCommandService.class);
        expose(LootCrateCommandService.class);

        bind(LootCrateRepository.class).to(DefaultLootCrateRepository.class);
        expose(LootCrateRepository.class);

        install(new FactoryModuleBuilder()
                .implement(LootCrateBuilder.class, SimpleLootCrateBuilder.class)
                .build(LootCrateBuilderFactory.class));
        expose(LootCrateBuilderFactory.class);

        install(new FactoryModuleBuilder()
                .implement(LootCrateReader.class, SimpleLootCrateReader.class)
                .build(LootCrateReaderFactory.class));
        expose(LootCrateReaderFactory.class);
    }

    private void configureMenu() {
        bind(LegendsMenuFactory.class).to(DefaultLegendsMenuFactory.class);
        bind(MainMenuFactory.class).to(DefaultMainMenuFactory.class);
        bind(TeamMenuFactory.class).to(DefaultTeamMenuFactory.class);
        bind(SpecializationMenuFactory.class).to(DefaultSpecializationMenuFactory.class);
        bind(DailyLoginMenuFactory.class).to(DefaultDailyLoginMenuFactory.class);
        bind(CollectablesMenuFactory.class).to(DefaultCollectablesMenuFactory.class);
        bind(DecorationsMenuFactory.class).to(DefaultDecorationsMenuFactory.class);
        bind(DecorationPackMenuFactory.class).to(DefaultDecorationPackMenuFactory.class);
        bind(MiniPetsMenuFactory.class).to(DefaultMiniPetsMenuFactory.class);
        bind(PromotionsMenuFactory.class).to(DefaultPromotionsMenuFactory.class);
        bind(ShopMenuFactory.class).to(DefaultShopMenuFactory.class);
        bind(AdminMenuFactory.class).to(DefaultAdminMenuFactory.class);

        expose(LegendsMenuFactory.class);
        expose(AdminMenuFactory.class);
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

    private void configureSiege() {
        // Blueprints
        bind(BlueprintVendorDialogFactory.class).to(DefaultBlueprintVendorDialogFactory.class);
        expose(BlueprintVendorDialogFactory.class);

        install(new FactoryModuleBuilder()
                .implement(BlueprintBuilder.class, SimpleBlueprintBuilder.class)
                .build(BlueprintBuilderFactory.class));
        expose(BlueprintBuilderFactory.class);
    }
}
