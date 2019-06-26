case class Vector2(x: Float, y: Float) { //todo: refactor everything to use this
  def +(that: Vector2): Vector2 = Vector2(this.x+that.x, this.y+that.y)
  def *(that: Float): Vector2 = Vector2(this.x*that, this.y*that)
  def -(that: Vector2): Vector2 = this + (that * -1)
  def /(that: Float): Vector2 = Vector2(this.x/that, this.y/that)
  def *:(that: Vector2): Vector2 = Vector2(this.x*that.x, this.y*that.y) //componentwise multiplication. does this have name/symbol?
  def /:(that: Vector2): Vector2 = Vector2(that.x/this.x, that.y/this.y) //todo: find out why this seems to be reverse

  def xi: Int = x.toInt
  def yi: Int = y.toInt
}
