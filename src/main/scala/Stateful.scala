trait Stateful { //todo: think about state abstract class wich holds a list of statefuls. Also make stateful/mutation a thing again
  def simulate(deltaTime: Long, input: Input): Stateful
  def render(grid: Grid): Unit
}
