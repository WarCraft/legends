package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.service.SpecializationQueryService;
import gg.warcraft.legends.api.combat.LegendsSpecialization;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.ButtonBuilder;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuSize;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultSpecializationMenuFactory implements SpecializationMenuFactory {
    private static final String YELLOW = ColorCode.YELLOW.toString();
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String RED = ColorCode.RED.toString();

    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final LegendsPlayerCommandService legendsPlayerCommandService;
    private final SpecializationQueryService specializationQueryService;
    private final MenuBuilderFactory menuBuilderFactory;
    private final ButtonBuilderFactory buttonBuilderFactory;
    private final TimeUtils timeUtils;

    @Inject
    public DefaultSpecializationMenuFactory(LegendsPlayerQueryService legendsPlayerQueryService,
                                            LegendsPlayerCommandService legendsPlayerCommandService,
                                            SpecializationQueryService specializationQueryService,
                                            MenuBuilderFactory menuBuilderFactory,
                                            ButtonBuilderFactory buttonBuilderFactory, TimeUtils timeUtils) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.legendsPlayerCommandService = legendsPlayerCommandService;
        this.specializationQueryService = specializationQueryService;
        this.menuBuilderFactory = menuBuilderFactory;
        this.buttonBuilderFactory = buttonBuilderFactory;
        this.timeUtils = timeUtils;
    }

    List<String> createAbilitiesView(Specialization specialization) {
        List<String> view = new ArrayList<>();
        view.add("Abilities:");
        view.add("[LEFT CLICK] " + YELLOW + specialization.getLeftClickAbility());
        view.add("[RIGHT CLICK] " + YELLOW + specialization.getRightClickAbility());
        view.add("[SHIFT LEFT CLICK] " + YELLOW + specialization.getShiftLeftClickAbility());
        view.add("[SHIFT RIGHT CLICK] " + YELLOW + specialization.getShiftRightClickAbility());
        return view;
    }

    List<String> createSwitchView(Specialization specialization, boolean canSwitch, String cooldown) {
        List<String> view = new ArrayList<>();
        if (canSwitch) {
            if (cooldown == null) {
                view.add(GREEN + "[SHIFT RIGHT CLICK] To switch to the " + specialization.getName());
            } else {
                view.add(RED + "You can change specializations in " + cooldown);
            }
        } else {
            view.add("This is your current specialization");
        }
        return view;
    }

    Button createInfoButton(Specialization specialization) {
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BOOK, "Choosing a specialization");
        builder.addTooltip("You are currently fighting as a");
        builder.addTooltip(YELLOW + specialization.getName());
        builder.addTooltip("");
        builder.addTooltip("You can change your specialization");
        builder.addTooltip("once every 10 minutes.");
        return builder.build();
    }

    Button createArmsmasterButton(LegendsPlayer player, String cooldown) {
        String armsmaster = LegendsSpecialization.ARMSMASTER.getName();
        Specialization specialization = specializationQueryService.getSpecialization(armsmaster);
        boolean canSwitch = !player.getSpecialization().getName().equals(armsmaster);

        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.WOOD_SWORD, armsmaster);
        builder.addTooltip("Armsmasters wield sword and board. Their");
        builder.addTooltip("skills focus on control and defense.");
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), armsmaster);
            }
        });
        return builder.build();
    }

    Button createBerserkerButton(LegendsPlayer player, String cooldown) {
        String berserker = LegendsSpecialization.BERSERKER.getName();
        Specialization specialization = specializationQueryService.getSpecialization(berserker);
        boolean canSwitch = !player.getSpecialization().getName().equals(berserker);

        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.WOOD_AXE, berserker);
        builder.addTooltip("Berserkers wield an axe and the skulls");
        builder.addTooltip("of their enemies. Their skills focus on");
        builder.addTooltip("damage and recovering health.");
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), berserker);
            }
        });
        return builder.build();
    }

    Button createHunterButton(LegendsPlayer player, String cooldown) {
        String hunter = LegendsSpecialization.HUNTER.getName();
        Specialization specialization = specializationQueryService.getSpecialization(hunter);
        boolean canSwitch = !player.getSpecialization().getName().equals(hunter);

        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BOW, hunter);
        builder.addTooltip("Hunters use bow and arrow. Their skills");
        builder.addTooltip("offer damage, control, and mobility.");
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), hunter);
            }
        });
        return builder.build();
    }

    Button createMarksmanButton(LegendsPlayer player, String cooldown) {
        String marksman = LegendsSpecialization.MARKSMAN.getName();
        Specialization specialization = specializationQueryService.getSpecialization(marksman);
        boolean canSwitch = !player.getSpecialization().getName().equals(marksman);

        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BOW, marksman);
        builder.addTooltip("Marksmen use bow and arrow. Their skills");
        builder.addTooltip("focus on damage while sacrificing mobility.");
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), marksman);
            }
        });
        return builder.build();
    }

    Button createTempestButton(LegendsPlayer player, String cooldown) {
        String tempest = LegendsSpecialization.TEMPEST.getName();
        Specialization specialization = specializationQueryService.getSpecialization(tempest);
        boolean canSwitch = !player.getSpecialization().getName().equals(tempest);

        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.WOOD_HOE, tempest);
        builder.addTooltip("Tempests wield a staff (hoe). Their");
        builder.addTooltip("skills offer large area damage.");
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), tempest);
            }
        });
        return builder.build();
    }

    Button createWizardButton(LegendsPlayer player, String cooldown) {
        String wizard = LegendsSpecialization.WIZARD.getName();
        Specialization specialization = specializationQueryService.getSpecialization(wizard);
        boolean canSwitch = !player.getSpecialization().getName().equals(wizard);

        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.STICK, wizard);
        builder.addTooltip("Wizards carry a wand (stick) and book. Their");
        builder.addTooltip("skills focus on area control and mobility.");
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), wizard);
            }
        });
        return builder.build();
    }

    @Override
    public Menu createSpecializationMenu(UUID playerId) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
        Specialization specialization = player.getSpecialization();
        long cooldown = player.getSpecializationCooldown();
        String cooldownText = null;
        if (cooldown > System.currentTimeMillis()) {
            cooldownText = timeUtils.getTimeToGoUntil(cooldown);
        }

        MenuBuilder builder = menuBuilderFactory.createMenuBuilder("Specialization", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton(specialization));
        builder.addButton(2, createArmsmasterButton(player, cooldownText));
        builder.addButton(3, createBerserkerButton(player, cooldownText));
        builder.addButton(4, createHunterButton(player, cooldownText));
        builder.addButton(5, createMarksmanButton(player, cooldownText));
        builder.addButton(6, createTempestButton(player, cooldownText));
        builder.addButton(7, createWizardButton(player, cooldownText));

        return builder.build();
    }
}
