package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.entity.event.EntityPreFatalDamageEvent;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import java.util.UUID;

public class HumanWarriorPassive extends AbstractRacialPassive {
    private static final String NAME = "Last Stand";
    private static final int DURATION_IN_MILLIS = 4000;
    private static final long COOLDOWN_IN_MILLIS = 300 * TimeUtils.MILLIS_PER_SECOND;

    private final WorldCommandService worldCommandService;
    private final PlayerQueryService playerQueryService;

    private long invulnerableUntil;
    private long cooldown;

    @Inject
    public HumanWarriorPassive(WorldCommandService worldCommandService,
                               PlayerQueryService playerQueryService,
                               @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.worldCommandService = worldCommandService;
        this.playerQueryService = playerQueryService;
    }

    @Override
    public void onEntityPreFatalDamageEvent(EntityPreFatalDamageEvent event) {
        long now = System.currentTimeMillis();
        if (now <= invulnerableUntil) {
            event.cancel();
        } else if (now >= cooldown) {
            Player player = playerQueryService.getPlayer(event.getEntityId());
            worldCommandService.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.HOSTILE, 1, 1);
            invulnerableUntil = now + DURATION_IN_MILLIS;
            cooldown = now + COOLDOWN_IN_MILLIS;
            event.cancel();
        }
    }
}
