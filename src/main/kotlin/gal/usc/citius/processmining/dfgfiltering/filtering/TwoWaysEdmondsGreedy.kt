package gal.usc.citius.processmining.dfgfiltering.filtering

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraph

/**
 * Given a connected digraph with one source and one sink vertices, and where all vertices are in, at least, a path from
 * root to sink (i.e., a Directly Follows Graph), obtains an approximation to the maximally filtered directly follows
 * graph (MF-DFG) maximizing (or minimizing) the total weight of the DFG by applying the Greedy technique to the result
 * of the Two Ways Edmonds technique.
 *
 * @param minimum if set to true search for the MF-DFG with the lowest total weight.
 *
 * @return an approximation to the MF-DFG problem with a maximum total weight (minimum if [minimum] is true).
 */
fun DirectedGraph.filterEdgesTWEG(minimum: Boolean = false): DirectedGraph =
    this.filterEdgesTWE(minimum).filterEdgesGreedy(minimum)
