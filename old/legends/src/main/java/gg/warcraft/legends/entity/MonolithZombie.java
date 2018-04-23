package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.CustomEntity;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class MonolithZombie extends EntityZombie implements CustomEntity {
    public MonolithZombie(World world) {
        super(world);
    }

    public MonolithZombie(Location location) {
        this(((CraftWorld) location.getWorld()).getHandle());
        clearPathFinderGoalSelectors(goalSelector, targetSelector);
        setKnockbackResistance(1.0);
        // TODO: EntityModule.spawn(this, location);
    }

    protected void setMaxHealth(double value) {
        getAttributeInstance(GenericAttributes.maxHealth).setValue(value);
    }

    protected void setKnockbackResistance(double value) {
        getAttributeInstance(GenericAttributes.c).setValue(value);
    }

    @Override
    public boolean isTypeNotPersistent() {
        return false;
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
