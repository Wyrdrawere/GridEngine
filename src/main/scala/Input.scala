case class Input
(
  cursorPosition: Vector2,
  pressedButton: Set[InputMouseButton],
  heldButton: Set[InputMouseButton],
  releasedButton: Set[InputMouseButton],
  pressedKey: Set[InputKey],
  heldKey: Set[InputKey],
  releasedKey: Set[InputKey],
)

object Input {
  def update: Input = {
    updateKeys()
    updateMouse()
    input
  }

  private var input = Input(Vector2(0), Set.empty, Set.empty, Set.empty, Set.empty, Set.empty, Set.empty)

  private var cursorPosition: Vector2 = Vector2(0)
  private var frameAddedKeys: Set[InputKey] = Set.empty
  private var frameRemovedKeys: Set[InputKey] = Set.empty
  private var frameAddedButtons: Set[InputMouseButton] = Set.empty
  private var frameRemovedButtons: Set[InputMouseButton] = Set.empty

  def moveCursor(newPosition: Vector2): Unit = cursorPosition = newPosition

  def addButton(button: InputMouseButton): Unit = {
    frameAddedButtons = frameAddedButtons + button
  }

  def removeButton(button: InputMouseButton): Unit = {
    frameRemovedButtons = frameRemovedButtons + button
  }

  def addKey(key: InputKey): Unit = {
    frameAddedKeys = frameAddedKeys + key
  }

  def removeKey(key: InputKey): Unit = {
    frameRemovedKeys = frameRemovedKeys + key
  }

  private def updateKeys(): Unit = {

    var pressed: Set[InputKey] = frameAddedKeys
    var held: Set[InputKey] = input.pressedKey ++ input.heldKey
    var released: Set[InputKey] = frameRemovedKeys

    for (k <- released) {
      pressed = pressed - k
      held = held - k
    }

    for(k <- held) {
      pressed = pressed - k
    }

    frameAddedKeys = Set.empty
    frameRemovedKeys = Set.empty
    input = input.copy(pressedKey = pressed, heldKey = held, releasedKey = released)
  }

  private def updateMouse(): Unit = {

    var pressed: Set[InputMouseButton] = frameAddedButtons
    var held: Set[InputMouseButton] = input.pressedButton ++ input.heldButton
    var released: Set[InputMouseButton] = frameRemovedButtons

    for (b <- released) {
      pressed = pressed - b
      held = held - b
    }

    for(b <- held) {
      pressed = pressed - b
    }

    frameAddedButtons = Set.empty
    frameRemovedButtons = Set.empty
    input = input.copy(cursorPosition = cursorPosition, pressedButton = pressed, heldButton = held, releasedButton = released)
  }
}