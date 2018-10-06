package gg.warcraft.legends.api.item.crate.service;

import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.item.crate.LootCrateType;

import java.util.UUID;

public interface LootCrateCommandService {

    void readConfiguration(ServerConfiguration configuration);

    void openCrate(UUID playerId, LootCrateType type);
}
