object Component {
  trait Component

  trait ComponentKey[C <: Component] {
    implicit val key: ComponentKey[C] = this
  }

  case class HPComponent(var hp: Int) extends Component

  case object HPComponent extends ComponentKey[HPComponent]
}

