package gg.warcraft.legends.app.shop.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.config.DecorationConfiguration;
import gg.warcraft.legends.api.shop.config.DecorationPackConfiguration;
import gg.warcraft.legends.api.shop.config.MiniPetConfiguration;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;
import gg.warcraft.legends.api.shop.service.ShopCommandService;
import gg.warcraft.legends.api.shop.service.ShopRepository;
import gg.warcraft.legends.app.shop.SimpleDecoration;
import gg.warcraft.legends.app.shop.SimpleDecorationPack;
import gg.warcraft.legends.app.shop.SimpleMiniPet;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultShopCommandService implements ShopCommandService {
    private final ShopRepository shopRepository;

    @Inject
    public DefaultShopCommandService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    void readDecorationConfiguration(DecorationConfiguration configuration) {
        Decoration decoration = new SimpleDecoration(
                configuration.getId(),
                configuration.getType(),
                configuration.getRarity(),
                configuration.getName(),
                configuration.getIcon(),
                configuration.getLore(),
                configuration.getItemType());
        shopRepository.save(decoration);
    }

    void readDecorationPackConfiguration(DecorationPackConfiguration configuration) {
        List<Decoration> decorations = configuration.getDecorations().stream()
                .map(shopRepository::getItem)
                .filter(Objects::nonNull)
                .map(Decoration.class::cast)
                .collect(Collectors.toList());
        DecorationPack decorationPack = new SimpleDecorationPack(
                configuration.getId(),
                configuration.getType(),
                configuration.getRarity(),
                configuration.getName(),
                configuration.getIcon(),
                configuration.getLore(),
                decorations);
        shopRepository.save(decorationPack);
    }

    void readMiniPetConfiguration(MiniPetConfiguration configuration) {
        MiniPet miniPet = new SimpleMiniPet(
                configuration.getId(),
                configuration.getType(),
                configuration.getRarity(),
                configuration.getName(),
                configuration.getSkull(),
                configuration.getLore(),
                configuration.getEntityType(),
                configuration.isRainbow());
        shopRepository.save(miniPet);
    }

    @Override
    public void readConfiguration(ShopConfiguration shopConfiguration) {
        shopConfiguration.getDecorations().forEach(this::readDecorationConfiguration);
        shopConfiguration.getDecorationPacks().forEach(this::readDecorationPackConfiguration);
        shopConfiguration.getMiniPets().forEach(this::readMiniPetConfiguration);
    }
}
