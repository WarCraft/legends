package gg.warcraft.legends.api.combat.hunter.marksman

class Sneak {

}


/*
public interface SneakFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}
 */

/*
public class Sneak extends AbstractAbility implements MeleeAbility, Cancellable {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_PLAYER_BREATH;

    private final PlayerHidingCommandService playerHidingCommandService;

    @Inject
    public Sneak(EntityCommandService entityCommandService, PlayerHidingCommandService playerHidingCommandService,
                 WorldService WorldService, TaskService taskService, @Assisted AbilitySnapshot snapshot,
                 @Assisted("caster") Entity caster, @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.playerHidingCommandService = playerHidingCommandService;

        PotionEffect speed = snapshot.getPotionEffect(PotionEffectType.SPEED);
        entityCommandService.addPotionEffect(caster.getId(), speed);

        playerHidingCommandService.hidePlayer(caster.getId());
        taskService.runLater(this::cancel, speed.getDuration());

        WorldService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY, 1, 1);
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


 */
