package gg.warcraft.legends.combat.ranger.marksman;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.event.MonolithPlayerConnectEvent;
import gg.warcraft.monolith.event.MonolithPlayerDisconnectEvent;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.player.PlayerService;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

public class Sneak extends SimpleMonolithAbility implements Listener {
    private static final Monolith monolith = Monolith.getInstance();

    private final PlayerService playerService;

    private Player casterAsPlayer;

    public Sneak(MonolithEntity caster, PotionConfiguration speed) {
        this(EventService.getInstance(), PlayerService.getInstance(), LegendsCombatSound.SNEAK_CAST, caster, speed);
    }

    public Sneak(EventService eventService, PlayerService playerService, MonolithSound onCastSound,
                 MonolithEntity caster, PotionConfiguration speed) {
        super(caster);
        this.playerService = playerService;
        if (caster instanceof MonolithPlayer) {
            casterAsPlayer = ((MonolithPlayer) caster).getPlayer();
            playerService.getOnlineMonolithPlayers().forEach(player -> player.hidePlayer(monolith, casterAsPlayer));
            eventService.registerForEvents(this);
            RunnableUtils.runLater(this::reveal, speed.getDuration().as(Duration.Type.TICKS));
        }
        caster.addPotionEffect(PotionEffectType.SPEED, speed);
        onCastSound.play(caster.getLocation());
    }

    private void reveal() {
        playerService.getOnlineMonolithPlayers().forEach(player -> player.showPlayer(monolith, casterAsPlayer));
        HandlerList.unregisterAll(this);
    }

    @Override
    public String toString() {
        return "Sneak";
    }

    @EventHandler
    public void onMonolithPlayerConnectEvent(MonolithPlayerConnectEvent event) {
        event.getPlayer().hidePlayer(monolith, casterAsPlayer);
    }

    @EventHandler
    public void onMonolithPlayerDisconnectEvent(MonolithPlayerDisconnectEvent event) {
        event.getPlayer().showPlayer(monolith, casterAsPlayer);
    }
}
