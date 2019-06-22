class Level(levelData: Array[Array[Int]]) {

  // todo: better define level, make editor!

  def getSlice(xSize: Int, ySize: Int, centerPos: (Int, Int)): Array[Array[Int]] = {
    val slice = Array.ofDim[Int](xSize, ySize)
    for (x <- slice.indices; y <- slice(x).indices) {
      val levelX = centerPos._1 - (xSize / 2) + x
      val levelY = centerPos._2 - (xSize / 2) + y
      if (levelData.indices.contains(levelX) && levelData(0).indices.contains(levelY)) {
        slice(x)(y) = levelData(levelX)(levelY)
      } else {
        slice(x)(y) = 22 //todo: make blank Element adjustable. Possibly make tileSet part of Level
      }
    }
    slice
  }

}

object Level {
  val Test = Array(
    Array(1, 1, 1, 1, 1),
    Array(1, 3, 3, 3, 1),
    Array(1, 3, 3, 3, 1),
    Array(1, 3, 3, 3, 1),
    Array(1, 3, 3, 3, 1),
    Array(1, 4, 4, 4, 1),
    Array(1, 4, 4, 4, 1),
    Array(1, 4, 4, 4, 1),
    Array(1, 4, 4, 4, 1),
    Array(1, 1, 1, 1, 1),
  )

  def RandomLevel(xSize: Int, ySize: Int, max: Float): Level = {
    val level = Array.ofDim[Int](xSize, ySize)
    for (x <- level.indices; y <- level(x).indices) {
      level(x)(y) = (Math.random() * max).toInt
    }
    new Level(level)
  }

  val TestDungeon: Level = {
    val level = Array.ofDim[Int](21, 11)
    for (x <- level.indices; y <- level(x).indices) {
      if(y == level(x).length-1) {
        if(x % 2 == 0) {
          level(x)(y) = 0
        } else {
          level(x)(y) = 2
        }
      } else {
        level(x)(y) = 1
      }
    }
    new Level(level)
  }
}
