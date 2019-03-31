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
            builder.addTooltip(BLUE + "for 15000 favor.");

            builder.withAction(click -> {
                if (click.isShiftRightClick()) {
                    LegendsPlayer player = playerQueryService.getPlayer(playerId);
                    if (player.getFavor() < 15000) {
                        playerCommandService.sendNotification(playerId, "You need at least 15000 favor to unlock this class.");
                        closeMenu(playerId);
                        return;
                    }

                    playerCommandService.removeCurrency(playerId, LegendsCurrency.FAVOR, 15000);
                    shopProfileCommandService.unlockSpecialization(specializationId, playerId, false);
                    closeMenu(playerId);
                    playerCommandService.sendNotification(playerId, "You have unlocked the " + specializationName + "!");
                }
            });
        } else {
            builder.addTooltip("You already have this class unlocked.");
        }
        return builder.build();
    }

    private Button createArmsmasterButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.ARMSMASTER.name());
        final List<String> lore = new ArrayList<>();
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
        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createBerserkerButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.BERSERKER.name());
        final List<String> lore = new ArrayList<>();
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
        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createHunterButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.HUNTER.name());
        final List<String> lore = new ArrayList<>();
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
        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createMarksmanButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.MARKSMAN.name());
        final List<String> lore = new ArrayList<>();
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
        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createTempestButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.TEMPEST.name());
        final List<String> lore = new ArrayList<>();
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
        return createSpecializationButton(playerId, specialization, lore);
    }

    private Button createWizardButton(UUID playerId) {
        Specialization specialization = shopQueryService.getSpecialization(SpecializationId.WIZARD.name());
        final List<String> lore = new ArrayList<>();
        lore.add("Wizards wield wand (stick) and book. Their");
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
        lore.add(ColorCode.YELLOW + "[Shift right click] Mass Bamboozle");
        lore.add("Conjure chaotic energies at your targeted");
        lore.add("location, which after a short delay turns");
        lore.add("your enemies to into uncontrollable animals");
        lore.add("while making them nauseous.");
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
            case "Ranger Trainer":
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
