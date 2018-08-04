package gg.warcraft.legends.app.combat.ranger.marksman;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;

public class Focus extends AbstractAbility implements MeleeAbility {

    @Inject
    public Focus(@Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                 @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
    }

    @Override
    public void onHit(Entity entity) {

    }

    @Override
    public void onMiss(Entity entity) {

    }

    /*
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
    public void cancel() {
        getCaster().freeze(0);
        getCaster().removeStatusEffect(focusStatusEffect);
        focusEffect.cancel();
        getCaster().removeMetadata(METADATA_KEY, monolith);
    }
     */
}
