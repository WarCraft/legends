package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.MonolithEntityWrapper;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.entity.LivingEntity;

public class CombatPetMonolithEntityDecorator extends MonolithEntityWrapper {

    private final MonolithPlayer owner;

    public CombatPetMonolithEntityDecorator(LivingEntity livingEntity, MonolithPlayer owner) {
        super(livingEntity);
        this.owner = owner;
    }

    @Override
    public Race getRace() {
        return owner.getRace();
    }
}
