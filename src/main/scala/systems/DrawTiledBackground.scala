package systems

import components.{Background, Position, Scroll, Zoom}
import engine.{System, World, State}
import render.Grid
import util.Vector2

object DrawTiledBackground extends System {

  override def update(newWorld: World, state: State, deltaTime: Long): Unit = {
    state.selectEntities(Background).view.foreach(e => e.get(Background).foreach(bg => {
      val offset = e.get(Scroll) match {
        case Some(Scroll(p, m, d)) => d*p/m*(-1)
        case None => Vector2(0)
      }
      val zoom = e.get(Zoom) match {
        case Some(z) => z.value
        case None => 5
      }
      val pos = e.get(Position) match {
        case Some(Position(value)) => value
        case None => Vector2(0)
      }
      val data = getSlice(bg.data, pos, Vector2(zoom*2+1), bg.blank)
        .map(x => x.map(y => bg.drawInfo(y)))
      state.grid.drawGrid(data, offset)
    }))
  }

  private def getSlice(level: Array[Array[Int]], centerPos: Vector2, size: Vector2, blank: Int): Array[Array[Int]] = {
    val slice = Array.ofDim[Int](size.xi, size.yi)
    for (x <- slice.indices; y <- slice(x).indices) {
      val levelX = centerPos.xi - (size.xi / 2) + x
      val levelY = centerPos.yi - (size.yi / 2) + y
      if (level.indices.contains(levelX) && level(0).indices.contains(levelY)) {
        slice(x)(y) = level(levelX)(levelY)
      } else {
        slice(x)(y) = blank
      }
    }
    slice
  }
}
