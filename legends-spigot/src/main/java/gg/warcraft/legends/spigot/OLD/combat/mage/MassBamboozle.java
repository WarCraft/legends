package gg.warcraft.legends.spigot.OLD.combat.mage;

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
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.location.Location;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Server;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MassBamboozle extends AbstractAbility implements RangedAbility {
    private static final Random random;
    private static final List<MobDisguise> disguises;

    static {
        random = new Random(System.currentTimeMillis());
        disguises = new ArrayList<>();
        disguises.add(new MobDisguise(DisguiseType.COW));
        disguises.add(new MobDisguise(DisguiseType.PIG));
        disguises.add(new MobDisguise(DisguiseType.SHEEP));
    }

    private final Server server;
    private final MassBamboozleRepository massBamboozleRepository;
    private final TaskService taskService;

    @Inject
    public MassBamboozle(Server server, AbilityCommandService abilityCommandService, MovementFactory movementFactory,
                         HitboxFactory hitboxFactory, MageEffectFactory mageEffectFactory, TaskService taskService,
                         MassBamboozleRepository massBamboozleRepository,
                         @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                         @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.server = server;
        this.massBamboozleRepository = massBamboozleRepository;
        this.taskService = taskService;
        Location location;
        if (rangedTarget != null) {
            location = rangedTarget.getLocation();
        } else if (blockTarget != null) {
            location = blockTarget.getLocation().toLocation().add(0.5f, 1, 0.5f);
        } else {
            throw new IllegalStateException("Failed to cast Mass Bamboozle with null entity and block targets");
        }

        Cancellable effect = mageEffectFactory.createMassBamboozleCountdownEffect(location);

        CombatValue radius = snapshot.getValue(AbilityConfigurationValue.RADIUS);
        Movement movement = movementFactory.createOneOffMovement(location);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius.getModifiedValue(), entity -> entity.getTeam() != caster.getTeam());
        Duration countdown = snapshot.getDuration("countdown");
        taskService.runLater(() -> {
            effect.cancel();
            abilityCommandService.createProjectile(this, movement, hitbox, () -> {});
        }, countdown);
    }

    private MobDisguise getRandomDisguise() {
        int disguiseCount = disguises.size();
        int randomIndex = random.nextInt(disguiseCount);
        return disguises.get(randomIndex);
    }

    @Override
    public boolean onHit(Entity entity) {
        UUID entityId = entity.getId();

        MobDisguise randomDisguise = getRandomDisguise();
        org.bukkit.entity.Entity spigotEntity = server.getEntity(entityId);
        DisguiseAPI.disguiseToAll(spigotEntity, randomDisguise);

        massBamboozleRepository.save(entityId);
        Duration duration = getSnapshot().getDuration();
        taskService.runLater(() -> {
            massBamboozleRepository.delete(entityId);
            DisguiseAPI.undisguiseToAll(spigotEntity);
        }, duration);

        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        // do nothing
        return false;
    }
}
