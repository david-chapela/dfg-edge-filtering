package gal.usc.citius.processmining.dfgfiltering.filtering

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraph
import gal.usc.citius.processmining.dfgfiltering.graph.Edge
import gal.usc.citius.processmining.dfgfiltering.graph.Vertex
import java.util.Deque
import java.util.LinkedList

/**
 * Given a connected digraph with one source and one sink vertices, and where all vertices are in, at least, a path from
 * root to sink (i.e., a Directly Follows Graph), obtains an approximation to the maximally filtered directly follows
 * graph (MF-DFG) maximizing (or minimizing) the total weight of the DFG by combining two arborescence with maximum
 * (or minimum weight) using Edmonds' Algorithm.
 *
 * @param minimum if set to true search for the minimum equivalent graph with the lowest overall weight.
 *
 * @return an approximation to the MF-DFG problem with a maximum total weight (minimum if [minimum] is true).
 */
fun DirectedGraph.filterEdgesTWE(minimum: Boolean = false): DirectedGraph {
    this.checkDFGCorrectness()
    val digraph = this.removeSelfCycles()
        .let { if (minimum) it.invertWeights() else it }

    // Call Edmond's algorithm forward
    val root = digraph.vertices.first { vertex ->
        vertex !in this.edges.map { it.to }
    }
    val fwMSA = digraph.getSpanningArborescence(root)

    // Call Edmond's algorithm backwards
    val sink = digraph.vertices.first { vertex ->
        vertex !in this.edges.map { it.from }
    }
    val bwMSA = digraph.reverse().getSpanningArborescence(sink).reverse()

    // Merge
    val meg = DirectedGraph(
        vertices = digraph.vertices,
        edges = fwMSA.edges + bwMSA.edges
    )

    return if (minimum) meg.invertWeights() else meg
}

/**
 * Edmonds' Algorithm: get the spanning arborescence with the maximum weight (the sum of all edge weights). An
 * arborescence is a directed graph in which, for a vertex u called root and any other vertex v, there is exactly one
 * directed path from u to v. An arborescence is thus the directed-graph form of a rooted tree.
 *
 * @param root vertex from the graph to be the root of the arborescence.
 * @param minimum if set to true search for the minimum spanning arborescence with the lowest overall weight.
 *
 * @return the spanning arborescence rooted in [root] and with a maximum total weight (minimum if [minimum] is set to
 * true).
 */
