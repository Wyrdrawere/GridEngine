package util

case class Vector2(x: Float, y: Float) { //todo: refactor everything to use this
  def +(that: Vector2): Vector2 = Vector2(this.x+that.x, this.y+that.y)
  def *(that: Float): Vector2 = Vector2(this.x*that, this.y*that)
  def -(that: Vector2): Vector2 = this + (that * -1)
  def /(that: Float): Vector2 = Vector2(this.x/that, this.y/that)

  def *:(that: Vector2): Vector2 = Vector2(this.x*that.x, this.y*that.y) //componentwise multiplication. does this have name/symbol?
  def /:(that: Vector2): Vector2 = Vector2(that.x/this.x, that.y/this.y) // this./:(that) = that /: this, for some reason
  def %:(that: Vector2): Vector2 = Vector2(that.x%this.x, that.y%this.y)

  def map(f: Float => Float): Vector2 = Vector2(f(this.x), f(this.y))

  def xi: Int = x.toInt
  def yi: Int = y.toInt
  def abs: Vector2 = Vector2(this.x.abs, this.y.abs)
  def value: Float = Math.sqrt(this.x*this.x + this.y+this.y).toFloat
  def normalized: Vector2 = this/this.value
  def clamp(min: Vector2, max: Vector2): Vector2 = {
    val clampX = if (this.x < min.x) min.x else if (this.x > max.x) max.x else this.x
    val clampY = if (this.y < min.y) min.y else if (this.y > max.y) max.y else this.y
    Vector2(clampX, clampY)
  }

  //todo: make all the things used to manipulate these into methods OR figure out implicitly translating tuples to Vector2s
}

object Vector2 {
  def apply(f: Float): Vector2 = Vector2(f,f)

  def apply(xd: Double, yd: Double): Vector2 = Vector2(xd.toFloat, yd.toFloat)

  val Up = Vector2(0,1)
  val Down = Vector2(0,-1)
  val Left = Vector2(-1,0)
  val Right = Vector2(1,0)
}