package gal.usc.citius.processmining.dfgfiltering.settings

import java.io.File

data class DFGFilteringSettings(
    val algorithm: Algorithm,
    val inputFile: File,
    val outputFile: File,
    val minimizing: Boolean = false,
    val runtime: Boolean = false,
    val all: Boolean = false
)

enum class Algorithm { G, TWE, TWEG }
