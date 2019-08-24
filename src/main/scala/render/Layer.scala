package render

sealed trait Layer {
  val position: Double
}

object Layer {

  object Background extends Layer {
    override val position: Double = 1
  }

  object Foreground extends Layer {
    override val position: Double = -1
  }

  object CenterPlane extends Layer {
    override val position: Double = 0
  }

}
