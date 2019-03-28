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
import gg.warcraft.legends.api.shop.SpecializationId;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DefaultSpecializationMenuFactory extends AbstractMenuFactory implements SpecializationMenuFactory {
    private static final String YELLOW = ColorCode.YELLOW.toString();
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String RED = ColorCode.RED.toString();
    private static final Map<String, SpecializationId> specializationNameToId;

    static {
        specializationNameToId = new HashMap<>();
        specializationNameToId.put("Armsmaster", SpecializationId.ARMSMASTER);
        specializationNameToId.put("Berserker", SpecializationId.BERSERKER);
        specializationNameToId.put("Hunter", SpecializationId.HUNTER);
        specializationNameToId.put("Marksman", SpecializationId.MARKSMAN);
        specializationNameToId.put("Tempest", SpecializationId.TEMPEST);
        specializationNameToId.put("Wizard", SpecializationId.WIZARD);
    }

    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final LegendsPlayerCommandService legendsPlayerCommandService;
    private final SpecializationQueryService specializationQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final MainMenuFactory mainMenuFactory;
    private final StringUtils stringUtils;
    private final TimeUtils timeUtils;

    @Inject
    public DefaultSpecializationMenuFactory(LegendsPlayerQueryService legendsPlayerQueryService,
                                            LegendsPlayerCommandService legendsPlayerCommandService,
                                            SpecializationQueryService specializationQueryService,
                                            ShopProfileQueryService shopProfileQueryService,
                                            MenuCommandService menuCommandService,
                                            MenuBuilderFactory menuBuilderFactory,
                                            ButtonBuilderFactory buttonBuilderFactory,
                                            MainMenuFactory mainMenuFactory,
                                            StringUtils stringUtils, TimeUtils timeUtils) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.legendsPlayerCommandService = legendsPlayerCommandService;
        this.specializationQueryService = specializationQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
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

    List<String> createSwitchView(Specialization specialization, boolean isLocked,
                                  boolean canSwitch, String cooldown) {
        List<String> view = new ArrayList<>();
        if (isLocked) {
            view.add(RED + "Locked");
        } else if (canSwitch) {
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
        SpecializationId specializationId = specializationNameToId.get(specializationName);
        if (specializationId == null) {
            System.out.println("WARN failed to createSpecializationButton, specializationId is null for " + specializationName);
            ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BARRIER, "Something went wrong");
            builder.addTooltip("Please contact fishb6nes");
            return builder.build();
        }

        boolean isLocked = !shopProfileQueryService.getProfile(player.getId()).hasSpecialization(specializationId);
        boolean canSwitch = !player.getSpecialization().getName().equals(specializationName);

        ButtonBuilder builder = createSimpleButtonBuilder(icon, specializationName);
        lore.forEach(builder::addTooltip);
        builder.addTooltip("");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        createSwitchView(specialization, isLocked, canSwitch, cooldown).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (!isLocked && cooldown == null && action.isShiftRightClick()) {
                legendsPlayerCommandService.setSpecialization(player.getId(), specializationName);
            }
        });
        return builder.build();
    }

    Button createArmsmasterButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Armsmasters wield a sword and a shield.");
        lore.add("Their skills focus on control and defense.");
        lore.add("");
        lore.add(ColorCode.YELLOW + "[Left click] Melee");
        lore.add("A normal Minecraft melee swing.");
        lore.add(ColorCode.YELLOW + "[Right click] Hamstring");
        lore.add("Damage your enemy and slow them for a");
        lore.add("short duration.");
        lore.add(ColorCode.YELLOW + "[Shift left click] Slam");
        lore.add("An overpowering blow that damages the");
        lore.add("target, knocks them back, and makes");
        lore.add("them nauseous for a moderate duration.");
        lore.add(ColorCode.YELLOW + "[Shift right click] Stand Your Ground");
        lore.add("Bunker down to greatly increase all of your");
        lore.add("resistances for a moderate duration.");
        return createSpecializationButton(player, LegendsSpecialization.ARMSMASTER, ItemType.WOOD_SWORD,
                lore, cooldown);
    }

    Button createBerserkerButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Berserkers wield an axe and the skulls");
        lore.add("of their enemies. Their skills focus on");
        lore.add("damage and recovering health.");
        lore.add("");
        lore.add(ColorCode.YELLOW + "[Left click] Melee");
        lore.add("A normal Minecraft melee swing.");
        lore.add(ColorCode.YELLOW + "[Right click] Maul");
        lore.add("Maul your enemy for damage and receive");
        lore.add("absorption for a short duration.");
        lore.add(ColorCode.YELLOW + "[Shift left click] Grudge");
        lore.add("Mark a player with your grudge for a");
        lore.add("moderate duration. Any attacks made");
        lore.add("against them heal you a little bit.");
        lore.add(ColorCode.YELLOW + "[Shift right click] Shatter");
        lore.add("Slam down into the ground in front of");
        lore.add("you, damaging enemies while throwing");
        lore.add("them into the air and making them");
        lore.add("nauseous for a moderate duration.");
        return createSpecializationButton(player, LegendsSpecialization.BERSERKER, ItemType.WOOD_AXE,
                lore, cooldown);
    }

    Button createHunterButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Hunters use bow and arrow. Their skills");
        lore.add("offer damage, control, and mobility.");
        lore.add("");
        lore.add(ColorCode.YELLOW + "[Left click] Toxic Arrow");
        lore.add("Fire a poisoned arrow damaging your target");
        lore.add("and applying poison for a short duration.");
        lore.add(ColorCode.YELLOW + "[Right click] Shoot");
        lore.add("A normal Minecraft bow shot.");
        lore.add(ColorCode.YELLOW + "[Shift left click] Trap");
        lore.add("Place a trap at your feet which roots");
        lore.add("any enemies that trigger it.");
        lore.add(ColorCode.YELLOW + "[Shift right click] Disengage");
        lore.add("Leap backwards a sizable distance.");
        return createSpecializationButton(player, LegendsSpecialization.HUNTER, ItemType.BOW,
                lore, cooldown);
    }

    Button createMarksmanButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Marksmen use bow and arrow. Their skills");
        lore.add("focus on damage while sacrificing mobility.");
        lore.add("");
        lore.add(ColorCode.YELLOW + "[Left click] Power Shot");
        lore.add("Fire a high velocity shot dealing damage");
        lore.add("and knocking back your target.");
        lore.add(ColorCode.YELLOW + "[Right click] Shoot");
        lore.add("A normal Minecraft bow shot.");
        lore.add(ColorCode.YELLOW + "[Shift left click] Focus");
        lore.add("Focus your vision and greatly increase");
        lore.add("your damage while becoming immobile");
        lore.add("(cast again to cancel).");
        lore.add(ColorCode.YELLOW + "[Shift right click] Sneak");
        lore.add("Gain a speed boost and hide in plain");
        lore.add("sight, becoming invisible to enemies.");
        return createSpecializationButton(player, LegendsSpecialization.MARKSMAN, ItemType.BOW,
                lore, cooldown);
    }

    Button createTempestButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Tempests wield a staff (hoe). Their");
        lore.add("skills offer large area damage.");
        lore.add("");
        lore.add(ColorCode.YELLOW + "[Left click] Fireball");
        lore.add("Fling a ball of fire at your target");
        lore.add("dealing damage and setting them on fire");
        lore.add(ColorCode.YELLOW + "[Right click] Bolt");
        lore.add("Call down lightning at your targeted");
        lore.add("location damaging nearby enemies.");
        lore.add(ColorCode.YELLOW + "[Shift left click] Hail Blast");
        lore.add("Blast an icy torrent in front of you");
        lore.add("damaging and slowing enemies.");
        lore.add(ColorCode.YELLOW + "[Shift right click] Molten Storm");
        lore.add("Conjure a storm of molten rocks at your");
        lore.add("targeted location. Each fireball damages");
        lore.add("and briefly sets enemies on fire.");
        return createSpecializationButton(player, LegendsSpecialization.TEMPEST, ItemType.WOOD_HOE,
                lore, cooldown);
    }

    Button createWizardButton(LegendsPlayer player, String cooldown) {
        List<String> lore = new ArrayList<>();
        lore.add("Tempests wield wand (stick) and book. Their");
        lore.add("skills focus on area control, and mobility.");
        lore.add("");
        lore.add(ColorCode.YELLOW + "[Left click] Fireball");
        lore.add("Fling a ball of fire at your target");
        lore.add("dealing damage and setting them on fire");
        lore.add(ColorCode.YELLOW + "[Right click] Arcane Missile");
        lore.add("Cast an arcane missile damaging nearby");
        lore.add("enemies on impact.");
        lore.add(ColorCode.YELLOW + "[Shift left click] Teleport");
        lore.add("Teleport to your targeted location, releasing");
        lore.add("an arcane explosion around you on arrival");
        lore.add("damaging and slowing nearby enemies.");
        lore.add(ColorCode.YELLOW + "[Shift right click] Mass Piggify");
        lore.add("Conjure chaotic energies at your targeted");
        lore.add("location, which after a short delay force");
        lore.add("your enemies to ride uncontrollable pigs");
        lore.add("making them nauseous.");
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
