package gal.usc.citius.processmining.dfgfiltering.filtering

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraph

/**
 * Check the properties of the graph to ensure it is valid to calculate the MEG and remove self-cycles.
 *
 * @throws RuntimeException if the current graph violates a DFG correctness measure.
 */
internal fun DirectedGraph.checkDFGCorrectness(dfgName: String = "DFG") {
    if (!this.isConnected()) {
        throw RuntimeException("The $dfgName is not connected.")
    }
    if (this.edges.map { it.to }.toSet().size != (this.vertices.size - 1)) {
        throw RuntimeException("The $dfgName has more than one start vertex.")
    }
    if (this.edges.map { it.from }.toSet().size != (this.vertices.size - 1)) {
        throw RuntimeException("The $dfgName has more than one end vertex.")
    }
    val root = this.vertices.first { vertex -> vertex !in this.edges.map { edge -> edge.to } }
    val sink = this.vertices.first { vertex -> vertex !in this.edges.map { edge -> edge.from } }
    if (this.getReachableVerticesFrom(root) != (this.vertices - root) ||
        this.reverse().getReachableVerticesFrom(sink) != (this.vertices - sink)
    ) {
        throw RuntimeException("The $dfgName is not sound")
    }
}

/**
 * Check if the current graph is a correct filtered DFG of [completeDFG].
 *
 * @param completeDFG the complete DFG.
 * @throws RuntimeException if the current graph is not a valid F-DFG of [completeDFG].
 */
fun DirectedGraph.isValidFDFG(completeDFG: DirectedGraph) {
    // Check the filtered DFG is correct
    this.checkDFGCorrectness("filtered DFG")
    // Check if it is spanning
    if (this.vertices != completeDFG.vertices) {
        throw RuntimeException("The number of vertices in the filtered DFG (${this.vertices.size}) is different than in the complete DFG (${completeDFG.vertices.size}). ")
    }
    // Check if there are no new edges
    if (this.edges.any { it !in completeDFG.edges }) {
        throw RuntimeException("The filtered DFG has edges not present in the complete DFG")
    }
}
