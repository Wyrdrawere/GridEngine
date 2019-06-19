sealed trait Tile

object Tile {
  case class Color(red: Float, green: Float, blue: Float) extends Tile

  object Color {
    val White = Color(1,1,1)
    val Black = Color(0,0,0)
    val Red = Color(1,0,0)
    val Green = Color(0,1,0)
    val Blue = Color(0,0,1)
    val Pink = Color(1,0,1)

    val simpleMap: Map[Int, Color] = Map(
      0 -> White,
      1 -> Black,
      2 -> Red,
      3 -> Green,
      4 -> Blue,
      5 -> Pink
    )
  }

  case class Texture(id: Int) extends Tile

  object Texture {
    def OpenPathSet(): Map[Int, Texture] = Map(
      0 -> TextureLoad("src/resources/OpenSquare/Open Square Path Cross.png"),
      1 -> TextureLoad("src/resources/OpenSquare/Open Square Path Grass.png"),
      2 -> TextureLoad("src/resources/OpenSquare/Open Square Path Horizontal.png"),
      3 -> TextureLoad("src/resources/OpenSquare/Open Square Path Vertical.png"),
      4 -> TextureLoad("src/resources/OpenSquare/Open Square Path Corner NW.png"),
      5 -> TextureLoad("src/resources/OpenSquare/Open Square Path Corner NE.png"),
      6 -> TextureLoad("src/resources/OpenSquare/Open Square Path Corner SW.png"),
      7 -> TextureLoad("src/resources/OpenSquare/Open Square Path Corner SE.png"),
    )

    def BlackMageSprite(): Texture = TextureLoad("src/resources/Sprite/blm.png")
  }
}