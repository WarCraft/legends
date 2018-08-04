package gg.warcraft.legends.app.combat.ranger.marksman;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.hiding.PlayerHidingCommandService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

public class Sneak extends AbstractAbility implements MeleeAbility, Cancellable {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_PLAYER_BREATH;

    private final PlayerHidingCommandService playerHidingCommandService;

    @Inject
    public Sneak(EntityCommandService entityCommandService, PlayerHidingCommandService playerHidingCommandService,
                 WorldCommandService worldCommandService, TaskService taskService, @Assisted AbilitySnapshot snapshot,
                 @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.playerHidingCommandService = playerHidingCommandService;

        PotionEffect speed = snapshot.getPotionEffect(PotionEffectType.SPEED);
        entityCommandService.addPotionEffect(caster.getId(), speed);

        playerHidingCommandService.hidePlayer(caster.getId());
        taskService.runLater(this::cancel, speed.getDuration());

        worldCommandService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void cancel() {
        // TODO what about casting abilities? should that reveal?
        playerHidingCommandService.revealPlayer(getCasterId());
    }

    @Override
    public void onHit(Entity entity) {
        // do nothing
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}
