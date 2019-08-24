import util.Vector2

object Input {

  var cursor: Vector2 = Vector2(0)

  private var activeInput: Set[InputItem] = Set.empty
  private var releasedInput: Set[InputItem] = Set.empty
  private var delayedInput: Map[InputItem, Long] = Map.empty

  def update(deltaTime: Long): Unit = {
    releasedInput = Set.empty
    delayedInput = delayedInput.map{case (i,t:Long) => (i, t-deltaTime)}.filter{case (_,t) => t>0}
  }

  def add(input: InputItem): Unit = {
    activeInput = activeInput + input
  }

  def remove(input: InputItem): Unit = {
    activeInput = activeInput - input
    releasedInput = releasedInput + input
  }

  def delay(input: InputItem, time: Long): Unit = {
    delayedInput = delayedInput.updated(input, time)
  }

  def isActive(input: InputItem): Boolean = {
    activeInput.contains(input) && !delayedInput.contains(input)
  }

  def isReleased(input: InputItem): Boolean = {
    releasedInput.contains(input)
  }
}
