package gal.usc.citius.processmining.dfgfiltering

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException
import com.xenomachina.argparser.mainBody
import gal.usc.citius.processmining.dfgfiltering.filtering.isValidFDFG
import gal.usc.citius.processmining.dfgfiltering.filtering.filterEdgesGreedy
import gal.usc.citius.processmining.dfgfiltering.filtering.filterEdgesTWE
import gal.usc.citius.processmining.dfgfiltering.filtering.filterEdgesTWEG
import gal.usc.citius.processmining.dfgfiltering.graph.DirectedGraph
import gal.usc.citius.processmining.dfgfiltering.input.readDirectedGraph
import gal.usc.citius.processmining.dfgfiltering.output.writeToFile
import gal.usc.citius.processmining.dfgfiltering.settings.DFGFilteringSettings
import gal.usc.citius.processmining.dfgfiltering.settings.Algorithm
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Executable to run the DFG edge filtering techniques.
 *
 *    usage: [-h] --g [-r] [-m] [-a] INPUT-FILE OUTPUT-FILE
 *
 *    required arguments:
 *    --g, --twe, --tweg    filtering algorithm to use
 *
 *    optional arguments:
 *    -h, --help            show this help message and exit
 *    -r, --runtime         print filtering runtime
 *    -m, --minimizing      minimize the total weight of the F-DFG
 *    -a, --all-dfgs        compute the F-DFG of all number of edges
 *
 *    positional arguments:
 *    INPUT-FILE            input file path (TGF format).
 *    OUTPUT-FILE           output file path to write the dfg.
 */
fun main(args: Array<String>) {
    // Parse params
    val settings = parseParams(args)

    // Read DFG from file
    val completeDFG = settings.inputFile.readDirectedGraph()

    // Filter DFG
    val filteredDFG: DirectedGraph
    val runtime = measureTimeMillis {
        filteredDFG = when (settings.algorithm) {
            Algorithm.G -> completeDFG.filterEdgesGreedy(settings.minimizing)
            Algorithm.TWE -> completeDFG.filterEdgesTWE(settings.minimizing)
            Algorithm.TWEG -> completeDFG.filterEdgesTWEG(settings.minimizing)
        }
    }
    filteredDFG.isValidFDFG(completeDFG)

    // Write DFG to file
    filteredDFG.writeToFile(settings.outputFile)
    if (settings.all) {
        // Compute all greater F-DFGs and write them to files
        val fileAbsoluteName = settings.outputFile.absolutePath.removeSuffix(".tgf")
        val removedArcs = (completeDFG.edges - filteredDFG.edges).sortedByDescending { it.weight }
        var augmentedDFG = filteredDFG.copy()
        removedArcs.forEach { edge ->
            augmentedDFG = augmentedDFG.copy(edges = augmentedDFG.edges + edge)
            augmentedDFG.writeToFile(File("${fileAbsoluteName}_${augmentedDFG.edges.size}.tgf"))
        }
    }

    if (settings.runtime) {
        println("Filtering runtime: ${runtime}ms")
    }
}

/**
 * Parse the params of the script.
 */
private fun parseParams(args: Array<String>): DFGFilteringSettings = mainBody {
    val parser = ArgParser(args)

    val algorithm by parser.mapping(
        "--g" to Algorithm.G,
        "--twe" to Algorithm.TWE,
        "--tweg" to Algorithm.TWEG,
        help = "filtering algorithm to use"
    )

    val runtime by parser.flagging(
        "-r", "--runtime",
        help = "print filtering runtime"
    )

    val minimizing by parser.flagging(
        "-m", "--minimizing",
        help = "minimize the total weight of the F-DFG"
    )

    val all by parser.flagging(
        "-a", "--all-dfgs",
        help = "compute the F-DFG of all number of edges"
    )

    val inputFile by parser.positional(
        name = "INPUT-FILE",
        help = "input file path (TGF format)."
    ) { File(this) }
        .addValidator {
            if (!value.path.endsWith("tgf", true))
                throw InvalidArgumentException("INPUT-FILE must be a .tgf file")
        }

    val outputFile by parser.positional(
        name = "OUTPUT-FILE",
        help = "output file path to write the dfg."
    ) { File(this) }
        .addValidator {
            if (!value.path.endsWith("tgf", true))
                throw InvalidArgumentException("OUTPUT-FILE must be a .tgf file")
        }

    DFGFilteringSettings(algorithm, inputFile, outputFile, minimizing, runtime, all)
}
