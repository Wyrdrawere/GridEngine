package components

import engine.{Component, ComponentKey, Drawable, Event}

case class PanelMenu(panels: Array[Array[(Drawable, Event)]]) extends Component

case object PanelMenu extends ComponentKey[PanelMenu]
