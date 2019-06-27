case class Vector2(x: Float, y: Float) { //todo: refactor everything to use this
  def +(that: Vector2): Vector2 = Vector2(this.x+that.x, this.y+that.y)
  def *(that: Float): Vector2 = Vector2(this.x*that, this.y*that)
  def -(that: Vector2): Vector2 = this + (that * -1)
  def /(that: Float): Vector2 = Vector2(this.x/that, this.y/that)

  def *:(that: Vector2): Vector2 = Vector2(this.x*that.x, this.y*that.y) //componentwise multiplication. does this have name/symbol?
  def /:(that: Vector2): Vector2 = Vector2(that.x/this.x, that.y/this.y) // this./:(that) = that /: this, for some reason

  def map(f: Float => Float): Vector2 = Vector2(f(this.x), f(this.y))

  def xi: Int = x.toInt
  def yi: Int = y.toInt
}
