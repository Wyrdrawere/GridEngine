object LevelFormat {

  type LevelDataPure = (Array[Array[List[(Int, Int)]]], List[(String, Vector2, Vector2)])

  type LevelDataDraw = Array[Array[Tile]]

  type LevelDataEdit = (List[Array[Array[(Int, Int)]]], List[Map[Int, Drawable]])

  def pureToDraw(pure: LevelDataPure): LevelDataDraw = {
    var draw = Array.ofDim[Tile](pure._1.length, pure._1(0).length)
    var spriteSheets = Map.empty[Int, Map[Int, Sprite]]
    for (x <- 0 until pure._2.length) {
      spriteSheets = spriteSheets.updated(x, DrawableStorage.spriteSheet(pure._2(x)._1, pure._2(x)._2, pure._2(x)._3))
    }
    for (x <- draw.indices; y <- draw(x).indices) {
      draw(x)(y) = Tile(for (a <- pure._1(x)(y)) yield spriteSheets(a._1)(a._2))
    }
    draw
  }
}

