package components

import engine.ComponentKey


final case class Health(
  value: Long,
)

case object Health extends ComponentKey[Health]
