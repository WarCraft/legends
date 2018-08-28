package gg.warcraft.legends.app.combat.mage.wizard;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.world.block.Block;

import javax.annotation.Nullable;

public class MassBamboozle extends AbstractAbility implements RangedAbility {

    @Inject
    public MassBamboozle(AbilityCommandService abilityCommandService, PlayerCommandService playerCommandService,
                         @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                         @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        playerCommandService.sendMessage(caster.getId(), "Bamboozled");
        // TODO do the thing
    }

    @Override
    public boolean onHit(Entity entity) {
        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }
}
