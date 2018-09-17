package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.service.SpecializationQueryService;
import gg.warcraft.legends.api.combat.LegendsSpecialization;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
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
import gg.warcraft.monolith.api.util.StringUtils;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultSpecializationMenuFactory extends AbstractMenuFactory implements SpecializationMenuFactory {
    private static final String YELLOW = ColorCode.YELLOW.toString();
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String RED = ColorCode.RED.toString();

    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final LegendsPlayerCommandService legendsPlayerCommandService;
    private final SpecializationQueryService specializationQueryService;
    private final MainMenuFactory mainMenuFactory;
    private final StringUtils stringUtils;
    private final TimeUtils timeUtils;

    @Inject
    public DefaultSpecializationMenuFactory(LegendsPlayerQueryService legendsPlayerQueryService,
                                            LegendsPlayerCommandService legendsPlayerCommandService,
                                            SpecializationQueryService specializationQueryService,
                                            MenuCommandService menuCommandService, MenuBuilderFactory menuBuilderFactory,
                                            ButtonBuilderFactory buttonBuilderFactory, MainMenuFactory mainMenuFactory,
                                            StringUtils stringUtils, TimeUtils timeUtils) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.legendsPlayerCommandService = legendsPlayerCommandService;
        this.specializationQueryService = specializationQueryService;
        this.mainMenuFactory = mainMenuFactory;
        this.stringUtils = stringUtils;
        this.timeUtils = timeUtils;
    }

    Button createInfoButton(Specialization specialization) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Choosing a specialization");
        String specializationName = specialization.getName();
        String specializationPrefix = stringUtils.isVowel(specializationName.charAt(0)) ? "an " : "a ";
        builder.addTooltip("You are currently fighting as " + specializationPrefix);
        builder.addTooltip(YELLOW + specializationName);
        builder.addTooltip("");
        builder.addTooltip("You can change your specialization");
        builder.addTooltip("once every 10 minutes.");
        return builder.build();
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

    Button createSpecializationButton(LegendsPlayer player, LegendsSpecialization legendsSpecialization,
                                      ItemType icon, List<String> lore, String cooldown) {
        String specializationName = legendsSpecialization.getName();
        Specialization specialization = specializationQueryService.getSpecialization(specializationName);
        boolean canSwitch = !player.getSpecialization().getName().equals(specializationName);

        ButtonBuilder builder = createSimpleButtonBuilder(icon, specializationName);
        lore.forEach(builder::addTooltip);
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), specializationName);
            }
        });
        return builder.build();
    }

    Button createArmsmasterButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Armsmasters wield sword and board. Their");
        lore.add("skills focus on control and defense.");
        return createSpecializationButton(player, LegendsSpecialization.ARMSMASTER, ItemType.WOOD_SWORD,
                lore, cooldown);
    }

    Button createBerserkerButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Berserkers wield an axe and the skulls");
        lore.add("of their enemies. Their skills focus on");
        lore.add("damage and recovering health.");
        return createSpecializationButton(player, LegendsSpecialization.BERSERKER, ItemType.WOOD_AXE,
                lore, cooldown);
    }

    Button createHunterButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Hunters use bow and arrow. Their skills");
        lore.add("offer damage, control, and mobility.");
        return createSpecializationButton(player, LegendsSpecialization.HUNTER, ItemType.BOW,
                lore, cooldown);
    }

    Button createMarksmanButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Marksmen use bow and arrow. Their skills");
        lore.add("focus on damage while sacrificing mobility.");
        return createSpecializationButton(player, LegendsSpecialization.MARKSMAN, ItemType.BOW,
                lore, cooldown);
    }

    Button createTempestButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Tempests wield a staff (hoe). Their");
        lore.add("skills offer large area damage.");
        return createSpecializationButton(player, LegendsSpecialization.TEMPEST, ItemType.WOOD_HOE,
                lore, cooldown);
    }

    Button createWizardButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Wizards carry a wand (stick) and book. Their");
        lore.add("skills focus on area control and mobility.");
        return createSpecializationButton(player, LegendsSpecialization.WIZARD, ItemType.STICK,
                lore, cooldown);
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

        MenuBuilder builder = createMenuBuilder("Specialization", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton(specialization));
        builder.addButton(2, createArmsmasterButton(player, cooldownText));
        builder.addButton(3, createBerserkerButton(player, cooldownText));
        builder.addButton(4, createHunterButton(player, cooldownText));
        builder.addButton(5, createMarksmanButton(player, cooldownText));
        builder.addButton(6, createTempestButton(player, cooldownText));
        builder.addButton(7, createWizardButton(player, cooldownText));
        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));
        return builder.build();
    }
}
