object Component {
  trait Component {
    def update(deltaTime: Long, world: World): Unit = ()
  }

  trait ComponentKey[C <: Component] {
    implicit val key: ComponentKey[C] = this
  }

  case class HPComponent(hp: Int) extends Component

  case object HPComponent extends ComponentKey[HPComponent]
}

