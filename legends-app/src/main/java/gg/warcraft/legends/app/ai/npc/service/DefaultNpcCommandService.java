package gg.warcraft.legends.app.ai.npc.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.NpcFactory;
import gg.warcraft.legends.api.ai.npc.Npc;
import gg.warcraft.legends.api.ai.npc.NpcType;
import gg.warcraft.legends.api.ai.npc.config.NpcConfiguration;
import gg.warcraft.legends.api.ai.npc.menu.BlueprintVendorDialogFactory;
import gg.warcraft.legends.api.ai.npc.menu.ClassTrainerDialogFactory;
import gg.warcraft.legends.api.ai.npc.service.NpcCommandService;
import gg.warcraft.legends.api.ai.npc.service.NpcRepository;
import gg.warcraft.legends.app.ai.npc.SimpleNpc;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.world.location.Location;

import java.util.UUID;
import java.util.logging.Logger;

public class DefaultNpcCommandService implements NpcCommandService {
    private final NpcRepository npcRepository;
    private final EntityCommandService entityCommandService;
    private final MenuCommandService menuCommandService;
    private final NpcFactory npcFactory;
    private final BlueprintVendorDialogFactory blueprintVendorDialogFactory;
    private final ClassTrainerDialogFactory classTrainerDialogFactory;
    private final Logger pluginLogger;

    @Inject
    public DefaultNpcCommandService(NpcRepository npcRepository,
                                    EntityCommandService entityCommandService,
                                    MenuCommandService menuCommandService,
                                    NpcFactory npcFactory,
                                    BlueprintVendorDialogFactory blueprintVendorDialogFactory,
                                    ClassTrainerDialogFactory classTrainerDialogFactory,
                                    @PluginLogger Logger pluginLogger) {
        this.npcRepository = npcRepository;
        this.entityCommandService = entityCommandService;
        this.menuCommandService = menuCommandService;
        this.npcFactory = npcFactory;
        this.blueprintVendorDialogFactory = blueprintVendorDialogFactory;
        this.classTrainerDialogFactory = classTrainerDialogFactory;
        this.pluginLogger = pluginLogger;
    }

    @Override
    public void readConfiguration(NpcConfiguration configuration) {
        String name = configuration.getName();
        NpcType type = configuration.getType();
        EntityType entityType = configuration.getEntityType();
        Location location = Location.apply(configuration.getLocation());

        UUID npcId = entityCommandService.spawnEntity(entityType, location);
        npcFactory.create(npcId, name);

        Npc npc = new SimpleNpc(npcId, type, name);
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
                Menu blueprintMenu = blueprintVendorDialogFactory.createBlueprintVendorDialog(playerId);
                menuCommandService.showMenu(blueprintMenu, playerId);
                break;
            case CLASS_TRAINER:
                Menu classTrainerMenu = classTrainerDialogFactory.createClassTrainerDialog(playerId, npc.getName());
                menuCommandService.showMenu(classTrainerMenu, playerId);
                break;
        }
    }
}
