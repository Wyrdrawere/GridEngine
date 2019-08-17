object Main extends App {

  val overworldInit: () => Stateful = () => new Overworld(
    Statebox.OverworldBox(
      Level.TestDungeon,
      OverworldSprite.FF1_PlayerSprite(0),
      Vector2(0),
      5,
      Scroller(Config.scrollUnit, Vector2(0), Scroller.Stay),
      Sound.load("src/resources/Sound/step.ogg")
    ),
    new Grid)

  val window: Window = new Window(overworldInit)
  window.run()

}
