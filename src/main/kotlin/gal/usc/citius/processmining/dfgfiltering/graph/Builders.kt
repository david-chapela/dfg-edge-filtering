package gal.usc.citius.processmining.dfgfiltering.graph

class DirectedGraphBuilder {
    private val vertices: MutableSet<Vertex> = mutableSetOf()
    private val edges: MutableSet<DirectedEdgeBuilder> = mutableSetOf()

    fun vertex(id: String): DirectedGraphBuilder {
        this.vertices.add(Vertex(id))
        return this
    }

    fun edge(): DirectedEdgeBuilder {
        val builder = DirectedEdgeBuilder(this)
        this.edges.add(builder)
        return builder
    }

    fun build(): DirectedGraph = DirectedGraph(this.vertices, this.edges.map { it.build() })
}

class DirectedEdgeBuilder(val gb: DirectedGraphBuilder) {
    private lateinit var from: Vertex
    private lateinit var to: Vertex
    private var weight: Double = 1.0

    fun weight(weight: Double): DirectedEdgeBuilder {
        this.weight = weight
        return this
    }

    fun from(vertex: String): DirectedEdgeBuilder {
        this.from = Vertex(vertex)
        return this
    }

    fun to(vertex: String): DirectedGraphBuilder {
        this.to = Vertex(vertex)
        return this.gb
    }

    fun build(): Edge = Edge(from, to, weight)
}
