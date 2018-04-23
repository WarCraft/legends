package gg.warcraft.legends.combat.ranger.marksman;

import gg.warcraft.legends.combat.effect.ranger.marksman.FocusEffect;
import gg.warcraft.legends.event.LegendsPlayerSpecializationChangedEvent;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.effect.MonolithEffect;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.StatusEffect;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.event.PlayerDisconnectEvent;
import gg.warcraft.monolith.util.Cancellable;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Focus extends SimpleMonolithAbility implements Cancellable {
    private static final String METADATA_KEY = Focus.class.getCanonicalName();

    static {
        EventService eventService = EventService.getInstance();
        eventService.registerForEvents(new Listener() {
            @EventHandler
            public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
                List<MetadataValue> metadata = event.getPlayer().getMetadata(METADATA_KEY);
                if (!metadata.isEmpty()) {
                    Focus focus = (Focus) metadata.get(0).value();
                    focus.cancel();
                }
            }

            @EventHandler
            public void onLegendsPlayerSpecializationChangedEvent(LegendsPlayerSpecializationChangedEvent event) {
                List<MetadataValue> metadata = event.getPlayer().getMetadata(METADATA_KEY);
                if (!metadata.isEmpty()) {
                    Focus focus = (Focus) metadata.get(0).value();
                    focus.cancel();
                }
            }
        });
    }

    private final Plugin monolith;
    private final StatusEffect focusStatusEffect;
    private final MonolithEffect focusEffect;

    public Focus(MonolithEntity caster, AbilityConfiguration configuration) {
        this(Monolith.getInstance(), caster, configuration);
    }

    public Focus(Plugin monolith, MonolithEntity caster, AbilityConfiguration configuration) {
        super(caster, configuration);
        this.monolith = monolith;
        List<MetadataValue> metadata = caster.getMetadata(METADATA_KEY);
        if (metadata.isEmpty()) {
            double damageModifier = configuration.getDouble("damage-modifier");
            this.focusStatusEffect = new FocusStatusEffect(caster, damageModifier);
            caster.addStatusEffect(this.focusStatusEffect);
            this.focusEffect = new FocusEffect(caster);
            caster.freeze(Integer.MAX_VALUE);
            MetadataValue data = new FixedMetadataValue(monolith, this);
            caster.setMetadata(METADATA_KEY, data);

            caster.sendMessage(ChatColor.YELLOW + "You're currently focussing and can not move, cast [SHIFT LEFT CLICK] again to cancel!");
        } else {
            this.focusStatusEffect = null;
            this.focusEffect = null;
            Focus focus = (Focus) metadata.get(0).value();
            focus.cancel();
        }
    }

    @Override
    public String toString() {
        return "Focus";
    }

    @Override
    public void cancel() {
        getCaster().freeze(0);
        getCaster().removeStatusEffect(focusStatusEffect);
        focusEffect.cancel();
        getCaster().removeMetadata(METADATA_KEY, monolith);
    }
}
