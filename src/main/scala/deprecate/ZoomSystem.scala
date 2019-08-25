package deprecate

import components.{Background, Zoom}

object ZoomSystem extends System {
  override def update(world: World, deltaTime: Long): Unit = ()

  def zoom(change: Int)(deltaTime: Long, world: World): Unit = {
    world.selectEntities(Background).view.foreach(_.modify[Zoom](z =>
      Zoom(
        if(z.value+change > 0) z.value+change else z.value
      )))
  }
}
