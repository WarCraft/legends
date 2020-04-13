package gg.warcraft.legends.api.combat.hunter.ranger

class ToxicArrow {

}

/*
public interface ToxicArrowFactory extends MeleeAbilityFactory { // TODO rename this AbilityFactory and the other one to TargetedAbilityFactory? Who then gets to register for projectiles?

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}

 */

/*
public class ToxicArrow extends AbstractAbility implements MeleeAbility {
    private static final float SPEED = 3; // suggested speed of 0.6 by the spigot docs is way too low
    private static final float SPREAD = 2; // suggested spread of 12 by the spigot docs is way too wide

    private final EntityCommandService entityCommandService;

    @Inject
    public ToxicArrow(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                      @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                      @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        abilityCommandService.createArrow(this, caster.getId(), caster.getEyeLocation(),
                caster.getEyeLocation().rotation(), SPEED, SPREAD);
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        PotionEffect poison = getSnapshot().getPotionEffect(PotionEffectType.POISON);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.addPotionEffect(entity.getId(), poison);
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}

 */