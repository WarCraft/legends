package gg.warcraft.legends.app.combat.warrior.berserker;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Hitbox;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.location.Location;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class Shatter extends AbstractAbility implements RangedAbility {
    private final EntityQueryService entityQueryService;
    private final AbilityCommandService abilityCommandService;
    private final EntityCommandService entityCommandService;
    private final MovementFactory movementFactory;
    private final HitboxFactory hitboxFactory;

    private final Team casterTeam;
    private final CombatValue damage;
    private final float launch;
    private final PotionEffect confusion;

    private boolean grounded = false;

    @Inject
    public Shatter(EntityQueryService entityQueryService,
                   AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                   TaskService taskService, MovementFactory movementFactory, HitboxFactory hitboxFactory,
                   TimeUtils timeUtils, @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                   @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityQueryService = entityQueryService;
        this.abilityCommandService = abilityCommandService;
        this.entityCommandService = entityCommandService;
        this.movementFactory = movementFactory;
        this.hitboxFactory = hitboxFactory;

        this.casterTeam = caster.getTeam();
        this.damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        this.launch = getSnapshot().getFloat(AbilityConfigurationValue.LAUNCH);
        this.confusion = getSnapshot().getPotionEffect(PotionEffectType.CONFUSION);

        float distance = snapshot.getFloat(AbilityConfigurationValue.DISTANCE);
        Vector3f direction = caster.getEyeLocation().getDirection();
        entityCommandService.leap(caster.getId(), direction, distance);

        Movement movement = movementFactory.createStationaryMovement(caster.getLocation());
        Hitbox hitbox = hitboxFactory.createVoidHitbox();
        taskService.runLater(() -> abilityCommandService.createProjectile(this, movement, hitbox, () -> {}),
                timeUtils.createDurationInMillis(500));
    }

    @Override
    public boolean onHit(Entity entity) {
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.knockup(entity.getId(), launch);
        entityCommandService.addPotionEffect(entity.getId(), confusion);
        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }

    @Override
    public boolean onGrounded(Location location) {
        return false;
    }

    @Override
    public boolean onTick() {
        if (grounded) {
            return false;
        }

        Entity caster = entityQueryService.getEntity(getCasterId());
        if (caster.isGrounded()) {
            grounded = true;

            float radius = getSnapshot().getFloat(AbilityConfigurationValue.RADIUS);
            Movement movement = movementFactory.createOneOffMovement(caster.getLocation());
            Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != casterTeam);
            abilityCommandService.createProjectile(this, movement, hitbox, () -> {});
            return true;
        }
        return false;
    }
}
