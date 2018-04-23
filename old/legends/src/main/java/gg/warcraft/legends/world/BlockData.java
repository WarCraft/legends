package gg.warcraft.legends.world;

import org.bukkit.Material;

public class BlockData {

    private final Material material;
    private final byte data;

    public BlockData(Material material, Byte data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }
}
