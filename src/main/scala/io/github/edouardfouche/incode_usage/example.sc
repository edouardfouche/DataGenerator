
/**
  * Import Generator classes
  */

import io.github.edouardfouche.generators._

/**
  * Create Generator instance
  */

val independent_generator = Independent(nDim = 5, noise = 0.0, noisetype = "gaussian", discretize = 0)
val independent: Array[Array[Double]] = independent_generator.generate(n = 100000) // Dim: 100000 x 5