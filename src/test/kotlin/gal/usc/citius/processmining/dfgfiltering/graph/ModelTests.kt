package gal.usc.citius.processmining.dfgfiltering.graph

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ModelTests : Spek({

    describe("A connected directed graph with positive weights and no cycles") {
        val dg = DirectedGraphBuilder()
            .vertex("A")
            .vertex("B")
            .vertex("C")
            .vertex("D")
            .vertex("E")
            .vertex("F")
            .vertex("G")
            .vertex("H")
            .edge().weight(1.0).from("A").to("B")
            .edge().weight(4.1).from("A").to("C")
            .edge().weight(4.3).from("B").to("F")
            .edge().weight(1.7).from("C").to("D")
            .edge().weight(3.1).from("C").to("E")
            .edge().weight(1.1).from("F").to("D")
            .edge().weight(2.1).from("D").to("G")
            .edge().weight(1.5).from("E").to("G")
            .edge().weight(1.3).from("F").to("H")
            .build()

        it("should return true when checking if it contains a vertex inside the graph") {
            (Vertex("A") in dg).shouldBeTrue()
            (Vertex("F") in dg).shouldBeTrue()
            (Vertex("H") in dg).shouldBeTrue()
        }

        it("should return false when checking if it contains a vertex not in the graph") {
            (Vertex("X") in dg).shouldBeFalse()
            (Vertex("L") in dg).shouldBeFalse()
        }

        it("should return true when checking if it contains an edge inside the graph") {
            (Edge(Vertex("C"), Vertex("D"), 1.7) in dg).shouldBeTrue()
            (Edge(Vertex("D"), Vertex("G"), 2.1) in dg).shouldBeTrue()
            (Edge(Vertex("F"), Vertex("H"), 1.3) in dg).shouldBeTrue()
        }

        it("should return false when checking if it contains an edge not in the graph") {
            (Edge(Vertex("A"), Vertex("F")) in dg).shouldBeFalse()
            (Edge(Vertex("L"), Vertex("P")) in dg).shouldBeFalse()
        }

        it("should return false when checking if it contains an edge inside the graph but with a different weight") {
            (Edge(Vertex("C"), Vertex("D"), 1.3) in dg).shouldBeFalse()
            (Edge(Vertex("F"), Vertex("H"), 1.7) in dg).shouldBeFalse()
        }

        it("should return the vertices of the incoming edges to a vertex when checking the predecessors") {
            dg.predecessors(Vertex("A")).shouldBeEmpty()
            dg.predecessors(Vertex("G")) shouldBeEqualTo setOf(Vertex("D"), Vertex("E"))
            dg.predecessors(Vertex("F")) shouldBeEqualTo setOf(Vertex("B"))
        }

        it("should return the vertices of the outgoing edges of a vertex when checking the successors") {
            dg.successors(Vertex("H")).shouldBeEmpty()
            dg.successors(Vertex("F")) shouldBeEqualTo setOf(Vertex("D"), Vertex("H"))
            dg.successors(Vertex("B")) shouldBeEqualTo setOf(Vertex("F"))
        }

        it("should return the same digraph when searching for the subgraph connected to a vertex") {
            dg.getSubgraphConnectedTo(dg.vertices.first()) shouldBeEqualTo dg
        }

        it("should return true when checking it if is connected") {
            dg.isConnected().shouldBeTrue()
        }

        it("should return F, D, G and H as the reachable vertices from B") {
            dg.getReachableVerticesFrom(Vertex("B")) shouldBeEqualTo setOf(
                Vertex("F"),
                Vertex("D"),
                Vertex("G"),
                Vertex("H")
            )
        }

        it("should return D, E and G as the reachable vertices from C") {
            dg.getReachableVerticesFrom(Vertex("C")) shouldBeEqualTo setOf(Vertex("D"), Vertex("E"), Vertex("G"))
        }

        context("whose edge weights are inverted") {
            val idg = DirectedGraphBuilder()
                .vertex("A")
                .vertex("B")
                .vertex("C")
                .vertex("D")
                .vertex("E")
                .vertex("F")
                .vertex("G")
                .vertex("H")
                .edge().weight(-1.0).from("A").to("B")
                .edge().weight(-4.1).from("A").to("C")
                .edge().weight(-4.3).from("B").to("F")
                .edge().weight(-1.7).from("C").to("D")
                .edge().weight(-3.1).from("C").to("E")
                .edge().weight(-1.1).from("F").to("D")
                .edge().weight(-2.1).from("D").to("G")
                .edge().weight(-1.5).from("E").to("G")
                .edge().weight(-1.3).from("F").to("H")
                .build()

            it("should return the same graph with inverted edge weights") {
                dg.invertWeights() shouldBeEqualTo idg
            }
        }

        context("is reversed") {
            val rdg = DirectedGraphBuilder()
                .vertex("A")
                .vertex("B")
                .vertex("C")
                .vertex("D")
                .vertex("E")
                .vertex("F")
                .vertex("G")
                .vertex("H")
                .edge().weight(1.0).from("B").to("A")
                .edge().weight(4.1).from("C").to("A")
                .edge().weight(4.3).from("F").to("B")
                .edge().weight(1.7).from("D").to("C")
                .edge().weight(3.1).from("E").to("C")
                .edge().weight(1.1).from("D").to("F")
                .edge().weight(2.1).from("G").to("D")
                .edge().weight(1.5).from("G").to("E")
                .edge().weight(1.3).from("H").to("F")
                .build()

            it("should return the same graph with the edge directions reversed") {
                dg.reverse() shouldBeEqualTo rdg
            }
        }
    }

    describe("An unconnected directed graph formed by three connected subgraphs") {
        val dg = DirectedGraphBuilder()
            .vertex("A")
            .vertex("B")
            .vertex("C")
            .vertex("D")
            .vertex("E")
            .vertex("F")
            .vertex("G")
            .vertex("H")
            .edge().from("A").to("B")
            .edge().from("A").to("C")
            .edge().from("G").to("H")
            .edge().from("D").to("E")
            .edge().from("E").to("F")
            .edge().from("F").to("D")
            .build()

        it("should return each the connected component A, B, C when searching for the connected subgraph with A") {
            val subdg = DirectedGraphBuilder()
                .vertex("A")
                .vertex("B")
                .vertex("C")
                .edge().from("A").to("B")
                .edge().from("A").to("C")
                .build()

            dg.getSubgraphConnectedTo(dg.vertices.first { it.id == "A" }) shouldBeEqualTo subdg
        }

        it("should return each the connected component A, B, C when searching for the connected subgraph with A") {
            val subdg = DirectedGraphBuilder()
                .vertex("D")
                .vertex("E")
                .vertex("F")
                .edge().from("D").to("E")
                .edge().from("E").to("F")
                .edge().from("F").to("D")
                .build()

            dg.getSubgraphConnectedTo(dg.vertices.first { it.id == "E" }) shouldBeEqualTo subdg
        }

        it("should return each the connected component A, B, C when searching for the connected subgraph with A") {
            val subdg = DirectedGraphBuilder()
                .vertex("G")
                .vertex("H")
                .edge().from("G").to("H")
                .build()

            dg.getSubgraphConnectedTo(dg.vertices.first { it.id == "H" }) shouldBeEqualTo subdg
        }

        it("should return false when checking it if is connected") {
            dg.isConnected().shouldBeFalse()
        }

        it("should return D, E and F as the reachable vertices from either D, E or F") {
            dg.getReachableVerticesFrom(Vertex("D")) shouldBeEqualTo setOf(Vertex("D"), Vertex("E"), Vertex("F"))
            dg.getReachableVerticesFrom(Vertex("E")) shouldBeEqualTo setOf(Vertex("D"), Vertex("E"), Vertex("F"))
            dg.getReachableVerticesFrom(Vertex("F")) shouldBeEqualTo setOf(Vertex("D"), Vertex("E"), Vertex("F"))
        }

        it("should return B and C as the reachable vertices from A") {
            dg.getReachableVerticesFrom(Vertex("A")) shouldBeEqualTo setOf(Vertex("B"), Vertex("C"))
        }
    }
})