fun DirectedGraph.getSpanningArborescence(root: Vertex, minimum: Boolean = false): DirectedGraph {
    var arborescence: DirectedGraph
    // If we search for the minimum invert the edge weights
    var digraph = if (minimum) this.invertWeights() else this

    val cyclesLIFO: Deque<Pair<Vertex, DirectedGraph>> = LinkedList()
    val edgesHistoryLIFO: Deque<MutableMap<Edge, Edge>> = LinkedList()
    var cycleCounter = 0
    // Iterative arborescence construction until no cycles remaining
    do {
        // Get, for each vertex except the root, the incoming edge with more weight
        arborescence = digraph.copy(
            edges = digraph.vertices
                .filter { it != root }
                .mapNotNull { currentVertex ->
                    digraph.edges
                        .filter { it.to == currentVertex }
                        .maxByOrNull { it.weight }
                }
        )

        // Detect cycles
        val cycles = arborescence.getArborescenceCycles()

        // If there is any cycle, copy the current digraph collapsing the cycles into
        // single vertices, and updating the weights of the edges entering the cycle
        cycles.forEach { cycle ->
            // Vertex to collapse the cycle
            val collapsedCycle = Vertex("cycle${cycleCounter++}")
            // Save the cycle to re-expand it later
            cyclesLIFO.push(collapsedCycle to cycle)
            // Edges renaming history for this cycle
            val edgesHistory = mutableMapOf<Edge, Edge>()
            // Edge inside the cycle with minimum weight
            val minEdge = cycle.edges.minByOrNull { it.weight }!!
            // Update digraph collapsing the cycle
            digraph = DirectedGraph(
                vertices = digraph.vertices - cycle.vertices + collapsedCycle,
                edges = digraph.edges.mapNotNull { edge ->
                    when {
                        edge.from !in cycle.vertices && edge.to !in cycle.vertices -> {
                            // Edge outside the cycle
                            edge
                        }
                        edge.from !in cycle.vertices && edge.to in cycle.vertices -> {
                            // Edge entering the cycle
                            val newEdge = edge.copy(
                                to = collapsedCycle,
                                weight = edge.weight + minEdge.weight - cycle.edges.find { it.to == edge.to }!!.weight
                            )
                            edgesHistory += newEdge to edge
                            newEdge
                        }
                        edge.from in cycle.vertices && edge.to !in cycle.vertices -> {
                            // Edge leaving the cycle
                            val newEdge = edge.copy(
                                from = collapsedCycle
                            )
                            edgesHistory += newEdge to edge
                            newEdge
                        }
                        else -> {
                            // Edge inside the cycle
                            null
                        }
                    }
                }
            )
            // Save this edge renaming history
            edgesHistoryLIFO.push(edgesHistory)
        }
    } while (cycles.isNotEmpty())

    // Re-expand the cycles
    while (cyclesLIFO.isNotEmpty()) {
        // Retrieve last collapsed cycle
        val (collapsedCycle, cycle) = cyclesLIFO.pop()
        // Retrieve edge renaming history for this cycle
        val edgesHistory = edgesHistoryLIFO.pop()
        // Update the edges in the arborescence going to/from the collapsed cycle
        val newEdges = arborescence.edges.map { edge ->
            when (edge) {
                in edgesHistory.keys -> edgesHistory[edge]!!
                else -> edge
            }
        }
        // Update the arborescence including the re-expanded cycle, and removing
        // the edge going to the same vertex as the new edge entering the cycle
        arborescence = arborescence.copy(
            vertices = arborescence.vertices - collapsedCycle + cycle.vertices,
            edges = newEdges + cycle.edges.filter { edge ->
                edge.to !in newEdges.map { it.to }
            }
        )
    }

    return if (minimum) arborescence.invertWeights() else arborescence
}

/**
 * Given an arborescence, a directed graph where each vertex contains only one incoming edge, except for the root which
 * contains no incoming edges, search the cycles, if any.
 *
 * For this, the vertices with no incoming or outgoing edges are removed iteratively, along with the edges connected to
 * them. Once there are no more vertices without incoming or outgoing edges, the remaining structures are the cycles, if
 * any.
 *
 * @return a set with the cycles in the arborescence, if any.
 */
private fun DirectedGraph.getArborescenceCycles(): Set<DirectedGraph> {
    var digraph = this.copy()

    // Remove iteratively the edges of those vertices with no incoming or outgoing edges
    do {
        val fullyConnectedVertices = digraph.edges.map { it.from }.intersect(digraph.edges.map { it.to })
        digraph = DirectedGraph(
            vertices = fullyConnectedVertices,
            edges = digraph.edges.filter { it.from in fullyConnectedVertices && it.to in fullyConnectedVertices }
        )
        // Check if there is still orphan vertices remaining
        val orphanRemaining = fullyConnectedVertices.any { vertex ->
            vertex !in digraph.edges.map { it.from } ||
                vertex !in digraph.edges.map { it.to }
        }
    } while (orphanRemaining)

    // Retrieve each connected component, i.e., each cycle
    val visitedVertices = mutableSetOf<Vertex>()
    val cycles = mutableSetOf<DirectedGraph>()
    while (visitedVertices != digraph.vertices) {
        // get one unvisited vertex and get the vertices reaching it
        val unvisitedVertex = digraph.vertices.first { it !in visitedVertices }
        val cycle = digraph.getSubgraphConnectedTo(unvisitedVertex)
        // mark all cycle vertices as visited
        visitedVertices += cycle.vertices
        // save the cycle
        cycles += cycle
    }

    return cycles
}
