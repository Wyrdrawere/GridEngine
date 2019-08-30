package systems

import components.{Cursor, PanelMenu, Position}
import engine.{State, System, World}
import util.Vector2

object DrawPanelMenu extends System {
  override def update(World: World, state: State, deltaTime: Long): Unit = {
    state.selectEntities(PanelMenu).foreach(_.get(PanelMenu).foreach(menu => {
      state.selectEntities(PanelMenu).foreach(_.get(Position).foreach(mpos => {
        state.selectEntities(Cursor).foreach(_.get(Position).foreach(cpos => {
          state.selectEntities(Cursor).foreach(_.get(Cursor).foreach(cursor => {
            state.grid.squareCells(5)
            state.grid.drawOnGrid(cursor.drawable, Vector2(1), cpos.value*2+Vector2(1, state.grid.cells.yi - 3))
            for(x <- menu.panels.indices; y <- menu.panels(x).indices) {
              if(x >= mpos.value.x && x <= mpos.value.x+1 + cpos.value.x) {
                state.grid.drawOnGrid(menu.panels(x)(y)._1, Vector2(1), Vector2(1+2*x-2*mpos.value.x, state.grid.cells.yi - 2-1+2*y))
              }
            }
          }))
        }))
      }))
    }))
  }
}
