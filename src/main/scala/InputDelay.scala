case class InputDelay(keys: Map[InputKey, Long]) {

  //todo: belongs in stateful basetrait, instead of implementations

  def add(key: InputKey, cooldown: Long): InputDelay = this.copy(keys.updated(key, cooldown))

  def cooldown(time: Long): InputDelay = {
    this.copy(keys = keys.map{case (k: InputKey,c: Long) => (k, c-time)}.filter{case (k: InputKey,c: Long) => c > 0})
  }

  def keyActive(key: InputKey): Boolean = keys.get(key) match {
    case Some(k) => false
    case None => true
  }

}
