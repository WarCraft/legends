package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.status.CombatStatusEffect;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;

import java.util.Random;
import java.util.UUID;

public class CritPassive extends AbstractLegendsPassive implements CombatStatusEffect {
    private static final String EFFECT_NAME = "Crit";
    private static final float DEFAULT_CRIT = 5;
    private static final float CRIT_MODIFIER = 1; // a modifier of 1 means double damage (value is additive)
    private static final Random random;

    static {
        random = new Random(System.currentTimeMillis());
    }

    private final EntityQueryService entityQueryService;
    private final LegendsPlayerQueryService playerQueryService;
    private final CombatFactory combatFactory;

    @Inject
    public CritPassive(EntityQueryService entityQueryService,
                       LegendsPlayerQueryService playerQueryService,
                       CombatFactory combatFactory, @Assisted UUID entityId) {
        super(entityId, EFFECT_NAME, null);
        this.entityQueryService = entityQueryService;
        this.playerQueryService = playerQueryService;
        this.combatFactory = combatFactory;
    }

    @Override
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        LegendsPlayer player = playerQueryService.getPlayer(event.getCasterId());
        if (player == null) {
            return;
        }

        Specialization specialization = player.getSpecialization();
        if (specialization == null) {
            return;
        }

        Entity entity = entityQueryService.getEntity(getEntityId());
        float crit = entity.getAttributes().getValue(LegendsAttribute.CRIT);
        float critChance = (crit + DEFAULT_CRIT) * 0.01f;
        if (random.nextFloat() <= critChance) {
            CombatValueModifier damageModifier =
                    combatFactory.createCombatValueModifier(CombatValueModifierType.PERCENT, CRIT_MODIFIER, this);
            event.addDamageModifier(damageModifier);
        }
    }
}
