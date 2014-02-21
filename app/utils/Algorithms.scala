package utils

import java.util.PriorityQueue
import java.lang.Comparable


object Algorithms {
  def f_dist(x: (Int, Int), target: (Int, Int)): Int = Math.max(Math.abs(x._1 - target._1), Math.abs(x._2 - target._2))

  class Vertex(val point: (Int, Int), val f_cost: Int, val f_dist: Int, val came_from: Option[Vertex]) extends Comparable[Vertex] {
    def f(): Int = f_cost + f_dist
    def compareTo(x: Vertex): Int = f - x.f
    def neighbors: List[(Int, Int)] =
      List((0,1), (1,0), (1,1), (0,-1), (-1,0), (-1,-1), (-1,1), (1,-1)) map { d => (d._1 + point._1, d._2 + point._2) }

    override def equals(obj: Any): Boolean = if (obj.isInstanceOf[Vertex]) {
      obj.asInstanceOf[Vertex].point == point
    } else false
  }

  object Vertex {
    def apply(x: (Int, Int), f_cost: Int, target: (Int, Int), came_from: Option[Vertex]): Vertex =
      new Vertex(x, f_cost, f_dist(x, target), came_from)
  }

  def astar(start: (Int, Int), goal: (Int, Int), passability: ((Int, Int)) => Boolean): List[(Int, Int)] = {
    var closedSet = Set[Vertex]()
    val startVertex = Vertex(start, 0, goal, None)

    val openSetPQ = new PriorityQueue[Vertex]()
    openSetPQ.add(startVertex)

    var openSet = Set(startVertex)

    while (!openSetPQ.isEmpty) {
      val x = openSetPQ.poll()
      openSet -= x
      if (x.point == goal) return buildPath(x)

      closedSet += x

      x.neighbors.filter(passability).filter(p => closedSet.find(_.point == p).isEmpty).foreach(y => {
        val new_f_cost = x.f_cost + 1

        val yVertex = openSet.find(_.point == y)
        if (yVertex.isEmpty || yVertex.get.f_cost > new_f_cost) {
          yVertex foreach { v =>
            openSetPQ.remove(v)
            openSet -= v
          }

          var newY = Vertex(y, new_f_cost, goal, Some(x))
          openSetPQ.add(newY)
          openSet += newY
        }
      })
    }

    // need to pass Ordering explicitly
    buildPath(closedSet.max[Vertex](new Ordering[Vertex] {
      def compare(x: Vertex, y: Vertex): Int = x.compareTo(y)
    }))
  }

  def buildPath(target: Vertex): List[(Int, Int)] = {
    def rec(t: Vertex): List[(Int, Int)] = t.came_from match {
      case Some(x) => x.point :: rec(x)
      case None => Nil
    }

    rec(target).reverse drop 1
  }

}
