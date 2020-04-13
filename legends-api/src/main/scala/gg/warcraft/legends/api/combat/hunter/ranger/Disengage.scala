package gg.warcraft.legends.api.combat.hunter.ranger

class Disengage {

}


/*
public interface DisengageFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}

 */


/*
public class Disengage extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_ENDER_DRAGON_FLAP; // TODO use bat takeoff sound in stead?

    @Inject
    public Disengage(EntityCommandService entityCommandService, WorldService WorldService,
                     @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                     @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        Vector3f direction = caster.getEyeLocation().rotation()
                .withY(0)
                .multiply(-1);
        float distance = snapshot.getFloat("distance");
        entityCommandService.knockback(caster.getId(), direction, distance);
        WorldService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void onHit(Entity entity) {
        // do nothing
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
        // TODO do we want a specific MeleeAbility flag which does NOT do melee target hit/miss checks i.e.
        // TODO insta cast self buffs? Maybe even make an entirely new SelfCastFactory?
    }
}

 */