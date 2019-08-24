package deprecate

import drawables.Sprite
import render.Drawable
import util.Vector2

case class Level
(
  level: Array[Array[Int]],
  tileSet: Map[Int, Drawable],
  blankTile: Int
) {
  def getSlice(size: Vector2, centerPos: Vector2): Array[Array[Int]] = {
    val slice = Array.ofDim[Int](size.xi, size.yi)
    for (x <- slice.indices; y <- slice(x).indices) {
      val levelX = centerPos.xi - (size.xi / 2) + x
      val levelY = centerPos.yi - (size.yi / 2) + y
      if (level.indices.contains(levelX) && level(0).indices.contains(levelY)) {
        slice(x)(y) = level(levelX)(levelY)
      } else {
        slice(x)(y) = blankTile
      }
    }
    slice
  }

  def getDrawableSlice(size: Vector2, centerPos: Vector2): Array[Array[Drawable]] =
    getSlice(size, centerPos).map(x => x.map(y => tileSet(y)))
}

object Level {

  def RandomLevel(size: Vector2, tileSet: Map[Int, Drawable], blank: Int): Level = {
    val level = Array.ofDim[Int](size.xi, size.yi)
    for (x <- level.indices; y <- level(x).indices) {
      level(x)(y) = (Math.random() * tileSet.size).toInt
    }
    Level(level, tileSet, blank)
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
    Level(level, Sprite.get("src/resources/Tileset/basictiles.png", Vector2(128,240), Vector2(16)), 22)
  }

}
