package gg.warcraft.legends.app.shop.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
import gg.warcraft.legends.api.shop.service.ShopRepository;

public class DefaultShopQueryService implements ShopQueryService {
    private final ShopRepository shopRepository;

    @Inject
    public DefaultShopQueryService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public ShopItem getItem(String id) {
        return shopRepository.getItem(id);
    }

    @Override
    public Decoration getDecoration(String id) {
        ShopItem item = getItem(id);
        if (item instanceof Decoration) {
            return (Decoration) item;
        }
        return null;
    }

    @Override
    public DecorationPack getDecorationPack(String id) {
        ShopItem item = getItem(id);
        if (item instanceof DecorationPack) {
            return (DecorationPack) item;
        }
        return null;
    }

    @Override
    public MiniPet getMiniPet(String id) {
        ShopItem item = getItem(id);
        if (item instanceof MiniPet) {
            return (MiniPet) item;
        }
        return null;
    }

    @Override
    public Promotion getPromotion(String id) {
        ShopItem item = getItem(id);
        if (item instanceof Promotion) {
            return (Promotion) item;
        }
        return null;
    }
}
