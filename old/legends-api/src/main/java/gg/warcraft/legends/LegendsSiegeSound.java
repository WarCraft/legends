package gg.warcraft.legends;

import gg.warcraft.monolith.sound.MonolithSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public enum LegendsSiegeSound implements MonolithSound {
    TREBUCHET_CAST(Sound.ENTITY_FIREWORK_LAUNCH),
    TREBUCHET_HIT(Sound.BLOCK_STONE_HIT),
    TREBUCHET_GROUNDED(Sound.ENTITY_GENERIC_EXPLODE);

    private static final float DEFAULT_VOLUME = 7f;

    private final Sound sound;
    private final float pitch;

    LegendsSiegeSound(Sound sound) {
        this(sound, 1);
    }

    LegendsSiegeSound(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }

    @Override
    public void play(Location location) {
        location.getWorld().playSound(location, sound, SoundCategory.AMBIENT, DEFAULT_VOLUME, pitch);
    }
}
