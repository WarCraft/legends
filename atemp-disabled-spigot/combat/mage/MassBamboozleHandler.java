package gg.warcraft.legends.spigot.OLD.combat.mage;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;

public class MassBamboozleHandler {
    private final MassBamboozleRepository massBamboozleRepository;

    @Inject
    public MassBamboozleHandler(MassBamboozleRepository massBamboozleRepository) {
        this.massBamboozleRepository = massBamboozleRepository;
    }

    @Subscribe
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        boolean isBamboozled = massBamboozleRepository.isBamboozled(event.getCasterId());
        if (isBamboozled) {
            event.cancel();
        }
    }
}
