package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.MonolithEntityWrapper;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class CombatLoggingSubstituteMonolithEntityDecorator extends MonolithEntityWrapper {

    private final MonolithPlayer substitutedPlayer;

    public CombatLoggingSubstituteMonolithEntityDecorator(LivingEntity substitute, MonolithPlayer substitutedPlayer) {
        super(substitute);
        this.substitutedPlayer = substitutedPlayer;
    }

    @Override
    public Race getRace() {
        return substitutedPlayer.getRace();
    }

    @Override
    public UUID getUniqueId() {
        return substitutedPlayer.getUniqueId();
    }

    @Override
    public String getName() {
        return substitutedPlayer.getName();
    }
}
