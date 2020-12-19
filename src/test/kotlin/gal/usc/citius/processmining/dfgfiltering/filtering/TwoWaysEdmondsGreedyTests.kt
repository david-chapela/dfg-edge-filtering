package gal.usc.citius.processmining.dfgfiltering.filtering

import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraphBuilder
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TwoWaysEdmondsGreedyTests : Spek({

    describe("A connected directed graph with many edges and no long cycles (only a self-cycle)") {
        val dg = getDFGManyEdgesNoLongCycles()

        context("get the minimum equivalent graph with maximum weight using the TWE+G method") {
            val mwmfdfg = dg.filterEdgesTWEG()

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
                    .edge().weight(8.0).from("B").to("E")
                    .edge().weight(3.0).from("C").to("F")
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

        context("get the minimum equivalent graph with minimum weight using the TWE+G method") {
            val mwmfdfg = dg.filterEdgesTWEG(true)

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
                    .edge().weight(5.0).from("F").to("J")
                    .edge().weight(3.0).from("G").to("K")
                    .edge().weight(1.0).from("H").to("K")
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
})
