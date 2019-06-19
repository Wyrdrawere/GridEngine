import Input.{Down, Left, Right, Up}

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
    def FF1_Classes(): Texture = TextureLoad("src/resources/Sprite/ff1-classes.png")
  }

  case class Sprite(id: Int, minX: Float, maxX: Float, minY: Float, maxY: Float) extends Tile

  object Sprite {
    def FF1_Classes: Texture = TextureLoad("src/resources/Sprite/ff1-classes.png")
    def BLM_Front1: Sprite = Sprite(FF1_Classes.id, 0.0f*36.0f/972.0f, 1.0f*36.0f/972.0f, 10.0f*36.0f/432.0f, 11.0f*36.0f/432.0f)
    def BLM_Back1: Sprite = Sprite(FF1_Classes.id, 2.0f*36.0f/972.0f, 3.0f*36.0f/972.0f, 10.0f*36.0f/432.0f, 11.0f*36.0f/432.0f)
    def BLM_Right1: Sprite = Sprite(FF1_Classes.id, 4.0f*36.0f/972.0f, 5.0f*36.0f/972.0f, 10.0f*36.0f/432.0f, 11.0f*36.0f/432.0f)
    def BLM_Left1: Sprite = Sprite(FF1_Classes.id, 6.0f*36.0f/972.0f, 7.0f*36.0f/972.0f, 10.0f*36.0f/432.0f, 11.0f*36.0f/432.0f)
    def Blm_WalkSprite: Map[Input, Sprite] = {
      var spriteMap: Map[Input, Sprite] = Map.empty
      var sprite = BLM_Front1
      spriteMap = spriteMap.updated(Down, sprite)
      sprite = BLM_Back1
      spriteMap = spriteMap.updated(Up, sprite)
      sprite = BLM_Right1
      spriteMap = spriteMap.updated(Left, sprite)
      sprite = BLM_Left1
      spriteMap = spriteMap.updated(Right, sprite)
      spriteMap
    }

    def Tileset: Texture = TextureLoad("src/resources/Tileset/basictiles.png")
    def g0: Sprite = Sprite(Tileset.id, 3.0f*16.0f/128.0f, 4.0f*16.0f/128.0f, 1.0f*16.0f/240.0f, 2.0f*16.0f/240.0f)
    def g1: Sprite = Sprite(Tileset.id, 4.0f*16.0f/128.0f, 5.0f*16.0f/128.0f, 1.0f*16.0f/240.0f, 2.0f*16.0f/240.0f)
    def g2: Sprite = Sprite(Tileset.id, 0.0f*16.0f/128.0f, 1.0f*16.0f/128.0f, 8.0f*16.0f/240.0f, 9.0f*16.0f/240.0f)
    def g3: Sprite = Sprite(Tileset.id, 1.0f*16.0f/128.0f, 2.0f*16.0f/128.0f, 8.0f*16.0f/240.0f, 9.0f*16.0f/240.0f)
    def tileSetMap: Map[Int, Sprite] = {
      var spriteMap: Map[Int, Sprite] = Map.empty
      var sprite = g1
      spriteMap = spriteMap.updated(0, sprite)
      sprite = g0
      spriteMap = spriteMap.updated(1, sprite)
      sprite = g2
      spriteMap = spriteMap.updated(2, sprite)
      sprite = g3
      spriteMap = spriteMap.updated(3, sprite)
      spriteMap
    }

  }
}