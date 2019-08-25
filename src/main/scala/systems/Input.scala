package systems

import engine.Event.{KeyPressed, KeyReleased}
import engine.{System, World, State}
import util.InputItem

object Input extends System {

  private var activeInput: Set[InputItem] = Set.empty
  private var releasedInput: Set[InputItem] = Set.empty
  private var delayedInput: Map[InputItem, Long] = Map.empty

  override def update(newWorld: World, state: State, deltaTime: Long): Unit = {
    for(i <- activeInput) if(!delayedInput.contains(i)) newWorld.emit(KeyPressed(i))
    for(i <- releasedInput) newWorld.emit(KeyReleased(i))
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

  def delay(input: InputItem, time: Long)(newWorld: World): Unit = {
    delayedInput = delayedInput.updated(input, time)
  }

  def isActive(input: InputItem): Boolean = {
    !delayedInput.contains(input)
  }
}
