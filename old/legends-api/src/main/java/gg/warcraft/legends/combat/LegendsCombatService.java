package gg.warcraft.legends.combat;

import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.combat.CombatService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.player.MonolithPlayer;
import org.bukkit.entity.Tameable;

public interface LegendsCombatService extends CombatService {

    static LegendsCombatService getInstance() {
        return (LegendsCombatService) Monolith.getInstance().getService(Monolith.Service.COMBAT);
    }

    Tameable getCombatPetFor(MonolithPlayer player);

    MonolithEntity getOwnerFor(Tameable combatPet);
}
