package gg.warcraft.legends.app.combat.ranger.marksman;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import java.util.List;

public class Sneak extends AbstractAbility implements MeleeAbility, Cancellable {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_PLAYER_BREATH;

    private final List<? extends Player> players;

    @Inject
    public Sneak(EntityCommandService entityCommandService, PlayerQueryService playerQueryService,
                 WorldCommandService worldCommandService, TaskService taskService, @Assisted AbilitySnapshot snapshot,
                 @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        PotionEffect speed = snapshot.getPotionEffect(PotionEffectType.SPEED);
        entityCommandService.addPotionEffect(caster.getId(), speed);

        this.players = playerQueryService.getOnlinePlayers();
        this.players.forEach(player -> hide caster);
        taskService.runLater(this::cancel, speed.getDuration());

        worldCommandService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void cancel() {
        // TODO what about casting abilities? should that reveal?
        players.forEach(player -> reveal getCasterId());
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
