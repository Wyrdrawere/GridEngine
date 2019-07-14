import Mutation.{Direction, Identity}

case class JobMenu
(
  cursor: Vector2,

  grid: Grid,
  childState: Option[Stateful] = None,
  returnMutation: Mutation = Identity
) extends Stateful {


  var currentFirst = 0
  var sprites = Sprite.get("src/resources/Sprite/ff1-classes.png", Vector2(972,432), Vector2(36))
    .toList.filter(x => x._1 % 27 == 0).sortBy(x => x._1)
  for (x <- sprites.indices) {
    sprites = sprites.updated(x, (x,sprites(x)._2))
  }
  var evens = sprites.filter(x => x._1 % 2 == 0)
  var odds = sprites.filter(x => x._1 % 2 == 1)


  override def mutate(mutation: Mutation): Stateful = mutation match {
    case Direction(Vector2.Up) => this.copy(cursor = if (cursor.x < 1) cursor+Vector2.Up else cursor)
    case Direction(Vector2.Down) => this.copy(cursor = if (cursor.x > 0) cursor+Vector2.Down else cursor)
    case Direction(Vector2.Left) => this.copy(cursor = if (cursor.y > 0) cursor+Vector2.Left else cursor)
    case Direction(Vector2.Right) => this.copy(cursor = if (cursor.y < evens.length) cursor+Vector2.Right else cursor)

    case _ => this
  }

  override def draw(grid: Grid): Unit = {
    grid.setDimensions(Vector2(3))
    for(x <- 0 to 3; y <- 0 to 3) {
      grid.drawOnGrid(Color.Blue.translucent(0.75f), Vector2(x,y), Vector2(0))
    }
    grid.setDimensionsSquare(7)
    for(x <- 0 to 1; y <- cursor.yi to cursor.yi+1) {
      if (x % 2 == 0) {
        grid.drawOnGrid()
      }
      grid.drawOnGrid(sprites(x+y)._2, Vector2(3+2*x, 3+2*y), Vector2(0))
    }
  }
}
