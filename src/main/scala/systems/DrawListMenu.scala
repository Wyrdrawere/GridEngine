package systems

import components.{Cursor, ListMenu, Position}
import drawables.Color
import engine.{State, System, World}
import util.{Config, Vector2}

object DrawListMenu extends System {
  override def update(world: World, state: State, deltaTime: Long): Unit = {
    val offset = 0.2f
    state.selectEntities(ListMenu).foreach(_.get(ListMenu).foreach(l => {
      val x = l.items.map(_._1.string.length).max
      state.grid.squareCells(x + 3)
      for (p <- l.items.indices) {
        state.grid.drawOnGrid(l.items(p)._1, Vector2(l.items(p)._1.string.length), Vector2(2, state.grid.cells.y - 2 - (offset+1)*p))
      }
      val c = state.selectEntities(Cursor).head.get(Cursor) match {
        case Some(d) => d.drawable
        case None => Color.White
      }
      state.selectEntities(Cursor).head.get(Position).foreach(p => {
        state.grid.drawOnGrid(c, Vector2(1), Vector2(1, state.grid.cells.y - 2 - (offset+1)*p.value.y))
      })
    }))




  }
}
