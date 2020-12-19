package gal.usc.citius.processmining.dfgfiltering.output

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraph
import java.io.File

/**
 * Write the current directed graph to a file in custom TGF format:
 *
 *    node_id node_name
 *    node_id node_name
 *    node_id node_name
 *    #
 *    source_id target_id edge_weight
 *    source_id target_id edge_weight
 *    source_id target_id edge_weight
 */
fun DirectedGraph.writeToFile(outputFile: File) {
    outputFile.printWriter().use { out ->
        // Map vertices to int ids
        val vertexToInt = this.vertices.mapIndexed { i, vertex -> vertex to i }.toMap()

        this.vertices.forEach { vertex ->
            out.println("${vertexToInt[vertex]} ${vertex.name}")
        }
        out.println("#")
        this.edges.forEach { (from, to, weight) ->
            out.println("${vertexToInt[from]} ${vertexToInt[to]} $weight")
        }
    }
}
