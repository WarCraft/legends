package gg.warcraft.legends;

import gg.warcraft.monolith.sound.MonolithSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public enum LegendsCombatSound implements MonolithSound {
    // Warrior
    HAMSTRING_HIT(Sound.ENTITY_SILVERFISH_DEATH),
    MAUL_HIT(Sound.ENTITY_ZOMBIE_STEP),
    SLAM_HIT(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR),

    // Hunter
    DISENGAGE_CAST(Sound.ENTITY_ENDERDRAGON_FLAP),
    SNEAK_CAST(Sound.ENTITY_PLAYER_BREATH),

    // Mage
    ARCANEMISSILE_CAST(Sound.ENTITY_ENDERDRAGON_HURT),
    ARCANEMISSILE_HIT(Sound.ENTITY_ENDERDRAGON_HURT),
    TELEPORT_CAST(Sound.ENTITY_ENDERMEN_TELEPORT),
    TELEPORT_EXPLOSION(Sound.ENTITY_ENDERMEN_TELEPORT),
    HAILBLAST_CAST(Sound.BLOCK_SNOW_BREAK),
    HAILBLAST_CAST2(Sound.ENTITY_SILVERFISH_HURT, 0.1f);

    private static final float DEFAULT_VOLUME = 12f;

    private final Sound sound;
    private final float pitch;

    LegendsCombatSound(Sound sound) {
        this(sound, 1);
    }

    LegendsCombatSound(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }

    @Override
    public void play(Location location) {
        location.getWorld().playSound(location, sound, SoundCategory.HOSTILE, DEFAULT_VOLUME, pitch);
    }
}
