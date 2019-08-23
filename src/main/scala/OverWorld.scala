class OverWorld extends World {


  override def entities: List[Entity] = player :: Nil

  private val player: Player = new Player

  def getPlayer = player

}

class Player extends Entity {
  override def update(deltaTime: Long, world: World): Unit = ???

  override def render(grid: Grid): Unit = ???
}