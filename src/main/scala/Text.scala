case class Text
(
  string: String,
  font: Map[Char, Sprite]
) extends Drawable
{
  override def drawRectangle(size: Vector2, position: Vector2): Unit = {
    val text = string.toCharArray.map(c => font(c))
    val charUnit = size.x/text.length
    for (x <- text.indices) {
      text(x).drawRectangle(Vector2(charUnit), Vector2(position.x + (x*charUnit), position.y))
    }
  }
}
