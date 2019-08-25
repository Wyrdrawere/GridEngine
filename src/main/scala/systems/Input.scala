package systems

import engine.Event.{KeyPressed, KeyReleased}
import engine.{System, World}
import util.InputItem

object Input extends System {

  private var activeInput: Set[InputItem] = Set.empty
  private var releasedInput: Set[InputItem] = Set.empty
  private var delayedInput: Map[InputItem, Long] = Map.empty

  override def update(world: World, deltaTime: Long): Unit = {
    for(i <- activeInput) if(!delayedInput.contains(i)) world.emit(KeyPressed(i))
    for(i <- releasedInput) world.emit(KeyReleased(i))
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

  def delay(input: InputItem, time: Long)(deltaTime: Long, world: World): Unit = {
    delayedInput = delayedInput.updated(input, time)
  }

  def isActive(input: InputItem): Boolean = {
    !delayedInput.contains(input)
  }
}
