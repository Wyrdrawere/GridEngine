object DrawableStorage {

  // todo: might both belong in respective companion

  def texture(path: String): Texture = {
    if (textures.keys.toList.contains(path)) {
      textures(path)
    } else {
      val t = Texture.load(path)
      textures = textures.updated(path, t)
      t
    }
  }

  def spriteSheet(path: String, size: Vector2, tileSize: Vector2): Map[Int, Sprite] = {
    if (spriteSheets.keys.toList.contains(path)) {
      println("loaded store: " + path)
      spriteSheets(path)
    } else {
      println("loaded fresh: " + path)
      val s = Sprite.TextureToTileSet(texture(path), size, tileSize)
      spriteSheets = spriteSheets.updated(path, s)
      s
    }
  }

  private var textures: Map[String, Texture] = Map.empty

  private var spriteSheets: Map[String, Map[Int, Sprite]] = Map.empty

}
