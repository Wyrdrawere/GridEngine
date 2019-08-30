package systems

import components.Zoom
import engine.{State, System, World}

object ZoomSystem extends System {
  override def update(World: World, state: State, deltaTime: Long): Unit = ()

  def changeZoom(z: Int)(world: World, state: State): Unit = {
    state.selectEntities(Zoom).foreach(_.modify[Zoom](zoom => {
      val zz = zoom.value+z
      Zoom(if(zz > 0) zz else zoom.value)
    }))
  }
}
