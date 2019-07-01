import Config.LevelData

class LevelFormat(levelData: LevelData, tileSets: List[Map[Int,Drawable]], blankTile: (Int,Int)) {

  def render(grid: Grid): Unit =

  def getSlice(xSize: Int, ySize: Int, centerPos: (Int, Int)): LevelData = {
    val slice = Array.ofDim[List[(Int,Int)]](xSize, ySize)
    for (x <- slice.indices; y <- slice(x).indices) {
      val levelX = centerPos._1 - (xSize / 2) + x
      val levelY = centerPos._2 - (xSize / 2) + y
      if (levelData.indices.contains(levelX) && levelData(0).indices.contains(levelY)) {
        slice(x)(y) = levelData(levelX)(levelY)
      } else {
        slice(x)(y) = List(blankTile)
      }
    }
    slice
  }


}
