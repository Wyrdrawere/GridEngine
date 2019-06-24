case class Sprite(id: Int, minX: Float, maxX: Float, minY: Float, maxY: Float) extends Drawable

object Sprite {
  def TextureToTileSet(texture: Texture, width: Int, height: Int, tileWidth: Int, tileHeight: Int): Map[Int, Sprite] = {
    var xAmount = width/tileWidth
    var yAmount = height/tileHeight
    var spriteMap: Map[Int, Sprite] = Map.empty
    for(x <- 0 until xAmount; y <- 0 until yAmount) {
      spriteMap = spriteMap.updated(
        y*xAmount+x,
        Sprite(
          texture.id,
          x.toFloat/xAmount,
          (x+1).toFloat/xAmount,
          y.toFloat/yAmount,
          (y+1).toFloat/yAmount,
        ))
    }
    spriteMap
  }
}