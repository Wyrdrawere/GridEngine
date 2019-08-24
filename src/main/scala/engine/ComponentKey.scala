package engine

trait ComponentKey[C <: Component]
{
  implicit val key: ComponentKey[C] = this
}
