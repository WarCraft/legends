package gg.warcraft.legends.app.combat.warrior.human;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.legends.app.combat.warrior.status.WarriorStatusEffectFactory;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import javax.annotation.Nullable;

public class LastStand extends AbstractAbility implements RangedAbility {

    @Inject
    public LastStand(PlayerQueryService playerQueryService,
                     StatusCommandService statusCommandService,
                     WorldCommandService worldCommandService,
                     WarriorStatusEffectFactory warriorStatusEffectFactory,
                     @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                     @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        StatusEffect effect = warriorStatusEffectFactory.createLastStandStatusEffect(getCasterId(), snapshot.getDuration());
        statusCommandService.addStatusEffect(effect, getCasterId());

        Player player = playerQueryService.getPlayer(getCasterId());
        worldCommandService.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.HOSTILE, 1, 1);
    }

    @Override
    public boolean onHit(Entity entity) {
        // do nothing
        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        // do nothing
        return false;
    }
}
