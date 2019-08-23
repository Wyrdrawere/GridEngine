import Component.{Component, ComponentKey}

trait Entity {

  private var csm: Map[ComponentKey[_], Component] = Map.empty

  //def components: Map[ComponentKey[_], Component] = csm //todo: figure out csm-renaming exception

  def attach[C <: Component](component: C)(implicit componentKey: ComponentKey[C]): Unit = if (this.has(componentKey)) {
    throw new Exception("Component already exists on this Entity.")
  } else {
    csm = csm.updated(componentKey, component)
  }

  def detach[C <: Component](componentKey: ComponentKey[C]): Unit = {
    csm = csm.removed(componentKey)
  }

  def has[C <: Component](componentKey: ComponentKey[C]): Boolean = csm.contains(componentKey)

  def get[C <: Component](componentKey: ComponentKey[C]): Option[C] = if (this.has(componentKey)) {
    Some(csm(componentKey).asInstanceOf[C])
  } else None

  def modify[C <: Component](f: C => C)(implicit componentKey: ComponentKey[C]): Unit = if (this.has(componentKey)) {
    csm = csm.updated(componentKey, f(csm(componentKey).asInstanceOf[C]))
  } else {
    throw new Exception("Component to modify does not exist.")
  }

}