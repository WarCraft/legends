package gg.warcraft.legends.api.combat.hunter.marksman

class PowerShot {

}


/*

public interface PowerShotFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}

 */

/*
public class PowerShot extends AbstractAbility implements MeleeAbility {
    private static final float SPEED = 3; // suggested speed of 0.6 by the spigot docs is way too low
    private static final float SPREAD = 2; // suggested spread of 12 by the spigot docs is way too wide

    private final EntityCommandService entityCommandService;
    private final Vector3f knockbackDirection;

    @Inject
    public PowerShot(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                     @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                     @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.knockbackDirection = caster.getEyeLocation().rotation();
        abilityCommandService.createArrow(this, caster.getId(), caster.getEyeLocation(),
                caster.getEyeLocation().rotation(), SPEED, SPREAD);
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        float knockback = getSnapshot().getFloat(AbilityConfigurationValue.KNOCKBACK);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.knockback(entity.getId(), knockbackDirection, knockback);
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}

 */