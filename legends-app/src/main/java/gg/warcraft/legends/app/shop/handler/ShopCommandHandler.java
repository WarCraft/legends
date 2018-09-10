package gg.warcraft.legends.app.shop.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class ShopCommandHandler implements CommandHandler {
    private final ShopProfileCommandService shopProfileCommandService;

    @Inject
    public ShopCommandHandler(ShopProfileCommandService shopProfileCommandService) {
        this.shopProfileCommandService = shopProfileCommandService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (sender.isPlayer()) {
            return false;
        }

        if (arguments.size() != 4) {
            return false;
        }

        // FIXME
        switch (arguments.get(0)) {
            case "UNLOCK":
                switch (arguments.get(1)) {
                    case "MINI_PET":
                        MiniPetId id = MiniPetId.valueOf(arguments.get(2));
                        shopProfileCommandService.unlockMiniPet(id, UUID.fromString(arguments.get(3)), false);
                }
                break;
            case "LOCK":
                // TODO
                break;
        }
        return true;
    }
}
