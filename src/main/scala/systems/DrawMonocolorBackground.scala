package systems

import components.BackgroundColor
import engine.{State, System, World}
import render.Layer
import util.Vector2

object DrawMonocolorBackground extends System{
  override def update(newWorld: World, state: State, deltaTime: Long): Unit = {
    state.grid.cells = Vector2(1)
    state.selectEntities(BackgroundColor).view.foreach(
      _.get(BackgroundColor).foreach(c => state.grid.drawOnCenter(c.color, Vector2(1), Layer.Foreground)))
  }
}
