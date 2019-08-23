class OverWorld extends World {


  override def entities: List[Entity] = List.empty



}

class newLevel extends Entity {

  override def update(deltaTime: Long, world: World): Unit = ???

  override def render(grid: Grid): Unit = ???

}
