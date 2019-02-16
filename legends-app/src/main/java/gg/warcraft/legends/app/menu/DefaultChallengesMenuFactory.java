package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.challenge.Challenge;
import gg.warcraft.legends.api.core.challenge.service.ChallengeRepository;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.ChallengesMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
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

import java.util.UUID;

public class DefaultChallengesMenuFactory extends AbstractMenuFactory implements ChallengesMenuFactory {
    private final ChallengeRepository challengeRepository;
    private final LegendsPlayerQueryService playerQueryService;
    private final MainMenuFactory mainMenuFactory;

    @Inject
    public DefaultChallengesMenuFactory(ChallengeRepository challengeRepository,
                                        LegendsPlayerQueryService playerQueryService,
                                        MenuCommandService menuCommandService,
                                        MenuBuilderFactory menuBuilderFactory,
                                        ButtonBuilderFactory buttonBuilderFactory,
                                        MainMenuFactory mainMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.challengeRepository = challengeRepository;
        this.playerQueryService = playerQueryService;
        this.mainMenuFactory = mainMenuFactory;
    }

    Button createInfoButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Your Challenges");
        builder.addTooltip("Challenges are a simple way of adding a");
        builder.addTooltip("little bit of extra action to your day to");
        builder.addTooltip("day activities on the server. Can you");
        builder.addTooltip("complete them all?");
        return builder.build();
    }

    Button createDailyChallengesButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.GUNPOWDER, "Daily Challenges");
        builder.addTooltip("Daily challenges are small tasks that");
        builder.addTooltip("reset at the start of every day.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your daily challenges");

        builder.withAction(action -> {
            Menu menu = createDailyChallengesMenu(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createWarChallengesButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.TNT, "War Challenges");
        builder.addTooltip("War challenges are intermediate challenges");
        builder.addTooltip("that require a little more investment.");
        builder.addTooltip("They reset at the start of a war and unlock");
        builder.addTooltip("every week over the duration of the war");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your war challenges");

        builder.withAction(action -> {
            Menu menu = createWarChallengesMenu(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createAlltimeChallengesButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.MINECART_WITH_TNT, "All-time Challenges");
        builder.addTooltip("All-time challenges are the passive");
        builder.addTooltip("challenges of Ouros. Simply playing on");
        builder.addTooltip("the server will progress them. Some");
        builder.addTooltip("challenges however are a little more nifty.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your all-time challenges");

        builder.withAction(action -> {
            Menu menu = createAlltimeChallengesMenu(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createSpecialChallengesButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.MINECART_WITH_TNT, "Special Challenges");
        builder.addTooltip("Special challenges are challenges");
        builder.addTooltip("that require a little bit more thought.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your special challenges");

        builder.withAction(action -> {
            Menu menu = createSpecialChallengesMenu(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createChallengesMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("Challenges", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton(playerId));
        builder.addButton(2, createDailyChallengesButton(playerId));
        builder.addButton(3, createWarChallengesButton(playerId));
        builder.addButton(4, createAlltimeChallengesButton(playerId));
        builder.addButton(5, createSpecialChallengesButton(playerId));
        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));
        return builder.build();
    }

    Button createChallengeButton(LegendsPlayer player, Challenge challenge) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.TNT, challenge.getName());
        int requiredAmount = challenge.getRequiredStatisticValue();
        int currentAmount = Math.min(requiredAmount, player.getDataInt(challenge.getPlayerData().getKey()));
        ColorCode color = currentAmount >= requiredAmount ? ColorCode.GREEN : ColorCode.WHITE;
        builder.addTooltip(color.toString() + currentAmount + "/" + requiredAmount + " " +
                challenge.getStatistic().getName());
        if (currentAmount >= requiredAmount) {
            builder.addTooltip("");
            builder.addTooltip(ColorCode.GREEN + "Completed");
        }
        return builder.build();
    }

    Button createDailyChallengesInfoButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Daily Challenges");
        builder.addTooltip("Daily challenges are small tasks that");
        builder.addTooltip("reset at the start of every day.");
        return builder.build();
    }

    @Override
    public Menu createDailyChallengesMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = createMenuBuilder("Daily Challenges", MenuSize.ONE_ROW);
        builder.addButton(0, createDailyChallengesInfoButton(playerId));

        int slot = 2;
        for (Challenge daily : challengeRepository.getDailies()) {
            builder.addButton(slot, createChallengeButton(player, daily));
            slot += 1;
        }

        builder.addButton(8, createBackButton(playerId, this::createChallengesMenu));
        return builder.build();
    }

    Button createWarChallengesInfoButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "War Challenges");
        builder.addTooltip("War challenges are intermediate challenges");
        builder.addTooltip("that require a little more investment.");
        builder.addTooltip("They reset at the start of a war and unlock");
        builder.addTooltip("every week over the duration of the war");
        return builder.build();
    }

    @Override
    public Menu createWarChallengesMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = createMenuBuilder("War Challenges", MenuSize.TWO_ROWS);
        builder.addButton(0, createWarChallengesInfoButton(playerId));

        int slot = 2;
        for (Challenge daily : challengeRepository.getWarries()) {
            builder.addButton(slot, createChallengeButton(player, daily));
            slot += 1;
            if (slot == 7) {
                slot = 11;
            }
        }

        builder.addButton(8, createBackButton(playerId, this::createChallengesMenu));
        return builder.build();
    }

    Button createAlltimeChallengesInfoButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "All-time Challenges");
        builder.addTooltip("All-time challenges are the passive");
        builder.addTooltip("challenges of Ouros. Simply playing on");
        builder.addTooltip("the server will progress them, some");
        builder.addTooltip("challenges however are a little more nifty.");
        builder.addTooltip("");
        builder.addTooltip(ColorCode.GOLD + "! This menu is a work in progress, however,");
        builder.addTooltip(ColorCode.GOLD + "! rest assured that your all-time challenges");
        builder.addTooltip(ColorCode.GOLD + "! are still completing, you just can't check");
        builder.addTooltip(ColorCode.GOLD + "! the progress yet.");
        return builder.build();
    }

    @Override
    public Menu createAlltimeChallengesMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("All-time Challenges", MenuSize.ONE_ROW);
        builder.addButton(0, createAlltimeChallengesInfoButton(playerId));

        builder.addButton(8, createBackButton(playerId, this::createChallengesMenu));
        return builder.build();
    }

    Button createSpecialChallengesInfoButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Special Challenges");
        builder.addTooltip("Special challenges are challenges");
        builder.addTooltip("that require a little bit more thought.");
        return builder.build();
    }

    @Override
    public Menu createSpecialChallengesMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = createMenuBuilder("Special Challenges", MenuSize.ONE_ROW);
        builder.addButton(0, createWarChallengesInfoButton(playerId));

        int slot = 2;
        for (Challenge special : Challenge.getSpecials()) {
            builder.addButton(slot, createChallengeButton(player, special));
            slot += 1;
            if (slot == 7) {
                slot = 11;
            }
        }

        builder.addButton(8, createBackButton(playerId, this::createChallengesMenu));
        return builder.build();
    }
}
