package engine

trait Entity {

  private var components: Map[ComponentKey[_], Component] = Map.empty

  def attach[C <: Component](component: C)(implicit componentKey: ComponentKey[C]): Unit = {
    if (this.has(componentKey)) {
      throw new Exception("Component already exists on this Entity.")
    }
    else {
      components = components.updated(componentKey, component)
    }
  }

  def detach[C <: Component](componentKey: ComponentKey[C]): Unit = {
    components = components.removed(componentKey)
  }

  def has[C <: Component](componentKey: ComponentKey[C]): Boolean =
    components.contains(componentKey)

  def get[C <: Component](componentKey: ComponentKey[C]): Option[C] =
    components.get(componentKey).map(_.asInstanceOf[C])

  def modify[C <: Component](f: C => C)(implicit componentKey: ComponentKey[C]): Unit = {
    if(components.contains(componentKey)) {
      components = components.updated(componentKey, f(components(componentKey).asInstanceOf[C]))
    }
  }
}
