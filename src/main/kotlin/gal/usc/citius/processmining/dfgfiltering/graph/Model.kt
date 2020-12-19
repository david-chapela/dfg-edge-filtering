package gal.usc.citius.processmining.dfgfiltering.graph

import java.util.LinkedList
import java.util.Queue

/**
 * A directed graph formed by a set of vertices ([vertices]) and a list of directed edges ([edges]).
 *
 * @property vertices set of vertices of the graph.
 * @property edges list of edges of the graph.
 */
data class DirectedGraph(
    val vertices: Set<Vertex>,
    val edges: List<Edge>
) {

    operator fun contains(vertex: Vertex): Boolean = vertex in this.vertices
    operator fun contains(edge: Edge): Boolean = edge in this.edges

    /**
     * Get the set of vertices with an outgoing edge to [vertex].
     *
     * @return a set with the of vertices with an outgoing edge to [vertex].
     */
    fun predecessors(vertex: Vertex): Set<Vertex> = this.edges.filter { it.to == vertex }.map { it.from }.toSet()

    /**
     * Get the set of vertices with an incoming edge from [vertex].
     *
     * @return a set with the of vertices with an incoming edge from [vertex].
     */
    fun successors(vertex: Vertex): Set<Vertex> = this.edges.filter { it.from == vertex }.map { it.to }.toSet()

    /**
     * Get the set of vertices with an outgoing edge to the vertex with [id] as id.
     *
     * @return a set with the of vertices with an outgoing edge to the vertex with [id] as id.
     */
    fun predecessors(id: String): Set<Vertex> = this.predecessors(this.vertices.first { it.id == id })

    /**
     * Get the set of vertices with an incoming edge from the vertex with [id] as id.
     *
     * @return a set with the of vertices with an incoming edge from the vertex with [id] as id.
     */
    fun successors(id: String): Set<Vertex> = this.successors(this.vertices.first { it.id == id })

    /**
     * Check if the graph is connected, i.e., for all two vertices u and v in the graph, there is a weak path (a path
     * with edges in any direction) from u to v.
     *
     * @return true if all the vertices of this graph are weakly connected between them.
     */
    fun isConnected(): Boolean =
        this.getSubgraphConnectedTo(this.vertices.first()).vertices == this.vertices

    /**
     * Invert the weights of all the edges in this graph.
     *
     * @return an instance of this graph with the weights of all edges inverted.
     */
    fun invertWeights(): DirectedGraph =
        this.copy(
            edges = this.edges.map { edge ->
                edge.copy(weight = -edge.weight)
            }
        )

    /**
     * Get the graph result of reverting all the direction of the edges in the current graph.
     *
     * @return an instance of this graph with the direction of all edges reverted.
     */
    fun reverse(): DirectedGraph =
        this.copy(
            edges = this.edges.map { edge ->
                edge.copy(
                    from = edge.to,
                    to = edge.from
                )
            }
        )

    /**
     * Get the connected subgraph which vertices and edges are reachable (backward and forward) from [start], i.e., the
     * connected component of the graph containing [start].
     *
     * @param start the vertex to which all the elements in the returned subgraph are connected.
     *
     * @return the connected component of this graph containing [start].
     */
    fun getSubgraphConnectedTo(start: Vertex): DirectedGraph {
        val visitedVertices = mutableSetOf<Vertex>()
        // Create FIFO to store the vertices to explore
        val fifo: Queue<Vertex> = LinkedList()
        fifo.add(start)
        // Breadth-first search
        while (fifo.isNotEmpty()) {
            val vertex = fifo.remove()
            visitedVertices.add(vertex)
            // Expand forward
            fifo += this.edges
                .filter { it.from == vertex }
                .map { it.to }
                .filter { it !in visitedVertices }
            // Expand backward
            fifo += this.edges
                .filter { it.to == vertex }
                .map { it.from }
                .filter { it !in visitedVertices }
        }

        return DirectedGraph(
            vertices = visitedVertices,
            edges = this.edges.filter { it.from in visitedVertices && it.to in visitedVertices }
        )
    }

    /**
     * Get the set of vertices reachable from [start].
     *
     * @param start the vertex which all the elements in the returned subgraph must have a directed path from.
     *
     * @return the set of vertices with a directed path from [start].
     */
    fun getReachableVerticesFrom(start: Vertex): Set<Vertex> {
        val visitedVertices = mutableSetOf<Vertex>()
        // Create FIFO to store the vertices to explore
        val fifo: Queue<Vertex> = LinkedList()
        fifo.add(start)
        // Breadth-first search
        while (fifo.isNotEmpty()) {
            val vertex = fifo.remove()
            // Expand forward
            fifo += this.edges
                .filter { it.from == vertex }
                .map { it.to }
                .filter { it !in visitedVertices }
                .also { visitedVertices.addAll(it) }
        }
        // Return the reached vertices
        return visitedVertices
    }

    /**
     * Remove the self-cycles from the current graph.
     *
     * @return an instance of the current graph without the self cycles.
     */
    fun removeSelfCycles(): DirectedGraph = this.copy(
        edges = this.edges.filter { edge ->
            edge.from != edge.to
        }
    )
}

data class Vertex constructor(val id: String, val name: String = id)
data class Edge constructor(val from: Vertex, val to: Vertex, val weight: Double = 1.0)
