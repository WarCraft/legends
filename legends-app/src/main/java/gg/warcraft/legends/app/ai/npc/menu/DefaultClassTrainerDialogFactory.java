package gg.warcraft.legends.app.ai.npc.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.menu.ClassTrainerDialogFactory;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.shop.Specialization;
import gg.warcraft.legends.api.shop.SpecializationId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.menu.AbstractMenuFactory;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.ButtonBuilder;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuSize;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultClassTrainerDialogFactory extends AbstractMenuFactory implements ClassTrainerDialogFactory {
    private static final String BLUE = ColorCode.AQUA.toString();

    private final LegendsPlayerQueryService playerQueryService;
    private final PlayerCommandService playerCommandService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final ShopProfileCommandService shopProfileCommandService;
    private final ShopQueryService shopQueryService;

    @Inject
    public DefaultClassTrainerDialogFactory(MenuCommandService menuCommandService,
                                            LegendsPlayerQueryService playerQueryService,
                                            PlayerCommandService playerCommandService,
                                            MenuBuilderFactory menuBuilderFactory,
                                            ButtonBuilderFactory buttonBuilderFactory,
                                            ShopProfileQueryService shopProfileQueryService,
                                            ShopProfileCommandService shopProfileCommandServices,
                                            ShopQueryService shopQueryService) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.shopProfileCommandService = shopProfileCommandServices;
        this.shopQueryService = shopQueryService;
    }

    private Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Class Trainers");
        builder.addTooltip("Class trainers allow you to train yourself");
        builder.addTooltip("in other disciplines. Their services don't");
        builder.addTooltip("come for free however and they first want");
        builder.addTooltip("you to prove yourself on the battlefield!");
        return builder.build();
    }

    private Button createSpecializationButton(UUID playerId, Specialization specialization, List<String> lore) {
        String specializationName = specialization.getName();
        ButtonBuilder builder = createSimpleButtonBuilder(specialization.getIcon(), specializationName);
        lore.forEach(builder::addTooltip);
        builder.addTooltip("");

        SpecializationId specializationId = SpecializationId.valueOf(specialization.getId());
        ShopProfile shopProfile = shopProfileQueryService.getProfile(playerId);
        if (!shopProfile.hasSpecialization(specializationId)) {
            builder.addTooltip(BLUE + "[SHIFT RIGHT CLICK] to unlock the " + specializationName);
            builder.addTooltip(BLUE + "for 5000 favor.");

            builder.withAction(click -> {
                if (click.isShiftRightClick()) {
                    LegendsPlayer player = playerQueryService.getPlayer(playerId);
                    if (player.getFavor() < 5000) {
                        playerCommandService.sendNotification(playerId, "You need at least 5000 favor to unlock this class.");
                        closeMenu(playerId);
                        return;
                    }

                    playerCommandService.removeCurrency(playerId, LegendsCurrency.FAVOR, 5000);
                    shopProfileCommandService.unlockSpecialization(specializationId, playerId, false);
                    playerCommandService.sendNotification(playerId, "You have unlocked the " + specializationName + "!");
                }
            });
        } else {
            builder.addTooltip("You already know all this trainer has to teach.");
        }
        return builder.build();
    }

    private Button createArmsmasterButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.ARMSMASTER.name());
        final List<String> lore = new ArrayList<>();

        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createBerserkerButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.BERSERKER.name());
        final List<String> lore = new ArrayList<>();

        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createHunterButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.HUNTER.name());
        final List<String> lore = new ArrayList<>();

        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createMarksmanButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.MARKSMAN.name());
        final List<String> lore = new ArrayList<>();

        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createTempestButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.TEMPEST.name());
        final List<String> lore = new ArrayList<>();

        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createWizardButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.WIZARD.name());
        final List<String> lore = new ArrayList<>();

        return createSpecializationButton(playerId, specialization, lore);
    }

    @Override
    public Menu createClassTrainerDialog(UUID playerId, String trainerName) {
        MenuBuilder builder = createMenuBuilder(trainerName, MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());

        switch (trainerName) {
            case "Warrior Trainer":
                builder.addButton(2, createArmsmasterButton(playerId));
                builder.addButton(3, createBerserkerButton(playerId));
                break;
            case "Hunter Trainer":
                builder.addButton(2, createHunterButton(playerId));
                builder.addButton(3, createMarksmanButton(playerId));
                break;
            case "Mage Trainer":
                builder.addButton(2, createTempestButton(playerId));
                builder.addButton(3, createWizardButton(playerId));
                break;
        }
        return builder.build();
    }
}
