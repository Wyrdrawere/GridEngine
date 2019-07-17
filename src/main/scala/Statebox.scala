abstract class Statebox

object Statebox{

  case class OverworldBox
  (level: Level,
   playerSprite: OverworldSprite,
   pos: Vector2,
   zoom: Int,
   scroller: Scroller,
  )extends Statebox

  case class ListMenuBox
  (
  cursor: Int,
  items: Map[Int, (Drawable, Mutation)]
  )extends Statebox

  case class JobMenuBox
  (
  cursor: Vector2,
  items: Map[Vector2, (Drawable, Mutation)]
  )extends Statebox
}