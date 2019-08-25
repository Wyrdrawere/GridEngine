package components

import engine.{Component, ComponentKey}
import render.Drawable

case class Background(data: Array[Array[Int]], drawInfo: Map[Int, Drawable], blank: Int) extends Component

case object Background extends ComponentKey[Background]
