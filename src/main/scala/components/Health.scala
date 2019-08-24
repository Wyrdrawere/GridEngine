package components

import engine.{Component, ComponentKey}


final case class Health(
  value: Long,
) extends Component

case object Health extends ComponentKey[Health]
