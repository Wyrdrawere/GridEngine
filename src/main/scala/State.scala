trait State {
  def simulate(): State
  def render(): Unit
}
