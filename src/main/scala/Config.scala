import InputKey.{DownArrow, LeftArrow, RightArrow, UpArrow}
import Mutation.{Direction, KeyHeld, KeyPressed, KeyReleased}

object Config {
  val windowName: String = "GridEngine"
  val windowSize: Vector2 = Vector2(800,800)
  val fps: Int = 60
  val scrollUnit = 15 //todo: find better name and better place (if scroll becomes part of Overworld, that might be the place)

  val keyMap: Map[Mutation, Mutation] = Map(
    KeyPressed(UpArrow) -> Direction(Vector2.Up),
    KeyHeld(UpArrow) -> Direction(Vector2.Up),
    KeyPressed(DownArrow) -> Direction(Vector2.Down),
    KeyHeld(DownArrow) -> Direction(Vector2.Down),
    KeyPressed(LeftArrow) -> Direction(Vector2.Left),
    KeyHeld(LeftArrow) -> Direction(Vector2.Left),
    KeyPressed(RightArrow) -> Direction(Vector2.Right),
    KeyHeld(RightArrow) -> Direction(Vector2.Right),
  )

}
