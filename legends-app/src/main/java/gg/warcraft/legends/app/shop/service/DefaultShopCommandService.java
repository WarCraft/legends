package gg.warcraft.legends.app.shop.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.config.DecorationConfiguration;
import gg.warcraft.legends.api.shop.config.DecorationPackConfiguration;
import gg.warcraft.legends.api.shop.config.MiniPetConfiguration;
import gg.warcraft.legends.api.shop.config.PromotionConfiguration;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.api.shop.service.ShopRepository;
import gg.warcraft.legends.app.shop.SimpleDecoration;
import gg.warcraft.legends.app.shop.SimpleDecorationPack;
import gg.warcraft.legends.app.shop.SimpleMiniPet;
import gg.warcraft.legends.app.shop.SimplePromotion;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultShopCommandService implements ShopCommandService {
    private final ShopRepository shopRepository;

    @Inject
    public DefaultShopCommandService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    void readDecorationConfiguration(DecorationConfiguration configuration,
                                     Map<ShopItemType, Map<ItemRarity, Integer>> prices) {
        ShopItemType type = configuration.getType();
        ItemRarity rarity = configuration.getRarity();
        int price = prices.get(type).get(rarity);
        Decoration decoration = new SimpleDecoration(
                configuration.getId(),
                type,
                rarity,
                price,
                configuration.getName(),
                configuration.getIcon(),
                configuration.getLore(),
                configuration.getItemType());
        shopRepository.save(decoration);
    }

    void readDecorationPackConfiguration(DecorationPackConfiguration configuration,
                                         Map<ShopItemType, Map<ItemRarity, Integer>> prices) {
        ShopItemType type = configuration.getType();
        ItemRarity rarity = configuration.getRarity();
        int price = prices.get(type).get(rarity);
        List<Decoration> decorations = configuration.getDecorations().stream()
                .map(shopRepository::getItem)
                .filter(Objects::nonNull)
                .map(Decoration.class::cast)
                .collect(Collectors.toList());
        DecorationPack decorationPack = new SimpleDecorationPack(
                configuration.getId(),
                type,
                rarity,
                price,
                configuration.getName(),
                configuration.getIcon(),
                configuration.getLore(),
                decorations);
        shopRepository.save(decorationPack);
    }

    void readMiniPetConfiguration(MiniPetConfiguration configuration,
                                  Map<ShopItemType, Map<ItemRarity, Integer>> prices) {
        ShopItemType type = configuration.getType();
        ItemRarity rarity = configuration.getRarity();
        int price = prices.get(type).get(rarity);
        MiniPet miniPet = new SimpleMiniPet(
                configuration.getId(),
                type,
                rarity,
                price,
                configuration.getName(),
                configuration.getSkull(),
                configuration.getLore(),
                configuration.getEntityType(),
                configuration.isRainbow());
        shopRepository.save(miniPet);
    }

    void readPromotionConfiguration(PromotionConfiguration configuration,
                                    Map<ShopItemType, Map<ItemRarity, Integer>> prices) {
        ShopItemType type = configuration.getType();
        ItemRarity rarity = configuration.getRarity();
        int price = prices.get(type).get(rarity);
        Promotion promotion = new SimplePromotion(
                configuration.getId(),
                type,
                rarity,
                price,
                configuration.getName(),
                configuration.getIcon(),
                configuration.getLore(),
                configuration.getItemType(),
                configuration.getEquipmentSlot(),
                configuration.getAttributes());
        shopRepository.save(promotion);
    }

    @Override
    public void readConfiguration(ShopConfiguration shopConfiguration) {
        Map<ShopItemType, Map<ItemRarity, Integer>> prices = shopConfiguration.getPrices();
        shopConfiguration.getDecorations().forEach(config -> readDecorationConfiguration(config, prices));
        shopConfiguration.getDecorationPacks().forEach(config -> readDecorationPackConfiguration(config, prices));
        shopConfiguration.getMiniPets().forEach(config -> readMiniPetConfiguration(config, prices));
        shopConfiguration.getPromotions().forEach(config -> readPromotionConfiguration(config, prices));
    }
}
