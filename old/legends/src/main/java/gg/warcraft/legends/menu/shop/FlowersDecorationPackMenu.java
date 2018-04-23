package gg.warcraft.legends.menu.shop;

import com.google.common.collect.ImmutableList;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.shop.Decoration;
import gg.warcraft.legends.shop.ShopService;

public class FlowersDecorationPackMenu extends DecorationPackMenu {
    private static final ImmutableList<Decoration> DECORATIONS = Decoration.Pack.FLOWERS.getDecorations();

    public FlowersDecorationPackMenu(LegendsPlayer player) {
        super(player, "Flower Decorations", DECORATIONS, () -> ShopService.getInstance().hasFlowers(player.getUniqueId()));
    }
}
