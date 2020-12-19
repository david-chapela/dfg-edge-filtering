package gal.usc.citius.processmining.dfgfiltering.input

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraph
import gal.usc.citius.processmining.dfgfiltering.graph.Edge
import gal.usc.citius.processmining.dfgfiltering.graph.Vertex
import java.io.File

/**
 * Read a directed graph from a file in custom TGF format:
 *
 *    node_id node_name
 *    node_id node_name
 *    node_id node_name
 *    #
 *    source_id target_id edge_weight
 *    source_id target_id edge_weight
 *    source_id target_id edge_weight
 *
 * @return an instance of [DirectedGraph] with the read graph.
 */
fun File.readDirectedGraph(): DirectedGraph {
    val vertexMap = mutableMapOf<String, Vertex>()
    val edges = mutableListOf<Edge>()

    var state = 0
    this.readLines().forEach { line ->
        if (line.trim().isNotEmpty()) {
            when (state) {
                0 ->
                    // Reading vertices
                    if (line.trim() == "#") {
                        // Finished reading vertices
                        state = 1
                    } else {
                        // Read vertex
                        val elements = line.trim().split(" ", limit = 2).toTypedArray()
                        vertexMap[elements[0]] = Vertex(elements[1], elements[1])
                    }
                1 -> {
                    // Reading edges
                    val elements = line.trim().split(" ").toTypedArray()
                    edges.add(
                        Edge(
                            from = vertexMap[elements[0]]!!,
                            to = vertexMap[elements[1]]!!,
                            weight = elements[2].toDouble()
                        )
                    )
                }
            }
        }
    }

    return DirectedGraph(vertexMap.values.toSet(), edges.sortedBy { it.to.id }.sortedBy { it.from.id })
}
