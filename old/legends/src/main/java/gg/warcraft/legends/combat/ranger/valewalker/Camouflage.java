package gg.warcraft.legends.combat.ranger.valewalker;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class Camouflage extends SimpleMonolithAbility implements Listener {

    private BukkitTask task;

    public Camouflage(MonolithEntity caster, Duration duration, PotionConfiguration invisibility, PotionConfiguration regen) {
        this(EventService.getInstance(), caster, duration, invisibility, regen);
    }

    public Camouflage(EventService eventService, MonolithEntity caster, Duration duration, PotionConfiguration invisibility,
                      PotionConfiguration regen) {
        super(caster);
        caster.addPotionEffect(PotionEffectType.INVISIBILITY, invisibility);
        caster.addPotionEffect(PotionEffectType.REGENERATION, regen);
        task = RunnableUtils.runLater(this::terminate, duration.as(TICKS));
        eventService.registerForEvents(this);
    }

    private void terminate() {
        HandlerList.unregisterAll(this);
        task.cancel();
        getCaster().removePotionEffect(PotionEffectType.INVISIBILITY);
        getCaster().removePotionEffect(PotionEffectType.REGENERATION);
    }

    @Override
    public String toString() {
        return "Camouflage";
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (event.getPlayer() == getCaster()) {
            terminate();
        }
    }
}
