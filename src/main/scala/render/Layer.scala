package render

sealed trait Layer {
  val position: Double
}

object Layer {

  //todo: opengl immediate mode makes this impossible. Moving to modern opengl neccessary at some point

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
