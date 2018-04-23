package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.CustomEntity;
import net.minecraft.server.v1_12_R1.EntityIronGolem;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class MonolithGolem extends EntityIronGolem implements CustomEntity {

    public MonolithGolem(World world) {
        super(world);
    }

    public MonolithGolem(Location location) {
        this(((CraftWorld) location.getWorld()).getHandle());
        clearPathFinderGoalSelectors(goalSelector, targetSelector);
        // TODO: EntityModule.spawn(this, location);
    }

    protected void setMaxHealth(double value) {
        getAttributeInstance(GenericAttributes.maxHealth).setValue(value);
    }

    protected void setKnockbackResistance(double value) {
        getAttributeInstance(GenericAttributes.c).setValue(value);
    }

    protected void dropLoot() {

    }

    @Override
    public void dropDeathLoot(boolean flag, int i) {
        dropLoot();
    }

    @Override
    public int getExpReward() {
        return 0;
    }
}
