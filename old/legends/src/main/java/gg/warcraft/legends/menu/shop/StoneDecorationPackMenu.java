package gg.warcraft.legends.menu.shop;

import com.google.common.collect.ImmutableList;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.shop.Decoration;
import gg.warcraft.legends.shop.ShopService;

public class StoneDecorationPackMenu extends DecorationPackMenu {
    private static final ImmutableList<Decoration> DECORATIONS = Decoration.Pack.STONE.getDecorations();

    public StoneDecorationPackMenu(LegendsPlayer player) {
        super(player, "Stone Decorations", DECORATIONS, () -> ShopService.getInstance().hasStone(player.getUniqueId()));
    }
}
