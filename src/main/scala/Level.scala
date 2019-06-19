object Level {
  type LevelData = Array[Array[Int]]

  val Test = Array(
    Array(1,1,1,1,1),
    Array(1,3,3,3,1),
    Array(1,3,3,3,1),
    Array(1,3,3,3,1),
    Array(1,3,3,3,1),
    Array(1,4,4,4,1),
    Array(1,4,4,4,1),
    Array(1,4,4,4,1),
    Array(1,4,4,4,1),
    Array(1,1,1,1,1),
  )

  def RandomLevel(xSize: Int, ySize: Int, max: Float): Array[Array[Int]] = {
    val level = Array.ofDim[Int](xSize, ySize)
    for (x <- level.indices; y <- level(x).indices) {
      level(x)(y) = (Math.random()*max).toInt
    }
    level
  }

  def getSlice(level: LevelData, xSize: Int, ySize: Int, centerPos: (Int, Int)): Array[Array[Int]] = {
    val slice = Array.ofDim[Int](xSize, ySize)
    for(x <- slice.indices; y <- slice(x).indices) {
      val levelX = centerPos._1-(xSize/2)+x
      val levelY = centerPos._2-(xSize/2)+y
      if (level.indices.contains(levelX) && level(0).indices.contains(levelY)) {
        slice(x)(y) = level(levelX)(levelY)
      } else {
        slice(x)(y) = 1
      }
    }
    slice
  }
}
