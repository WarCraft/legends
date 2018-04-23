package gg.warcraft.legends;

import gg.warcraft.legends.chat.LegendsChatService;
import gg.warcraft.legends.combat.DefaultLegendsCombatService;
import gg.warcraft.legends.combat.Specialization;
import gg.warcraft.legends.config.DefaultLegendsConfigurationService;
import gg.warcraft.legends.data.DataService;
import gg.warcraft.legends.entity.DefaultLegendsEntityService;
import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.legends.gathering.LegendsGatheringModule;
import gg.warcraft.legends.item.EquipListener;
import gg.warcraft.legends.item.Equipment;
import gg.warcraft.legends.item.ItemDropper;
import gg.warcraft.legends.item.ItemService;
import gg.warcraft.legends.menu.MainMenu;
import gg.warcraft.legends.menu.ReturnButton;
import gg.warcraft.legends.menu.shop.DecorationMenu;
import gg.warcraft.legends.menu.shop.MiniPetMenu;
import gg.warcraft.legends.menu.specialization.SpecializationMenu;
import gg.warcraft.legends.metrics.PlayerCountListener;
import gg.warcraft.legends.metrics.TotalPointsListener;
import gg.warcraft.legends.objective.DefaultLegendsObjectiveService;
import gg.warcraft.legends.player.DefaultLegendsPlayerService;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerRepository;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.legends.player.PlayerInspector;
import gg.warcraft.legends.shop.ShopService;
import gg.warcraft.legends.siege.bomb.BombPlaceable;
import gg.warcraft.legends.siege.trebuchet.SimpleTrebuchet;
import gg.warcraft.legends.siege.trebuchet.TrebuchetPlaceable;
import gg.warcraft.legends.world.CommandBlockInteractorListener;
import gg.warcraft.legends.world.CommandBlockPlacerListener;
import gg.warcraft.legends.world.HologramPlacerListener;
import gg.warcraft.legends.world.placeable.FootGroundTargeter;
import gg.warcraft.legends.world.placeable.InFrontFootGroundTargeter;
import gg.warcraft.monolith.BukkitLoggerService;
import gg.warcraft.monolith.DefaultAuthorizationService;
import gg.warcraft.monolith.Dev;
import gg.warcraft.monolith.LoginAnnouncer;
import gg.warcraft.monolith.MonolithPlugin;
import gg.warcraft.monolith.MonolithService;
import gg.warcraft.monolith.TipBroadcaster;
import gg.warcraft.monolith.chat.DefaultChatService;
import gg.warcraft.monolith.combat.DefaultCombatService;
import gg.warcraft.monolith.command.DefaultCommandService;
import gg.warcraft.monolith.entity.DefaultEntityService;
import gg.warcraft.monolith.event.DefaultEventService;
import gg.warcraft.monolith.gathering.DefaultGatheringService;
import gg.warcraft.monolith.gathering.GatheringService;
import gg.warcraft.monolith.housing.BuildRepository;
import gg.warcraft.monolith.housing.DefaultBuildRepository;
import gg.warcraft.monolith.housing.DefaultHousingService;
import gg.warcraft.monolith.item.DefaultItemService;
import gg.warcraft.monolith.javafx.geometry.BoundingBox;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.world.DefaultWorldService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LegendsPlugin extends MonolithPlugin implements Legends {

    private BukkitLoggerService loggerService;
    private DefaultLegendsConfigurationService configService;
    private DefaultAuthorizationService authService;
    private DefaultEntityService entityService;
    private DefaultLegendsPlayerService playerService;
    private DefaultCommandService commandService;
    private DefaultEventService eventService;
    private DefaultChatService chatService;
    private DefaultWorldService worldService;
    private DefaultLegendsObjectiveService objectiveService;
    private DefaultItemService itemService;
    private DefaultCombatService combatService;

    private BuildRepository buildRepository;
    private DefaultHousingService housingService;

    @Override
    public void onLoad() {
        super.onLoad();
        loggerService = new BukkitLoggerService();
        configService = new DefaultLegendsConfigurationService();
    }

    @Override
    public void onEnable() {
        authService = new DefaultAuthorizationService();
        playerService = new DefaultLegendsPlayerService(getServer(), loggerService, new LegendsPlayerRepository());
        entityService = new DefaultLegendsEntityService(playerService);
        commandService = new DefaultCommandService(getServer());
        eventService = new DefaultEventService(getServer(), this, playerService);
        chatService = new LegendsChatService(loggerService, authService, commandService, eventService);

        eventService.registerForEvents(playerService);
        eventService.registerForEvents(new PlayerInspector(playerService));
        // FIXME eventService.registerForEvents(new PlayerDisplayNameTracker(playerService));
        eventService.registerForEvents(chatService);

        super.onEnable();

        worldService = new DefaultWorldService(getWorld());
        worldService.restoreAll();

        // TODO want to store reference? (needs to be after WorldService)
        GatheringService gatheringService = new DefaultGatheringService(eventService, worldService);

        // clear all non player entries as gathering module will respawn them all
        // NOTE in the future the objective module should respawn any guardians
        getWorld().getLivingEntities().stream()
                .filter(entity -> entity.getType() != EntityType.PLAYER)
                .forEach(LivingEntity::remove);

        LegendsGatheringModule gatheringModule = new LegendsGatheringModule(this, gatheringService);

        objectiveService = new DefaultLegendsObjectiveService(loggerService, eventService, configService);

        eventService.registerForEvents(new SpawnListener());
        eventService.registerForEvents(new TutorialListener());

        itemService = new DefaultItemService();
        ItemService.getInstance(); // instantiate event listeners

        combatService = new DefaultLegendsCombatService(eventService, playerService);
        eventService.registerForEvents(combatService);

        // TODO move
        eventService.registerForEvents(new EquipListener());

        buildRepository = new DefaultBuildRepository(getWorld(), new BoundingBox(5, 4, -6305, 300, 100, 300));
        housingService = new DefaultHousingService(eventService, playerService, buildRepository, getWorld());

        // prevent ops from breaking blocks when not in creative
        // TODO eventService.registerForEvents(new OpBlockBreakListener());

        PlayerCountListener playerCountListener = new PlayerCountListener();
        eventService.registerForEvents(playerCountListener);

        TotalPointsListener totalPointsListener = new TotalPointsListener();
        eventService.registerForEvents(totalPointsListener);

        // Initialize data service
        DataService.getInstance();

        eventService.registerForEvents(new Barrierifier());
        eventService.registerForEvents(new BarrierifierNullifier());

        eventService.registerForEvents(ShopService.getInstance());
        getServer().getPluginCommand("shop").setExecutor(ShopService.getInstance());

        getServer().getPluginCommand("givegold").setExecutor(new GiveGoldCommandExecutor());

        getServer().getPluginCommand("dropitems").setExecutor(new ItemDropper());

        getServer().getPluginCommand("kit").setExecutor(new KitDistributor());

        eventService.registerForEvents(new WatchYoProfamity());

        eventService.registerForEvents(new VoteListener(playerService));

        eventService.registerForEvents(new LoginAnnouncer());

        new TipBroadcaster(getTips());

        new GuidingHolograms(getWorld(), getServer().getWorld("Ouros_the_end"));

        eventService.registerForEvents(new HologramPlacerListener());
        eventService.registerForEvents(new CommandBlockPlacerListener());
        eventService.registerForEvents(new CommandBlockInteractorListener());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        LegendsPlayer player = LegendsPlayerService.getInstance().getLegendsPlayer(((Player) sender).getUniqueId());

        switch (label) {
            case "me":
            case "menu":
            case "monolith":
            case "help":
                RunnableUtils.runOnNextTick(() -> player.openInventory(new MainMenu(player)));
                return true;

            case "race":
                RacePicker racePicker = new RacePicker(player);
                if (args.length > 0) {
                    switch (args[0].toUpperCase()) {
                        case "HUMAN":
                            racePicker.setRace(LegendsRace.HUMAN);
                            return true;
                        case "ORC":
                            racePicker.setRace(LegendsRace.ORC);
                            return true;
                    }
                }
                RunnableUtils.runOnNextTick(() -> player.openInventory(racePicker));
                return true;

            case "class":
            case "spec":
            case "specialization":
            case "kits":
                SpecializationMenu specializationMenu = new SpecializationMenu(player);
                if (args.length > 0) {
                    switch (args[0].toUpperCase()) {
                        case "ARMSMASTER":
                            specializationMenu.setSpecialization(Specialization.ARMSMASTER);
                            return true;
                        case "BERSERKER":
                            specializationMenu.setSpecialization(Specialization.BERSERKER);
                            return true;
                        case "TEMPEST":
                            specializationMenu.setSpecialization(Specialization.TEMPEST);
                            return true;
                        case "WIZARD":
                            specializationMenu.setSpecialization(Specialization.WIZARD);
                            return true;
                        case "HUNTER":
                            specializationMenu.setSpecialization(Specialization.HUNTER);
                            return true;
                        case "MARKSMAN":
                            specializationMenu.setSpecialization(Specialization.MARKSMAN);
                            return true;
                    }
                }
                RunnableUtils.runOnNextTick(() -> player.openInventory(specializationMenu));
                return true;

            case "return":
            case "spawn":
                ReturnButton returnButton = new ReturnButton(player);
                returnButton.teleport();
                return true;

        }

        if (label.equals("treb")) {
            new FootGroundTargeter(player, new TrebuchetPlaceable());
            return true;
        }

        if (label.equals("bomb")) {
            new InFrontFootGroundTargeter(player, new BombPlaceable());
            return true;
        }

        if (label.equals("gold")) {
            player.sendMessage(ChatColor.YELLOW + "You have " + player.getInGameCurrency() + " gold.");
            return true;
        }

        if (label.equals("vote")) {
            player.sendMessage(ChatColor.YELLOW + "Support the server by voting for us at:");
            player.sendMessage(ChatColor.YELLOW + "http://minecraft-server-list.com/server/423343/vote");
            player.sendMessage(ChatColor.YELLOW + "http://minecraftservers.org/vote/257786");
            player.sendMessage(ChatColor.YELLOW + "https://minecraft-server.net/vote/warcraftgg");
            player.sendMessage(ChatColor.YELLOW + "http://minecraft-mp.com/server/192075/vote/");
            player.sendMessage(ChatColor.YELLOW + "https://minecraftservers.biz/servers/106701/vote/");
            player.sendMessage(ChatColor.YELLOW + "Every vote earns you a little bit of gold!");
        }

        if (label.equals("fvel")) {
            if (!player.getName().equals("fishb6nes")) {
                return false;
            }

            try {
                SimpleTrebuchet.velocityModifier = Double.parseDouble(args[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (label.equals("fvar")) {
            if (!player.getName().equals("fishb6nes")) {
                return false;
            }

            try {
                SimpleTrebuchet.maxVariance = Double.parseDouble(args[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (label.equals("restoreall")) {
            if (sender.getName().equals("fishb6nes")) {
                worldService.restoreAll();
                return true;
            }
            return false;
        }

        if (label.equals("pet")) {
            RunnableUtils.runOnNextTick(() -> player.openInventory(new MiniPetMenu(player)));
            return true;
        }

        if (label.equals("dec")) {
            RunnableUtils.runOnNextTick(() -> player.openInventory(new DecorationMenu(player)));
            return true;
        }

        if (label.equals("skirmish")) {
            if (!sender.getName().equals("fishb6nes")) {
                sender.sendMessage(ChatColor.YELLOW + "Whoops you can not execute this command!");
                return false;
            }

            try {
                objectiveService.startSkirmish(args);
            } catch (Exception ex) {
                System.out.println("FAILED TO START SKIRMISH");
                ex.printStackTrace();
            }
        }

        if (label.equals("build")) {
            if (player.getUniqueId().equals(UUID.fromString("a4ab3821-ee3e-4199-b555-405e70a6cbf8")) || // fishb6nes
                    player.getUniqueId().equals(UUID.fromString("926fe756-2622-4e18-94f9-c4b0fc22ebb9")) || // eijlen
                    player.getUniqueId().equals(UUID.fromString("e3e6e31c-6964-490c-bdb5-b41067c459b0")) || // Dragonary
                    player.getUniqueId().equals(UUID.fromString("f31254d7-f6d8-42d9-b658-4f76a4e5c53f")) || // Onslaughted
                    player.getUniqueId().equals(UUID.fromString("43dcc77c-be9a-46de-bbca-1f20f1130a3a"))) { // RodentConsumer
                if (args.length == 0) {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /build on/off");
                } else if (args[0].equals("on")) {
                    getServer().dispatchCommand(getServer().getConsoleSender(), "rg addmember __global__ " + player.getName() + " -w Ouros");
                    getServer().dispatchCommand(getServer().getConsoleSender(), "rg addmember __global__ " + player.getName() + " -w Ouros_the_end");
                    player.sendMessage(ChatColor.YELLOW + "You are now in builder mode, be careful with punching shit in GM 1!");
                } else if (args[0].equals("off")) {
                    getServer().dispatchCommand(getServer().getConsoleSender(), "rg removemember __global__ " + player.getName() + " -w Ouros");
                    getServer().dispatchCommand(getServer().getConsoleSender(), "rg removemember __global__ " + player.getName() + " -w Ouros_the_end");
                    player.sendMessage(ChatColor.YELLOW + "Your builder mode has been disabled.");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /build on/off");
                }
                return true;
            }
            return false;
        }

        if (label.equals("fish")) {
            if (!Dev.isDev(sender)) {
                return false;
            }

            try {
                switch (args[0]) {
                    case "disablePigSpawn":
                        DefaultGatheringService.DO_NOT_USE_preventPigRespawn = true;
                        break;
                    case "enablePigSpawn":
                        DefaultGatheringService.DO_NOT_USE_preventPigRespawn = false;
                        break;
                }
            } catch (Exception ex) {
                sender.sendMessage("Invalid command");
            }
            return true;
        }

        if (label.equals("mspawn")) {
            if (!sender.getName().equals("fishb6nes") && !sender.getName().equals("eijlen")) {
                sender.sendMessage(ChatColor.YELLOW + "Whoops you can not execute this command!");
                return false;
            }

            try {
                String subcommand = args[0];

                // equipment subcommand vars
                boolean equipCheat = false;
                ItemStack equipItem = null;

                switch (subcommand) {
                    case "legendary":
                        equipCheat = true;
                    case "equipment":
                        if (args.length > 1) {
                            String equipType = args[1];
                            Equipment equip;
                            try {
                                equip = Equipment.valueOf(equipType);
                            } catch (Exception ex) {
                                sender.sendMessage(ChatColor.YELLOW + "Valid equipment types are: " + Arrays.toString(Equipment.values()));
                                return false;
                            }
                            equipItem = equip.generateRandom(player, player.getSpecialization().getPrimaryAttribute(), equipCheat);

                            if (equipItem.hasItemMeta()) {
                                ItemMeta meta = equipItem.getItemMeta();

                                String nameArg = getNameArg(args);
                                if (nameArg != null && !nameArg.isEmpty()) {
                                    String colors = ChatColor.getLastColors(meta.getDisplayName());
                                    meta.setDisplayName(colors + nameArg);
                                }

                                String loreArg = getLoreArg(args);
                                if (loreArg != null && !loreArg.isEmpty()) {
                                    List<String> lore = meta.getLore();
                                    lore.add("");
                                    lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + loreArg);
                                    meta.setLore(lore);
                                }

                                equipItem.setItemMeta(meta);
                            }
                        } else {
                            equipItem = ItemService.getInstance().generateEquipment(player, equipCheat);
                        }
                        player.giveItem(equipItem, false);
                        break;
                }
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.YELLOW + "Nooo that's not how you use this command, ask fish.");
                return false;
            }
            return true;
        }

        return false;
    }

    private String getLongArg(String[] args, String arg) {
        Integer startIndex = null;
        Integer endIndex = null;
        for (int index = 0; index < args.length - 1; index += 1) {
            if (args[index].equals(arg)) {
                startIndex = index + 1;
                endIndex = startIndex;
                break;
            }
        }

        if (startIndex == null) {
            return "";
        }

        for (int index = startIndex; index < args.length; index += 1) {
            if (args[index].startsWith("--")) {
                endIndex = index;
                break;
            } else if (index == (args.length - 1)) {
                endIndex = args.length;
                break;
            }
        }
        String[] longArg = Arrays.copyOfRange(args, startIndex, endIndex);
        return String.join(" ", longArg);
    }

    private String getNameArg(String[] args) {
        return getLongArg(args, "--name");
    }

    private String getLoreArg(String[] args) {
        return getLongArg(args, "--lore");
    }

    @Override
    public MonolithService getService(Service service) {
        switch (service) {
            case LOGGER:
                return loggerService;
            case CONFIGURATION:
                return configService;
            case AUTHORIZATION:
                return authService;
            case CHAT:
                return chatService;
            case COMMAND:
                return commandService;
            case EVENT:
                return eventService;
            case ENTITY:
                return entityService;
            case PLAYER:
                return playerService;
            case WORLD:
                return worldService;
            case OBJECTIVE:
                return objectiveService;
            case ITEM:
                return itemService;
            case COMBAT:
                return combatService;
            default:
                return null;
        }
    }
}
