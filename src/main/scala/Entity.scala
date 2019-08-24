import Component.{Component, ComponentKey}

trait Entity {

  private var components: Map[ComponentKey[_], Component] = Map.empty

  //def components: Map[ComponentKey[_], Component] = csm //todo: figure out csm-renaming exception

  def attach[C <: Component](component: C)(implicit componentKey: ComponentKey[C]): Unit = if (this.has(componentKey)) {
    throw new Exception("Component already exists on this Entity.")
  } else {
    components = components.updated(componentKey, component)
  }

  def detach[C <: Component](componentKey: ComponentKey[C]): Unit = {
    components = components.removed(componentKey)
  }

  def has[C <: Component](componentKey: ComponentKey[C]): Boolean = components.contains(componentKey)

  def get[C <: Component](componentKey: ComponentKey[C]): Option[C] = if (this.has(componentKey)) {
    Some(components(componentKey).asInstanceOf[C])
  } else None

  def modify[C <: Component](f: C => C)(implicit componentKey: ComponentKey[C]): Unit = if (this.has(componentKey)) {
    components = components.updated(componentKey, f(components(componentKey).asInstanceOf[C]))
  } else {
    throw new Exception("Component to modify does not exist.")
  }

  def update(deltaTime: Long, world: World): Unit = {
    for (c <- components) {c._2.update(deltaTime, world)}
  }

}