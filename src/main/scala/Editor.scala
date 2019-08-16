class Editor
(
  override val box: Statebox.EditorBox,
  override val grid: Grid,
  override val childState: Option[Stateful],
  override val returnMutation: Mutation,
  override val inputDelay: InputDelay = InputDelay(Map.empty)
)
  extends Stateful {


  override def copy
  (box: Statebox = box,
   grid: Grid = grid,
   childState: Option[Stateful] = childState,
   returnMutation: Mutation = returnMutation,
   inputDelay: InputDelay = inputDelay): Stateful = new Editor(
    box.asInstanceOf[Statebox.EditorBox], grid, childState, returnMutation, inputDelay
  )

  override protected def mutate: Receive = ???

  override protected def draw(grid: Grid): Unit = ???
}
