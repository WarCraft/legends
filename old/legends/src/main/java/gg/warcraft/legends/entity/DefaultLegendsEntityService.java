package gg.warcraft.legends.entity;

import gg.warcraft.legends.combat.AntiCombatLogger;
import gg.warcraft.legends.combat.LegendsCombatService;
import gg.warcraft.monolith.entity.DefaultEntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.player.PlayerService;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class DefaultLegendsEntityService extends DefaultEntityService {

    public DefaultLegendsEntityService(PlayerService playerService) {
        super(playerService);
    }

    @Override
    public MonolithEntity getMonolithEntityFor(LivingEntity livingEntity) {
        // return combat pet wrapper if this is a pet to return the race of the owner on getRace
        if (livingEntity instanceof Tameable) {
            MonolithEntity petOwner = LegendsCombatService.getInstance().getOwnerFor((Tameable) livingEntity);
            if (petOwner != null && petOwner instanceof MonolithPlayer) {
                return new CombatPetMonolithEntityDecorator(livingEntity, (MonolithPlayer) petOwner);
            }
        }
        // return anti combat log armor stand wrapper if this is a substitute to return the race of the owner on getRace
        Player substitutedPlayer = AntiCombatLogger.getInstance().getSubstitutedPlayerFor(livingEntity);
        if (substitutedPlayer != null) {
            MonolithPlayer monolithPlayer = PlayerService.getInstance().getMonolithPlayer(substitutedPlayer.getUniqueId());
            return new CombatLoggingSubstituteMonolithEntityDecorator( livingEntity, monolithPlayer);
        }
        return super.getMonolithEntityFor(livingEntity);
    }
}
