package gg.warcraft.legends.app.combat.mage.wizard;

import gg.warcraft.legends.LegendsCombatSound;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.sound.MonolithSound;
import gg.warcraft.monolith.util.MaterialUtils;
import gg.warcraft.monolith.util.PotionConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import java.util.Collection;
import java.util.stream.Collectors;

public class Teleport extends SimpleMonolithAbility {

    public Teleport(MonolithEntity caster, AbilityConfiguration config) {
        this(EntityService.getInstance(), LegendsCombatSound.TELEPORT_CAST, LegendsCombatSound.TELEPORT_EXPLOSION,
                caster, config);
    }

    public Teleport(EntityService entityService, MonolithSound onCastSound, MonolithSound onExplosionSound,
                    MonolithEntity caster, AbilityConfiguration config) {
        super(caster, config);
        Location teleportLocation = null;
        int range = (int) config.get(AbilityConfiguration.Attribute.RANGE);
        Block block = caster.getTargetBlock(MaterialUtils.NON_SOLIDS, range);
        if (block != null) {
            // prevent players from messing about around barriers
            if (block.getType() == Material.BARRIER) {
                return;
            }

            Block relative = block.getRelative(BlockFace.UP);
            if (relative.isEmpty()) {
                teleportLocation = relative.getLocation();
                teleportLocation.setDirection(caster.getEyeLocation().getDirection());
            } else {
                Location blockToCaster = block.getLocation();
                blockToCaster.setDirection(caster.getEyeLocation().getDirection());
                BlockIterator iterator = new BlockIterator(blockToCaster);
                if (iterator.hasNext()) {
                    teleportLocation = iterator.next().getLocation();
                    teleportLocation.setDirection(caster.getEyeLocation().getDirection());
                }
            }
        }

        if (teleportLocation != null) {
            Location originalLocation = caster.getLocation();
            if (caster.teleport(teleportLocation, PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                double radius = config.get(AbilityConfiguration.Attribute.RADIUS);
                PotionConfiguration slow = config.getPotionConfiguration(PotionEffectType.SLOW);
                onCastSound.play(originalLocation);
                onExplosionSound.play(caster.getLocation());
                Collection<MonolithEntity> casualties = entityService.getNearbyMonolithEntities(caster.getLocation(), radius).stream()
                        .filter(entity -> entity.getRace() != caster.getRace())
                        .collect(Collectors.toList());
                publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> {
                    damageEvent.getEntity().damage(damageEvent.getDamage());
                    damageEvent.getEntity().addPotionEffect(PotionEffectType.SLOW, slow);
                });
            }
        }
    }

    @Override
    public String toString() {
        return "Teleport";
    }
}
