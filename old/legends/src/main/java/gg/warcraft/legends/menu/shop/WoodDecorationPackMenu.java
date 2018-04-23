package gg.warcraft.legends.menu.shop;

import com.google.common.collect.ImmutableList;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.shop.Decoration;
import gg.warcraft.legends.shop.ShopService;

public class WoodDecorationPackMenu extends DecorationPackMenu {
    private static final ImmutableList<Decoration> DECORATIONS = Decoration.Pack.WOOD.getDecorations();

    public WoodDecorationPackMenu(LegendsPlayer player) {
        super(player, "Wood Decorations", DECORATIONS, () -> ShopService.getInstance().hasWood(player.getUniqueId()));
    }
}
