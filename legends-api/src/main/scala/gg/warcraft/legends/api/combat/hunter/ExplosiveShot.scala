package gg.warcraft.legends.api.combat.hunter

class ExplosiveShot {

}


/*
public interface ExplosiveShotFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}
 */

/*
  public ExplosiveShot(EntityService entityService, MonolithEntity caster, double damage, double knockback, double radius) {
      super(caster);
      this.entityService = entityService;
      this.damage = damage;
      this.knockback = knockback;
      this.radius = radius;

      MonolithArrow arrow = new MonolithArrow(caster, new HostileTo(caster));
      arrow.addObserver(this);
  }

  private void explode(Location location) {
      List<MonolithEntity> casualties = entityService.getNearbyMonolithEntities(location, radius);
      casualties.forEach(entity -> {
          entity.damage(damage);
          entity.knockback(location, knockback);
      });

      World world = location.getWorld();
      world.createExplosion(location.getX(), location.getY(), location.getZ(), 0, false, false);
  }

  @Override
  public void onGrounded(Location location) {
      explode(location);
  }

  @Override
  public void onHit(MonolithEntity entity) {
      explode(entity.getLocation());
  }
  */