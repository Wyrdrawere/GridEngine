object LevelFormat {

  type LevelDataPure = (Array[Array[List[(Int, Int)]]], List[(String, Vector2, Vector2)])

  type LevelDataDraw = Array[Array[Tile]]

  type LevelDataEdit = (List[Array[Array[(Int, Int)]]], List[Map[Int, Sprite]])

  def pureToDraw(pure: LevelDataPure): LevelDataDraw = {
    var draw = Array.ofDim[Tile](pure._1.length, pure._1(0).length)
    var spriteSheets = List.empty[Map[Int, Sprite]]
    for (x <- 0 until pure._2.length) {
      spriteSheets = spriteSheets :+ DrawableStorage.spriteSheet(pure._2(x)._1, pure._2(x)._2, pure._2(x)._3)
    }
    for (x <- draw.indices; y <- draw(x).indices) {
      draw(x)(y) = Tile(for (a <- pure._1(x)(y)) yield spriteSheets(a._1)(a._2))
    }
    draw
  }

  def pureToEdit(pure: LevelDataPure): LevelDataEdit = {
    val max = (for (x <- pure._1.indices; y <- pure._1(x).indices) yield pure._1(x)(y).length).max
    var edit = for (x <- 0 until max) yield Array.ofDim[(Int,Int)](pure._1.length, pure._1(0).length)
    for (x <- edit.indices; y <- edit(x).indices; z <- edit(x)(y).indices) {
      edit(z)(x)(y) = pure._1(x)(y)(z)
    }
    var spriteSheets = List.empty[Map[Int, Sprite]]
    for (x <- 0 until pure._2.length) {
      spriteSheets = spriteSheets :+ DrawableStorage.spriteSheet(pure._2(x)._1, pure._2(x)._2, pure._2(x)._3)
    }
    (edit.toList, spriteSheets)
  }


  val testPure: LevelDataPure = {
    var pure = Array.ofDim[List[(Int,Int)]](2,2)
    var sprites = List(("src/resources/Tileset/basictiles.png", Vector2(128,240), Vector2(16)))
    pure(0)(0) = List((0, 0))
    pure(0)(1) = List((0, 2))
    pure(1)(0) = List((0, 4), (0, 6))
    pure(1)(1) = List((0, 1))
    (pure, sprites)
  }

//  val testDraw: LevelDataPure = ???

}

