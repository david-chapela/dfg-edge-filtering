package gal.usc.citius.processmining.dfgfiltering.filtering

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraph

/**
 * Given a connected digraph with one source and one sink vertices, and where all vertices are in, at least, a path from
 * root to sink (i.e., a Directly Follows Graph), obtains an approximation to the maximally filtered directly follows
 * graph (MF-DFG) maximizing (or minimizing) the total weight of the DFG by removing the edges one by one, and ensuring
 * that all vertices remain in a path from source to sink.
 *
 * @param minimum if set to true search for the MF-DFG with the lowest total weight.
 *
 * @return an approximation to the MF-DFG problem with a maximum total weight (minimum if [minimum] is true).
 */
fun DirectedGraph.filterEdgesGreedy(minimum: Boolean = false): DirectedGraph {
    this.checkDFGCorrectness()
    val digraph = this.removeSelfCycles()

    var mwmfdfg = digraph
    val root = mwmfdfg.vertices.first { vertex -> vertex !in mwmfdfg.edges.map { edge -> edge.to } }
    val sink = mwmfdfg.vertices.first { vertex -> vertex !in mwmfdfg.edges.map { edge -> edge.from } }
    // Order the edges based in the frequency and remove one by one if
    // the removal maintains all vertices in a path from start to end
    digraph.edges
        .let { edges ->
            if (minimum) edges.sortedByDescending { it.weight } else edges.sortedBy { it.weight }
        }.forEach { edge ->
            // If source and sink have (respectively) only one out/in edge, its deletion will surely lead to unsoundness
            if (mwmfdfg.edges.filter { it.from == edge.from }.count() > 1 &&
                mwmfdfg.edges.filter { it.to == edge.to }.count() > 1
            ) {
                // Create graph without this edge
                val filteredDFG = mwmfdfg.copy(edges = mwmfdfg.edges - edge)
                // If all vertices are in a path from start to end use this graph for next iteration
                if (filteredDFG.getReachableVerticesFrom(root) == (filteredDFG.vertices - root) &&
                    filteredDFG.reverse().getReachableVerticesFrom(sink) == (filteredDFG.vertices - sink)
                ) {
                    mwmfdfg = filteredDFG
                }
            }
        }

    return mwmfdfg
}
