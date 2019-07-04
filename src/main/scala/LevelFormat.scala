object LevelFormat {

  type LevelDataPure = (Array[Array[List[(Int, Int)]]], List[(String, Vector2, Vector2)])

  type LevelDataDraw = Array[Array[Tile]]

  type LevelDataEdit = (List[Array[Array[(Int, Int)]]], List[Map[Int, Sprite]])

  def pureToDraw(pure: LevelDataPure): LevelDataDraw = {
    var spriteSheets = pure._2.map(x => DrawableStorage.spriteSheet(x._1, x._2, x._3))
    pure._1.map(x =>
      x.map(y =>
        Tile(y.map(z =>
          spriteSheets(z._1)(z._2)))))
  }

  val testPure: LevelDataPure = {
    var pure = Array.ofDim[List[(Int,Int)]](3,3)
    var sprites = List(("src/resources/Tileset/basictiles.png", Vector2(128,240), Vector2(16)))
    pure = pure.map(x => x.map(y => List((0,0))))
    pure(1)(1) = List((0,11), (0,56), (0,24), (0,29))
    (pure, sprites)
  }

//  val testDraw: LevelDataPure = ???

}

