package systems

import components.Health
import engine.{System, World}

object HealthRegeneration extends System
{
  override def update(
    world: World,
    delta: Double,
  ): Unit =
    world.allEntities.view.filter(_.has(Health)).foreach(_.modify[Health](health => Health(health.value)))
}
