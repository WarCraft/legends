package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.status.CombatStatusEffect;
import gg.warcraft.legends.api.combat.LegendsSpecialization;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;

import java.util.UUID;

public class AttributesPassive extends AbstractLegendsPassive implements CombatStatusEffect {
    private static final String EFFECT_NAME = "Attributes";

    private final EntityQueryService entityQueryService;
    private final LegendsPlayerQueryService playerQueryService;
    private final CombatFactory combatFactory;

    @Inject
    public AttributesPassive(EntityQueryService entityQueryService,
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

        float modifier = 0;
        Entity entity = entityQueryService.getEntity(getEntityId());
        if (specialization.getName().equals(LegendsSpecialization.ARMSMASTER.getName())) {
            float strength = entity.getAttributes().getValue(LegendsAttribute.STRENGTH);
            modifier = strength * 0.1f;
        } else if (specialization.getName().equals(LegendsSpecialization.BERSERKER.getName())) {
            float strength = entity.getAttributes().getValue(LegendsAttribute.STRENGTH);
            modifier = strength * 0.1f;
        } else if (specialization.getName().equals(LegendsSpecialization.HUNTER.getName())) {
            float dexterity = entity.getAttributes().getValue(LegendsAttribute.DEXTERITY);
            modifier = dexterity * 0.1f;
        } else if (specialization.getName().equals(LegendsSpecialization.MARKSMAN.getName())) {
            float dexterity = entity.getAttributes().getValue(LegendsAttribute.DEXTERITY);
            modifier = dexterity * 0.1f;
        } else if (specialization.getName().equals(LegendsSpecialization.TEMPEST.getName())) {
            float intelligence = entity.getAttributes().getValue(LegendsAttribute.INTELLIGENCE);
            modifier = intelligence * 0.1f;
        } else if (specialization.getName().equals(LegendsSpecialization.WIZARD.getName())) {
            float intelligence = entity.getAttributes().getValue(LegendsAttribute.INTELLIGENCE);
            modifier = intelligence * 0.1f;
        }

        CombatValueModifier damageModifier =
                combatFactory.createCombatValueModifier(CombatValueModifierType.FLAT, modifier, this);
        event.addDamageModifier(damageModifier);
    }
}
