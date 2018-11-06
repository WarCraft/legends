package gg.warcraft.legends.app.ai.npc.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.NpcFactory;
import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.NpcType;
import gg.warcraft.legends.api.ai.npc.config.NpcConfiguration;
import gg.warcraft.legends.api.ai.npc.service.NpcCommandService;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;
import gg.warcraft.legends.app.ai.npc.SimpleNpc;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.world.location.Location;
import gg.warcraft.monolith.api.world.location.LocationFactory;

import java.util.UUID;
import java.util.logging.Logger;

public class DefaultNpcCommandService implements NpcCommandService {
    private final NpcRepository npcRepository;
    private final EntityCommandService entityCommandService;
    private final MenuCommandService menuCommandService;
    private final LocationFactory locationFactory;
    private final NpcFactory npcFactory;
    private final Logger pluginLogger;

    @Inject
    public DefaultNpcCommandService(NpcRepository npcRepository,
                                    EntityCommandService entityCommandService,
                                    MenuCommandService menuCommandService,
                                    LocationFactory locationFactory, NpcFactory npcFactory,
                                    @PluginLogger Logger pluginLogger) {
        this.npcRepository = npcRepository;
        this.entityCommandService = entityCommandService;
        this.menuCommandService = menuCommandService;
        this.locationFactory = locationFactory;
        this.npcFactory = npcFactory;
        this.pluginLogger = pluginLogger;
    }

    @Override
    public void readConfiguration(NpcConfiguration configuration) {
        String name = configuration.getName();
        NpcType type = configuration.getType();
        EntityType entityType = configuration.getEntityType();
        Location location = locationFactory.createLocation(configuration.getLocation());

        UUID npcId = entityCommandService.spawnEntity(entityType, location);
        npcFactory.create(npcId, name);

        Npc npc = new SimpleNpc(npcId, type);
        npcRepository.save(npc);
    }

    @Override
    public void openDialog(UUID npcId, UUID playerId) {
        Npc npc = npcRepository.getNpc(npcId);
        if (npc == null) {
            pluginLogger.warning("Failed to open dialog for npc " + npcId + " to player " + playerId +
                    ", npc doesn't exist.");
            return;
        }

        switch (npc.getType()) {
            case BLUEPRINT_VENDOR:
                Menu blueprintMenu = null; // TODO
                menuCommandService.showMenu(blueprintMenu, playerId);
                break;
            default:
                pluginLogger.info("Asked to open dialog for npc " + npcId + " to player " + playerId +
                        ", but npc type " + npc.getType() + " doesn't have a dialog yet.");
                break;
        }
    }
}
