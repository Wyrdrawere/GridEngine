package drawables

import render.{Drawable, Layer}
import util.Vector2

case class Text
(
  string: String,
  font: Map[Char, Sprite]
) extends Drawable
{
  override def drawRectangle(size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit = {
    val text = string.toCharArray.map(c => font(c))
    val charUnit = size.x/text.length
    for (x <- text.indices) {
      text(x).drawRectangle(Vector2(charUnit), Vector2(position.x + (x*charUnit), position.y), layer)
    }
  }
}

object Text {
  val CharToInt: Map[Char,Int] = { //todo: dont even know, needs to be better
    val values = List.range(0,96) :+ 167
    val keys = List(
      '~', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-',
      '+', '!', '@', '#', '$', '%', '°', '&', '*', '(', ')', '_',
      '=', '{', '}', '[', ']', '|', '„', ':', ';', '”', '\'', '<',
      ',', '>', '.', '?', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
      'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
      'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
      'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
      'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '¢', '€', '–',
      ' '
    )

    (keys zip values).toMap
  }

  lazy val WhiteFont: Map[Char, Sprite] = {
    val sheet = Sprite.get("src/resources/Font/8x8Text/8x8text_whiteShadow.png", Vector2(96,112), Vector2(8))
    CharToInt.toList.map(a => (a._1, sheet(a._2))).toMap
  }

  lazy val DarkGrayFont: Map[Char, Sprite] = {
    val sheet = Sprite.get("src/resources/Font/8x8Text/8x8text_darkGrayShadow.png", Vector2(96,112), Vector2(8))
    CharToInt.toList.map(a => (a._1, sheet(a._2))).toMap
  }
}