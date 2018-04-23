package gg.warcraft.legends.shop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ShopService implements CommandExecutor, Listener {
    private static final ShopService instance = new ShopService();

    public static ShopService getInstance() {
        return instance;
    }

    private static final Set<UUID> grandfatheredPlayerIds;

    static {
        grandfatheredPlayerIds = new HashSet<>();
        grandfatheredPlayerIds.add(UUID.fromString("f31254d7-f6d8-42d9-b658-4f76a4e5c53f")); // Onslaughted
        grandfatheredPlayerIds.add(UUID.fromString("5bf016c2-d7cf-41d1-9642-0e5fd63355fe")); // Chaotikal
    }

    public static boolean isGrandfathered(UUID playerId) {
        return grandfatheredPlayerIds.contains(playerId);
    }

    private final ShopProfileRepository repository;
    private final Map<UUID, ShopProfile> profiles;

    private ShopService() {
        repository = new ShopProfileRepository();
        profiles = new HashMap<>();
    }

    public boolean isSettler(UUID playerId) {
        // TODO grab profile from repo if not already in profiles, should not happen unless profile queried while player offline
        return profiles.get(playerId).hasPromotionalUnlocked("settler");
    }

    public boolean hasPiglet(UUID playerId) {
        return profiles.get(playerId).hasMiniPetUnlocked("piglet");
    }

    public boolean hasCalf(UUID playerId) {
        return profiles.get(playerId).hasMiniPetUnlocked("calf");
    }

    public boolean hasLamb(UUID playerId) {
        return profiles.get(playerId).hasMiniPetUnlocked("lamb");
    }

    public boolean hasChick(UUID playerId) {
        return profiles.get(playerId).hasMiniPetUnlocked("chick");
    }

    public boolean hasWood(UUID playerId) {
        return profiles.get(playerId).hasDecorationUnlocked("wood");
    }

    public boolean hasStone(UUID playerId) {
        return profiles.get(playerId).hasDecorationUnlocked("stone");
    }

    public boolean hasFabrics(UUID playerId) {
        return profiles.get(playerId).hasDecorationUnlocked("fabrics");
    }

    public boolean hasFlowers(UUID playerId) {
        return profiles.get(playerId).hasDecorationUnlocked("flowers");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            System.out.println(sender.getName() + " tried to execute a shop command as a Player");
            return false;
        }
        if (!command.getName().equals("shop")) {
            System.out.println("ShopService was asked to execute a non-shop command");
            return false;
        }
        if (args.length < 2) {
            System.out.println("shop command executed with less than 2 args, i.e. missing at least UUID or type");
            return false;
        }

        UUID playerId;
        try {
            playerId = UUID.fromString(args[0]);
        } catch (Exception ex) {
            System.out.println("ShopService was unable to extract UUID from command");
            return false;
        }

        String[] subArgs = Arrays.copyOfRange(args, 2, args.length);
        switch (args[1]) {
            case "promotional":
                return handlePromotionalCommand(playerId, subArgs);
            case "minipets":
                return handleMiniPetsCommand(playerId, subArgs);
            case "decorations":
                return handleDecorationsCommand(playerId, subArgs);
            default:
                System.out.println("ShopService was unable to process command of type " + args[1]);
                return false;
        }
    }

    private boolean handlePromotionalCommand(UUID playerId, String[] args) {
        if (args.length != 2) {
            System.out.println("handlePromotionalCommand got invalid args: " + Arrays.toString(args));
        }

        switch (args[0]) {
            case "settler":
                ShopProfile profile = profiles.get(playerId);
                if (args[1].equals("1")) {
                    System.out.println("Unlocking settler for player with id " + playerId);
                    profile.unlockPromotion(args[0]);
                } else {
                    profile.lockPromotion(args[0]);
                    System.out.println("Locking settler for player with id " + playerId);
                }
                repository.save(profile);
                return true;
            default:
                System.out.println("handlePromotionalCommand could not handle command with type " + args[0]);
                return false;
        }
    }

    private boolean handleMiniPetsCommand(UUID playerId, String[] args) {
        if (args.length != 2) {
            System.out.println("handleMiniPetsCommand got invalid args: " + Arrays.toString(args));
        }

        switch (args[0]) {
            case "piglet":
            case "calf":
            case "lamb":
            case "chick":
                ShopProfile profile = profiles.get(playerId);
                if (args[1].equals("1")) {
                    System.out.println("Unlocking mini pet " + args[0] + " for player with id " + playerId);
                    profile.unlockMiniPet(args[0]);
                } else {
                    profile.lockMiniPet(args[0]);
                    System.out.println("Locking mini pet " + args[0] + " for player with id " + playerId);
                }
                repository.save(profile);
                return true;
            default:
                System.out.println("handleMiniPetsCommand could not handle command with type " + args[0]);
                return false;
        }
    }

    private boolean handleDecorationsCommand(UUID playerId, String[] args) {
        if (args.length != 2) {
            System.out.println("handleDecorationsCommand got invalid args: " + Arrays.toString(args));
        }

        switch (args[0]) {
            case "wood":
            case "stone":
            case "fabrics":
            case "flowers":
                ShopProfile profile = profiles.get(playerId);
                if (args[1].equals("1")) {
                    System.out.println("Unlocking decoration " + args[0] + " for player with id " + playerId);
                    profile.unlockDecoration(args[0]);
                } else {
                    profile.lockDecoration(args[0]);
                    System.out.println("Locking decoration " + args[0] + " for player with id " + playerId);
                }
                repository.save(profile);
                return true;
            default:
                System.out.println("handleDecorationsCommand could not handle command with type " + args[0]);
                return false;
        }
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID playerId = event.getUniqueId();
        if (!profiles.containsKey(playerId)) {
            profiles.put(playerId, repository.getOrNew(playerId));
        }
    }
}
