package gg.warcraft.legends;

import gg.warcraft.legends.menu.specialization.SpecializationMenu;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class TutorialListener implements Listener {
    private static final String METADATA_KEY = TutorialListener.class.getCanonicalName();

    static {
        LegendsPlayerService playerService = LegendsPlayerService.getInstance();
        EventService.getInstance().registerForEvents(new Listener() {
            @EventHandler
            public void onInventoryCloseEvent(InventoryCloseEvent event) {
                if (event.getView() instanceof RacePicker ||
                        event.getView() instanceof SpecializationMenu) {
                    List<MetadataValue> metadata = event.getPlayer().getMetadata(METADATA_KEY);
                    if (!metadata.isEmpty()) {
                        LegendsPlayer player = playerService.getLegendsPlayer(event.getPlayer().getUniqueId());
                        if (player.getRace() == null) {
                            RunnableUtils.runLater(() -> player.openInventory(new RacePicker(player)), 2);
                        } else if (player.getSpecialization() == null) {
                            RunnableUtils.runLater(() -> player.openInventory(new SpecializationMenu(player)), 2);
                        } else {
                            // NOTE disabled for now: player.freeze(0);
                            player.removeMetadata(METADATA_KEY, Monolith.getInstance());
                        }
                    }
                }
            }
        });
    }

    private final LegendsPlayerService playerService;

    public TutorialListener() {
        this(LegendsPlayerService.getInstance());
    }

    public TutorialListener(LegendsPlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        LegendsPlayer player = playerService.getLegendsPlayer(event.getPlayer().getUniqueId());
        if (player.getRace() == null) {
            // NOTE disabled for now: player.freeze(Integer.MAX_VALUE);
            MetadataValue metadata = new FixedMetadataValue(Monolith.getInstance(), this);
            player.setMetadata(METADATA_KEY, metadata);
            RunnableUtils.runLater(() -> player.openInventory(new RacePicker(player)), 2);
        }
    }
}
