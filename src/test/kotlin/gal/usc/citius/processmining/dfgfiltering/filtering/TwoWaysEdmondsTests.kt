package gal.usc.citius.processmining.dfgfiltering.filtering

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraphBuilder
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TwoWaysEdmondsTests : Spek({

    describe("A connected directed graph with many edges and no long cycles (only a self-cycle)") {
        val dg = getDFGManyEdgesNoLongCycles()

        context("get the minimum equivalent graph with maximum weight") {
            val mwmfdfg = dg.filterEdgesTWE()

            it("should be the same as the following MWMF-DFG") {
                val realMWMFDFG = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .vertex("K")
                    .vertex("L")
                    .vertex("M")
                    .vertex("N")
                    .edge().weight(5.0).from("A").to("B")
                    .edge().weight(3.0).from("A").to("C")
                    .edge().weight(4.0).from("A").to("D")
                    .edge().weight(7.0).from("B").to("H")
                    .edge().weight(8.0).from("B").to("E")
                    .edge().weight(3.0).from("C").to("F")
                    .edge().weight(4.0).from("D").to("F")
                    .edge().weight(6.0).from("D").to("I")
                    .edge().weight(2.0).from("D").to("G")
                    .edge().weight(2.0).from("E").to("H")
                    .edge().weight(5.0).from("F").to("H")
                    .edge().weight(7.0).from("G").to("I")
                    .edge().weight(9.0).from("G").to("L")
                    .edge().weight(3.0).from("H").to("N")
                    .edge().weight(6.0).from("I").to("J")
                    .edge().weight(8.0).from("J").to("K")
                    .edge().weight(4.0).from("K").to("N")
                    .edge().weight(4.0).from("L").to("M")
                    .edge().weight(5.0).from("M").to("N")
                    .build()

                mwmfdfg shouldBeEqualTo realMWMFDFG
            }
        }

        context("get the minimum equivalent graph with minimum weight") {
            val mwmfdfg = dg.filterEdgesTWE(true)

            it("should be the same as the following MWMF-DFG") {
                val realMWMFDFG = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .vertex("K")
                    .vertex("L")
                    .vertex("M")
                    .vertex("N")
                    .edge().weight(5.0).from("A").to("B")
                    .edge().weight(3.0).from("A").to("C")
                    .edge().weight(4.0).from("A").to("D")
                    .edge().weight(7.0).from("B").to("H")
                    .edge().weight(1.0).from("C").to("E")
                    .edge().weight(3.0).from("C").to("F")
                    .edge().weight(6.0).from("D").to("I")
                    .edge().weight(2.0).from("D").to("G")
                    .edge().weight(2.0).from("E").to("H")
                    .edge().weight(5.0).from("F").to("H")
                    .edge().weight(5.0).from("F").to("J")
                    .edge().weight(3.0).from("G").to("K")
                    .edge().weight(3.0).from("H").to("N")
                    .edge().weight(1.0).from("H").to("K")
                    .edge().weight(6.0).from("I").to("J")
                    .edge().weight(6.0).from("I").to("L")
                    .edge().weight(8.0).from("J").to("K")
                    .edge().weight(2.0).from("K").to("M")
                    .edge().weight(4.0).from("L").to("M")
                    .edge().weight(5.0).from("M").to("N")
                    .build()

                mwmfdfg shouldBeEqualTo realMWMFDFG
            }
        }
    }

    describe("A connected directed graph with one cycle") {
        val dg = DirectedGraphBuilder()
            .vertex("A")
            .vertex("B")
            .vertex("C")
            .vertex("D")
            .vertex("E")
            .vertex("F")
            .vertex("G")
            .vertex("H")
            .vertex("I")
            .vertex("J")
            .edge().weight(1.0).from("A").to("B")
            .edge().weight(5.0).from("A").to("C")
            .edge().weight(4.0).from("B").to("G")
            .edge().weight(3.0).from("B").to("D")
            .edge().weight(6.0).from("C").to("D")
            .edge().weight(6.0).from("D").to("J")
            .edge().weight(3.0).from("D").to("G")
            .edge().weight(8.0).from("D").to("E")
            .edge().weight(4.0).from("E").to("H")
            .edge().weight(5.0).from("E").to("I")
            .edge().weight(7.0).from("E").to("F")
            .edge().weight(7.0).from("F").to("C")
            .edge().weight(7.0).from("F").to("J")
            .edge().weight(3.0).from("G").to("H")
            .edge().weight(2.0).from("H").to("J")
            .edge().weight(3.0).from("I").to("J")
            .build()

        context("get the minimum spanning arborescence with maximum weight") {
            val msa = dg.getSpanningArborescence(dg.vertices.first { it.id == "A" })

            it("should have 47 as maximum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 47.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .edge().weight(1.0).from("A").to("B")
                    .edge().weight(5.0).from("A").to("C")
                    .edge().weight(4.0).from("B").to("G")
                    .edge().weight(6.0).from("C").to("D")
                    .edge().weight(8.0).from("D").to("E")
                    .edge().weight(4.0).from("E").to("H")
                    .edge().weight(5.0).from("E").to("I")
                    .edge().weight(7.0).from("E").to("F")
                    .edge().weight(7.0).from("F").to("J")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }

        context("get the minimum spanning arborescence with minimum weight") {
            val msa = dg.getSpanningArborescence(
                root = dg.vertices.first { it.id == "A" },
                minimum = true
            )

            it("should have 36 as minimum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 37.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .edge().weight(1.0).from("A").to("B")
                    .edge().weight(5.0).from("A").to("C")
                    .edge().weight(3.0).from("B").to("D")
                    .edge().weight(3.0).from("D").to("G")
                    .edge().weight(8.0).from("D").to("E")
                    .edge().weight(5.0).from("E").to("I")
                    .edge().weight(7.0).from("E").to("F")
                    .edge().weight(3.0).from("G").to("H")
                    .edge().weight(2.0).from("H").to("J")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }
    }

    describe("A connected directed graph with two joint cycles") {
        val dg = DirectedGraphBuilder()
            .vertex("A")
            .vertex("B")
            .vertex("C")
            .vertex("D")
            .vertex("E")
            .vertex("F")
            .vertex("G")
            .vertex("H")
            .vertex("I")
            .vertex("J")
            .edge().weight(9.0).from("A").to("B")
            .edge().weight(1.0).from("A").to("C")
            .edge().weight(5.0).from("B").to("G")
            .edge().weight(9.0).from("C").to("D")
            .edge().weight(9.0).from("D").to("E")
            .edge().weight(9.0).from("E").to("H")
            .edge().weight(9.0).from("E").to("I")
            .edge().weight(9.0).from("E").to("F")
            .edge().weight(9.0).from("F").to("C")
            .edge().weight(4.0).from("G").to("D")
            .edge().weight(9.0).from("H").to("G")
            .edge().weight(3.0).from("H").to("J")
            .edge().weight(9.0).from("I").to("J")
            .build()

        context("get the minimum spanning arborescence with maximum weight") {
            val msa = dg.getSpanningArborescence(dg.vertices.first { it.id == "A" })

            it("should have 73 as maximum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 73.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .edge().weight(9.0).from("A").to("B")
                    .edge().weight(1.0).from("A").to("C")
                    .edge().weight(9.0).from("C").to("D")
                    .edge().weight(9.0).from("D").to("E")
                    .edge().weight(9.0).from("E").to("H")
                    .edge().weight(9.0).from("E").to("I")
                    .edge().weight(9.0).from("E").to("F")
                    .edge().weight(9.0).from("H").to("G")
                    .edge().weight(9.0).from("I").to("J")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }

        context("get the minimum spanning arborescence with minimum weight") {
            val msa = dg.getSpanningArborescence(
                root = dg.vertices.first { it.id == "A" },
                minimum = true
            )

            it("should have 58 as minimum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 58.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .edge().weight(9.0).from("A").to("B")
                    .edge().weight(1.0).from("A").to("C")
                    .edge().weight(5.0).from("B").to("G")
                    .edge().weight(9.0).from("D").to("E")
                    .edge().weight(9.0).from("E").to("H")
                    .edge().weight(9.0).from("E").to("I")
                    .edge().weight(9.0).from("E").to("F")
                    .edge().weight(4.0).from("G").to("D")
                    .edge().weight(3.0).from("H").to("J")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }
    }

    describe("Another connected directed graph with two joint cycles") {
        val dg = DirectedGraphBuilder()
            .vertex("A")
            .vertex("B")
            .vertex("C")
            .vertex("D")
            .vertex("E")
            .vertex("F")
            .vertex("G")
            .vertex("H")
            .vertex("I")
            .vertex("J")
            .edge().weight(9.0).from("A").to("B")
            .edge().weight(1.0).from("A").to("C")
            .edge().weight(5.0).from("B").to("G")
            .edge().weight(1.0).from("B").to("D")
            .edge().weight(9.0).from("C").to("D")
            .edge().weight(9.0).from("D").to("E")
            .edge().weight(9.0).from("E").to("H")
            .edge().weight(9.0).from("E").to("I")
            .edge().weight(9.0).from("E").to("F")
            .edge().weight(9.0).from("F").to("C")
            .edge().weight(7.0).from("G").to("D")
            .edge().weight(9.0).from("H").to("G")
            .edge().weight(3.0).from("H").to("J")
            .edge().weight(9.0).from("I").to("J")
            .build()

        context("get the minimum spanning arborescence with maximum weight") {
            val msa = dg.getSpanningArborescence(dg.vertices.first { it.id == "A" })

            it("should have 75 as maximum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 75.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .edge().weight(9.0).from("A").to("B")
                    .edge().weight(5.0).from("B").to("G")
                    .edge().weight(9.0).from("D").to("E")
                    .edge().weight(9.0).from("E").to("H")
                    .edge().weight(9.0).from("E").to("I")
                    .edge().weight(9.0).from("E").to("F")
                    .edge().weight(9.0).from("F").to("C")
                    .edge().weight(7.0).from("G").to("D")
                    .edge().weight(9.0).from("I").to("J")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }

        context("get the minimum spanning arborescence with minimum weight") {
            val msa = dg.getSpanningArborescence(
                root = dg.vertices.first { it.id == "A" },
                minimum = true
            )

            it("should have 55 as minimum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 55.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .edge().weight(9.0).from("A").to("B")
                    .edge().weight(1.0).from("A").to("C")
                    .edge().weight(5.0).from("B").to("G")
                    .edge().weight(1.0).from("B").to("D")
                    .edge().weight(9.0).from("D").to("E")
                    .edge().weight(9.0).from("E").to("H")
                    .edge().weight(9.0).from("E").to("I")
                    .edge().weight(9.0).from("E").to("F")
                    .edge().weight(3.0).from("H").to("J")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }
    }

    describe("A connected directed graph with many edges and multiple cycles") {
        val dg = DirectedGraphBuilder()
            .vertex("A")
            .vertex("B")
            .vertex("C")
            .vertex("D")
            .vertex("E")
            .vertex("F")
            .vertex("G")
            .vertex("H")
            .vertex("I")
            .vertex("J")
            .vertex("K")
            .vertex("L")
            .vertex("M")
            .vertex("N")
            .vertex("O")
            .vertex("P")
            .vertex("Q")
            .edge().weight(7.0).from("A").to("J")
            .edge().weight(6.0).from("A").to("B")
            .edge().weight(7.0).from("A").to("C")
            .edge().weight(1.0).from("A").to("D")
            .edge().weight(4.0).from("B").to("J")
            .edge().weight(3.0).from("B").to("I")
            .edge().weight(5.0).from("C").to("I")
            .edge().weight(1.0).from("C").to("M")
            .edge().weight(2.0).from("C").to("H")
            .edge().weight(4.0).from("D").to("F")
            .edge().weight(2.0).from("E").to("D")
            .edge().weight(3.0).from("E").to("F")
            .edge().weight(5.0).from("F").to("G")
            .edge().weight(1.0).from("G").to("C")
            .edge().weight(6.0).from("G").to("H")
            .edge().weight(3.0).from("G").to("N")
            .edge().weight(4.0).from("G").to("K")
            .edge().weight(4.0).from("G").to("E")
            .edge().weight(3.0).from("H").to("M")
            .edge().weight(7.0).from("H").to("P")
            .edge().weight(5.0).from("I").to("J")
            .edge().weight(6.0).from("I").to("O")
            .edge().weight(1.0).from("J").to("Q")
            .edge().weight(3.0).from("J").to("O")
            .edge().weight(3.0).from("K").to("G")
            .edge().weight(1.0).from("K").to("L")
            .edge().weight(5.0).from("L").to("K")
            .edge().weight(2.0).from("M").to("O")
            .edge().weight(5.0).from("M").to("Q")
            .edge().weight(3.0).from("M").to("P")
            .edge().weight(4.0).from("N").to("P")
            .edge().weight(2.0).from("N").to("Q")
            .edge().weight(7.0).from("O").to("Q")
            .edge().weight(8.0).from("P").to("Q")
            .build()

        context("get the minimum spanning arborescence with maximum weight") {
            val msa = dg.getSpanningArborescence(dg.vertices.first { it.id == "A" })

            it("should have 77 as maximum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 77.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .vertex("K")
                    .vertex("L")
                    .vertex("M")
                    .vertex("N")
                    .vertex("O")
                    .vertex("P")
                    .vertex("Q")
                    .edge().weight(7.0).from("A").to("J")
                    .edge().weight(6.0).from("A").to("B")
                    .edge().weight(7.0).from("A").to("C")
                    .edge().weight(1.0).from("A").to("D")
                    .edge().weight(5.0).from("C").to("I")
                    .edge().weight(4.0).from("D").to("F")
                    .edge().weight(5.0).from("F").to("G")
                    .edge().weight(6.0).from("G").to("H")
                    .edge().weight(3.0).from("G").to("N")
                    .edge().weight(4.0).from("G").to("K")
                    .edge().weight(4.0).from("G").to("E")
                    .edge().weight(3.0).from("H").to("M")
                    .edge().weight(7.0).from("H").to("P")
                    .edge().weight(6.0).from("I").to("O")
                    .edge().weight(1.0).from("K").to("L")
                    .edge().weight(8.0).from("P").to("Q")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }

        context("get the minimum spanning arborescence with minimum weight") {
            val msa = dg.getSpanningArborescence(
                root = dg.vertices.first { it.id == "A" },
                minimum = true
            )

            it("should have 45 as minimum overall weight") {
                msa.edges.sumByDouble { it.weight } shouldBeEqualTo 45.0
            }

            it("should have one incoming edge per vertex except for the root") {
                msa.edges.size shouldBeEqualTo (msa.vertices.size - 1)
            }

            it("should be equal to the following arborescence") {
                val realMSA = DirectedGraphBuilder()
                    .vertex("A")
                    .vertex("B")
                    .vertex("C")
                    .vertex("D")
                    .vertex("E")
                    .vertex("F")
                    .vertex("G")
                    .vertex("H")
                    .vertex("I")
                    .vertex("J")
                    .vertex("K")
                    .vertex("L")
                    .vertex("M")
                    .vertex("N")
                    .vertex("O")
                    .vertex("P")
                    .vertex("Q")
                    .edge().weight(6.0).from("A").to("B")
                    .edge().weight(1.0).from("A").to("D")
                    .edge().weight(4.0).from("B").to("J")
                    .edge().weight(3.0).from("B").to("I")
                    .edge().weight(1.0).from("C").to("M")
                    .edge().weight(2.0).from("C").to("H")
                    .edge().weight(4.0).from("D").to("F")
                    .edge().weight(5.0).from("F").to("G")
                    .edge().weight(1.0).from("G").to("C")
                    .edge().weight(3.0).from("G").to("N")
                    .edge().weight(4.0).from("G").to("K")
                    .edge().weight(4.0).from("G").to("E")
                    .edge().weight(1.0).from("J").to("Q")
                    .edge().weight(1.0).from("K").to("L")
                    .edge().weight(2.0).from("M").to("O")
                    .edge().weight(3.0).from("M").to("P")
                    .build()

                msa shouldBeEqualTo realMSA
            }
        }
    }
})
