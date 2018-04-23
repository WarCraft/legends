package gg.warcraft.legends.menu.shop;

import com.google.common.collect.ImmutableList;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.shop.Decoration;

public class BasicDecorationPackMenu extends DecorationPackMenu {
    private static final ImmutableList<Decoration> DECORATIONS = Decoration.Pack.BASIC.getDecorations();

    public BasicDecorationPackMenu(LegendsPlayer player) {
        super(player, "Basic Decorations", DECORATIONS, () -> true);
    }
}
